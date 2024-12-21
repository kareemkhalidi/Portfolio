import nfl_data_py as nfl # pip install nfl-data-py (note: not compatible with python 3.12, must use 3.11 or earlier)
import pandas as pd # pip install pandas

teams = [
    'ARI',
    'ATL',
    'BAL',
    'BUF',
    'CAR',
    'CHI',
    'CIN',
    'CLE',
    'DAL',
    'DEN',
    'DET',
    'GB',
    'HOU',
    'IND',
    'JAX',
    'KC',
    'LAC',
    'LAR',
    'LV',
    'MIA',
    'MIN',
    'NE',
    'NO',
    'NYG',
    'NYJ',
    'PHI',
    'PIT',
    'SEA',
    'SF',
    'TB',
    'TEN',
    'WAS'
]

class NFLStats:
    """Class for working with play by play data for the NFL."""

    def __init__(self):
        self._pbp_data = {}
        self._roster_data = {}

    def _get_pbp_data(self, season, team, is_team_on_defense=False):
        """
        Gets play by play data for the NFL.

        :param season: int, season to get data for, ex: 2024
        :param team: str, abbrv of team to get pbp data for, ex: 'SEA'
        :param is_team_on_defense: bool, returns only plays where team is on defense if true and offense if false
        :return: pd.Dataframe containing pbp data
        """

        # if season has not had its pbp data imported yet, import and save it
        if season not in self._pbp_data.keys():
            self._pbp_data[season] = nfl.import_pbp_data([season])
            self._pbp_data[season] = nfl.clean_nfl_data(self._pbp_data[season])
            self._pbp_data[season] = self._pbp_data[season].fillna(0)

        # get and return the requested plays
        if is_team_on_defense:
            return self._pbp_data[season][self._pbp_data[season]['defteam'] == team]
        else:
            return self._pbp_data[season][self._pbp_data[season]['posteam'] == team]

    def get_team_rush_stats(self, season, team, rush_location=None, rush_gap=None, is_team_on_defense=False):
        """
        Gets season long rushing stats for all players on the input team during the input season.

        NOTE: runs to the rush_location 'middle' have no rush gap. rush_gap will be ignored if 'middle' is the input rush_location.

        :param season: int, season to get data for, ex: 2024
        :param team: str, abbrv of team to get pbp data for, ex: 'SEA'
        :param rush_location: optional str, only uses data from rushes to the specified location ('left', 'middle', 'right')
        :param rush_gap: optional str, only uses data from rushes to the specified gap ('end', 'tackle', 'guard')
        :param is_team_on_defense: optional bool, defaults to False, only uses data from plays where team is on defense if true and offense if false
        :return: pd.Dataframe containing rushing stats, split into rows by player and week
        """

        # get pbp data and filter for all rushing plays
        pbp = self._get_pbp_data(season, team, is_team_on_defense=is_team_on_defense)
        pbp = pbp[pbp['play_type'] == 'run']

        # filter pbp by rush location (if provided and valid)
        if rush_location is not None:
            rush_location = rush_location.lower()
            if rush_location in ['left', 'middle', 'right']:
                # ignore rush gap (if run is to middle)
                if rush_location == 'middle':
                    rush_gap = None
                pbp = pbp[pbp['run_location'] == rush_location]
            else:
                raise Exception(f'Invalid rush location selected ({rush_location}). Valid rush locations: "left", "middle", "right".')

        # filter pbp by rush gap (if provided and valid)
        if rush_gap is not None:
            rush_gap = rush_gap.lower()
            if rush_gap in ['end', 'tackle', 'guard']:
                pbp = pbp[pbp['run_gap'] == rush_gap]
            else:
                raise Exception(f'Invalid rush gap selected ({rush_gap}). Valid rush gaps: "end", "tackle", "guard".')

        # group plays by player carrying the ball and aggregate to calculate rushing stats
        rushing_stats = pbp.groupby(['rusher_player_id', 'rusher_player_name', 'week']).agg(
            player_id=('rusher_player_id', 'first'),
            player_name=('rusher_player_name', 'first'),
            week=('week', 'first'),
            team=('posteam', 'first'),
            car=('rusher_player_id', 'size'),
            rush_yd=('rushing_yards', 'sum'),
            rush_td=('td_player_id', lambda x: (x != 0).sum())
        ).reset_index(drop=True)

        # convert stats to int format
        for col in ['car', 'rush_yd', 'rush_td']:
            rushing_stats[col] = rushing_stats[col].astype(int)

        # calculate yards per carry and add as a new column
        rushing_stats['ypcar'] = round(rushing_stats['rush_yd'] / rushing_stats['car'], 2)

        # sort by carries high to low and return
        return rushing_stats.sort_values(by='car', ascending=False)

    def get_team_rec_stats(self, season, team, pass_length=None, pass_location=None, is_team_on_defense=False):
        """
        Gets season long receiving stats for all players on the input team during the input season.

        :param season: int, season to get data for, ex: 2024
        :param team: str, abbrv of team to get pbp data for, ex: 'SEA'
        :param pass_length: optional str, only uses data from passes of the specified length ("short", "deep")
        :param pass_location: optional str, only uses data from passes to the specified location ("left", "middle", "right")
        :param player_position: optional str, gets rushing stats for players of the input position only, ex: 'QB', 'RB'
        :param is_team_on_defense: optional bool, defaults to False, only uses data from plays where team is on defense if true and offense if false
        :return: pd.Dataframe containing passing stats, split into rows by player and week
        """

        # get pbp data and filter for all passing plays
        pbp = self._get_pbp_data(season, team, is_team_on_defense=is_team_on_defense)
        pbp = pbp[pbp['play_type'] == 'pass']

        # remove sacks and 2 point conversions, so they do not count as targets
        pbp = pbp[pbp['two_point_attempt'] == 0]
        pbp = pbp[pbp['sack'] == 0]

        # filter pbp by pass length (if provided and valid)
        if pass_length is not None:
            pass_length = pass_length.lower()
            if pass_length in ['short', 'deep']:
                pbp = pbp[pbp['pass_length'] == pass_length]
            else:
                raise Exception(f'Invalid pass length selected ({pass_length}). Valid pass lengths: "short", "deep".')

        # filter pbp by pass location (if provided and valid)
        if pass_location is not None:
            pass_location = pass_location.lower()
            if pass_location in ['left', 'middle', 'right']:
                pbp = pbp[pbp['pass_location'] == pass_location]
            else:
                raise Exception(f'Invalid pass location selected ({pass_location}). Valid pass locations: "left", "middle", "right".')

        # group plays by player being targeted and aggregate to calculate receiving stats
        receiving_stats = pbp.groupby(['receiver_player_id', 'receiver_player_name', 'week']).agg(
            player_id=('receiver_player_id', 'first'),
            player_name=('receiver_player_name', 'first'),
            week=('week', 'first'),
            team=('posteam', 'first'),
            tar=('receiver_player_id', 'size'),
            rec=('complete_pass', 'sum'),
            rec_yd=('receiving_yards', 'sum'),
            rec_td=('td_player_id', lambda x: (x != 0).sum()),
        ).reset_index(drop=True)

        # convert stats to int format
        for col in ['tar', 'rec', 'rec_yd', 'rec_td']:
            receiving_stats[col] = receiving_stats[col].astype(int)

        # calculate yards per target and reception and add as new columns (float rounded to 2 digits)
        receiving_stats['yptar'] = round(receiving_stats['rec_yd'] / receiving_stats['tar'], 2)
        receiving_stats['yprec'] = round(receiving_stats['rec_td'] / receiving_stats['rec'], 2)

        # sort by targets high to low and return
        return receiving_stats.sort_values(by='tar', ascending=False)

    def get_team_pass_stats(self, season, team, pass_length=None, pass_location=None, is_team_on_defense=False):
        """
        Gets season long passing stats for all players on the input team during the input season.

        :param season: int, season to get data for, ex: 2024
        :param team: str, abbrv of team to get pbp data for, ex: 'SEA'
        :param pass_length: optional str, only uses data from passes of the specified length ("short", "deep")
        :param pass_location: optional str, only uses data from passes to the specified location ("left", "middle", "right")
        :param is_team_on_defense: optional bool, defaults to False, only uses data from plays where team is on defense if true and offense if false
        :return: pd.Dataframe containing passing stats, split into rows by player and week
        """

        # get pbp data and filter for all passing plays
        pbp = self._get_pbp_data(season, team, is_team_on_defense=is_team_on_defense)
        pbp = pbp[pbp['play_type'] == 'pass']

        # remove sacks and 2 point conversions, so they do not count as pass attempts
        pbp = pbp[pbp['two_point_attempt'] == 0]
        pbp = pbp[pbp['sack'] == 0]

        # filter pbp by pass length (if provided and valid)
        if pass_length is not None:
            pass_length = pass_length.lower()
            if pass_length in ['short', 'deep']:
                pbp = pbp[pbp['pass_length'] == pass_length]
            else:
                raise Exception(f'Invalid pass length selected ({pass_length}). Valid pass lengths: "short", "deep".')

        # filter pbp by pass location (if provided and valid)
        if pass_location is not None:
            pass_location = pass_location.lower()
            if pass_location in ['left', 'middle', 'right']:
                pbp = pbp[pbp['pass_location'] == pass_location]
            else:
                raise Exception(
                    f'Invalid pass location selected ({pass_location}). Valid pass locations: "left", "middle", "right".')

        # group plays by player throwing the ball and aggregate to calculate passing stats
        passing_stats = pbp.groupby(['passer_player_id', 'passer_player_name', 'week']).agg(
            player_id=('passer_player_id', 'first'),
            player_name=('passer_player_name', 'first'),
            week=('week', 'first'),
            team=('posteam', 'first'),
            att=('passer_player_id', 'size'),
            cmp=('complete_pass', 'sum'),
            pass_yd=('passing_yards', 'sum'),
            pass_td=('td_player_id', lambda x: (x != 0).sum()),
            int=('interception', 'sum')
        ).reset_index(drop=True)

        # convert stats to int format
        for col in ['att', 'cmp', 'pass_yd', 'pass_td', 'int']:
            passing_stats[col] = passing_stats[col].astype(int)

        # calculate yards per attempt and completion and add as new columns (float rounded to 2 digits)
        passing_stats['ypatt'] = round(passing_stats['pass_yd'] / passing_stats['att'], 2)
        passing_stats['ypcmp'] = round(passing_stats['pass_yd'] / passing_stats['cmp'], 2)

        # sort by attempts high to low and return
        return passing_stats.sort_values(by='att', ascending=False)

    def get_team_rush_summary(self, season, team, is_team_on_defense=False):
        """
        Gets a summary of a teams overall rushing performance on the season so far, split into 8 sections by rush type:
        ('overall', 'middle', 'left_guard', 'left_tackle', 'left_end', 'right_guard', 'right_tackle', 'right_end')

        :param season: int, season to get data for, ex: 2024
        :param team: str, abbrv of team to get pbp data for, ex: 'SEA'
        :param is_team_on_defense: optional bool, defaults to False, only uses data from plays where team is on defense if true and offense if false
        :return: pd.dataframe containing rushing stats, split into row by rush type
        """

        # initialize loop variables
        summaries = []
        games_played = -1

        # loop over rush types
        for rush_type in ['overall', 'left_end', 'left_tackle', 'left_guard', 'middle', 'right_guard', 'right_tackle', 'right_end']:
            # get rushing stats for current rush type
            if rush_type == 'overall':
                rush_stats = self.get_team_rush_stats(season, team, is_team_on_defense=is_team_on_defense)
                games_played = rush_stats['week'].nunique()
            elif rush_type == 'middle':
                rush_stats = self.get_team_rush_stats(season, team, rush_location=rush_type, is_team_on_defense=is_team_on_defense)
            else:
                rush_type_split = rush_type.split('_', 1)
                rush_stats = self.get_team_rush_stats(season, team, rush_location=rush_type_split[0], rush_gap=rush_type_split[1], is_team_on_defense=is_team_on_defense)
            # calculate rushing stats summary and add to summaries list
            summary = {'rush_type': rush_type}
            for stat in ['car', 'rush_yd', 'rush_td']:
                summary[stat] = round(rush_stats[stat].sum() / games_played, 2)
            summary['ypcar'] = round(summary['rush_yd'] / summary['car'], 2)
            summaries.append(summary)

        # combine summaries into a dataframe and return it
        return pd.DataFrame(summaries)

    def get_team_rec_summary(self, season, team, is_team_on_defense=False):
        """
        Gets a summary of a teams overall receiving performance on the season so far, split into 7 sections by pass type:
        ('overall', 'short_left', 'short_middle', 'short_right', 'deep_left', 'deep_middle', 'deep_right')

        :param season: int, season to get data for, ex: 2024
        :param team: str, abbrv of team to get pbp data for, ex: 'SEA'
        :param is_team_on_defense: optional bool, defaults to False, only uses data from plays where team is on defense if true and offense if false
        :return: pd.dataframe containing receiving stats, split into row by pass type
        """

        # initialize loop variables
        summaries = []
        games_played = -1

        # loop over pass types
        for pass_type in ['overall', 'short_left', 'short_middle', 'short_right', 'deep_left', 'deep_middle', 'deep_right']:
            # get receiving stats for current pass type
            if pass_type == 'overall':
                rec_stats = self.get_team_rec_stats(season, team, is_team_on_defense=is_team_on_defense)
                games_played = rec_stats['week'].nunique()
            else:
                pass_type_split = pass_type.split('_', 1)
                rec_stats = self.get_team_rec_stats(season, team, pass_length=pass_type_split[0], pass_location=pass_type_split[1], is_team_on_defense=is_team_on_defense)
            # calculate receiving stats summary and add to summaries list
            summary = {'pass_type': pass_type}
            for stat in ['tar', 'rec', 'rec_yd', 'rec_td']:
                summary[stat] = round(rec_stats[stat].sum() / games_played, 2)
            summary['yptar'] = round(summary['rec_yd'] / summary['tar'], 2)
            summary['yprec'] = round(summary['rec_yd'] / summary['rec'], 2)
            summaries.append(summary)

        # combine summaries into a dataframe and return it
        return pd.DataFrame(summaries)

    def get_team_pass_summary(self, season, team, is_team_on_defense=False):
        """
        Gets a summary of a teams overall passing performance on the season so far, split into 7 sections by pass type:
        ('overall', 'short_left', 'short_middle', 'short_right', 'deep_left', 'deep_middle', 'deep_right')

        :param season: int, season to get data for, ex: 2024
        :param team: str, abbrv of team to get pbp data for, ex: 'SEA'
        :param is_team_on_defense: optional bool, defaults to False, only uses data from plays where team is on defense if true and offense if false
        :return: pd.dataframe containing passing stats, split into row by pass type
        """

        # initialize loop variables
        summaries = []
        games_played = -1

        # loop over pass types
        for pass_type in ['overall', 'short_left', 'short_middle', 'short_right', 'deep_left', 'deep_middle', 'deep_right']:
            # get passing stats for current pass type
            if pass_type == 'overall':
                pass_stats = self.get_team_pass_stats(season, team, is_team_on_defense=is_team_on_defense)
                games_played = pass_stats['week'].nunique()
            else:
                pass_type_split = pass_type.split('_', 1)
                pass_stats = self.get_team_pass_stats(season, team, pass_length=pass_type_split[0], pass_location=pass_type_split[1], is_team_on_defense=is_team_on_defense)
            # calculate passing stats summary and add to summaries list
            summary = {'pass_type': pass_type}
            for stat in ['att', 'cmp', 'pass_yd', 'pass_td', 'int']:
                summary[stat] = round(pass_stats[stat].sum() / games_played, 2)
            summary['ypatt'] = round(summary['pass_yd'] / summary['att'], 2)
            summary['ypcmp'] = round(summary['pass_yd'] / summary['cmp'], 2)
            summaries.append(summary)

        # combine summaries into a dataframe and return it
        return pd.DataFrame(summaries)

if __name__ == '__main__':
    # get user input for season
    season_in = int(input('Season (int format, ex: 2024): '))

    # get user input for team on offense
    pos_team_in = input('Team on offense (abbrv format, ex: "SEA"): ').upper()
    if pos_team_in not in teams:
        print('ERROR: selected team is not valid.')
        quit()

    # get user input for team on defense
    def_team_in = input('Team on defense (abbrv format, ex: "SEA"): ').upper()
    if def_team_in not in teams:
        print('ERROR: selected team is not valid.')
        quit()

    # get user input for type of stat to compare
    stat_type_in = input('Type of stat to analyze ("rush", "rec", "pass"): ').lower()

    # get summaries for offense of pos_team and defense of def_team for the selected stat type
    nfl_stats_getter = NFLStats()
    if stat_type_in == 'rush':
        pos_team_summary = nfl_stats_getter.get_team_rush_summary(season_in, pos_team_in)
        def_team_summary = nfl_stats_getter.get_team_rush_summary(season_in, def_team_in, is_team_on_defense=True)
        section_split = 'rush_type'
        stats = ['car', 'rush_yd', 'rush_td', 'ypcar']
    elif stat_type_in == 'rec':
        pos_team_summary = nfl_stats_getter.get_team_rec_summary(season_in, pos_team_in)
        def_team_summary = nfl_stats_getter.get_team_rec_summary(season_in, def_team_in, is_team_on_defense=True)
        section_split = 'pass_type'
        stats = ['tar', 'rec', 'rec_yd', 'rec_td', 'yptar', 'yprec']
    elif stat_type_in == 'pass':
        pos_team_summary = nfl_stats_getter.get_team_pass_summary(season_in, pos_team_in)
        def_team_summary = nfl_stats_getter.get_team_pass_summary(season_in, def_team_in, is_team_on_defense=True)
        section_split = 'pass_type'
        stats = ['att', 'cmp', 'pass_yd', 'pass_td', 'int', 'ypatt', 'ypcmp']
    else:
        print('ERROR: selected stat is not valid.')
        quit()

    # print the summary breakdown of the matchup
    print(f'{pos_team_in} {stat_type_in[0].upper() + stat_type_in[1:].lower()} Offense VS {def_team_in} {stat_type_in[0].upper() + stat_type_in[1:].lower()} Defense')
    for section in pos_team_summary[section_split].unique():
        print('------------------------------')
        print(f'{section} {stat_type_in} stats')
        for stat in stats:
            print(f'  - {pos_team_in}: {pos_team_summary[pos_team_summary[section_split] == section][stat].item()} {stat} per game')
            print(f'  - {def_team_in}: {def_team_summary[def_team_summary[section_split] == section][stat].item()} {stat} allowed per game')
            print()
