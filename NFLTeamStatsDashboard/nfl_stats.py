import nfl_data_py as nfl # pip install nfl-data-py (note: not compatible with python 3.12, must use 3.11 or earlier)
import pandas as pd # pip install pandas

class NFLStats:
    """Class for working with play by play data for the NFL."""

    SEASONS = [
        2024,
        2023,
        2022,
        2021,
        2020,
    ]
    TEAMS = [
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
        'WAS',
    ]

    def __init__(self):
        self._pbp_data = {}
        self._roster_data = {}

    def get_pbp_data(self, season):
        """
        Gets play by play data for the NFL.

        :param season: int, season to get data for, ex: 2024.
        :return: pd.Dataframe containing all pbp data for the selected season.
        """

        # IMPORT AND SAVE SEASON PBP DATA IF IT HAS NOT YET BEEN SAVED
        if season not in self._pbp_data.keys():
            self._pbp_data[season] = nfl.import_pbp_data([season])
            self._pbp_data[season] = nfl.clean_nfl_data(self._pbp_data[season])
            self._pbp_data[season] = self._pbp_data[season].fillna(0)
        return self._pbp_data[season]

    def _get_roster_data(self, season):
        """
        Gets seasonal roster data for the NFL.

        :param season: int, season to get data for, ex: 2024.
        :return: pd.Dataframe containing seasonal roster data for all NFL teams.
        """

        # IMPORT AND SAVE PLAYER ROSTER DATA IF IT HAS NOT YET BEEN SAVED
        if season not in self._roster_data.keys():
            self._roster_data[season] = nfl.import_seasonal_rosters([season])
        return self._roster_data[season]

    def _get_player_pos(self, player_id, season):
        """
        Gets the position of the selected player.

        :param player_id: str, nfl_data_py player id of player to get position for.
        :param season: int, gets position based on roster data from selected season, ex: 2024.
        :return: str, position of player, ex: 'QB' or 'WR'.
        """

        roster_data = self._get_roster_data(season)
        player_data = roster_data[roster_data['player_id'] == player_id]
        if len(player_data) == 0:
            return ''
        else:
            return player_data['position'].item()

    def filter_pbp_basic(self, pbp, team, side_of_ball, play_type, weeks=None, pos=None, season=None, pass_len=None, pass_loc=None, rush_loc=None, rush_gap=None):
        """
        Filters the provided pbp data based on the parameters selected.

        :param pbp: pd.Dataframe, pbp data to filter.
        :param team: str (ex: 'SEA'), only includes plays from games that selected team played in.
        :param side_of_ball: str (must be 'offense' or 'defense'), only includes plays where selected team is on selected side of ball.
        :param play_type: str (must be 'passing', 'rushing', or 'receiving'), only includes plays of the selected type.
        :param weeks: optional list[int] (ex: [1, 2, 3, 4, 5]), only includes plays from selected weeks.
        :param pos: optional str (ex: 'QB' or 'WR'), only includes plays where passer/rusher/receiver (depending on stat_type) is of selected position.
        :param season: optional int (ex: 2024), season to use roster data for when filtering by pos, must be provided if pos is provided, ignored otherwise.
        :param pass_len: optional str (must be 'short' or 'deep'), only includes plays where pass was of selected length (ignored if play_type is not 'passing' or 'receiving').
        :param pass_loc: optional str (must be 'left', 'middle', or 'right'), only includes plays where pass was to selected location (ignored if play_type is not 'passing' or 'receiving').
        :param rush_loc: optional str (must be 'left', 'middle', or 'right'), only includes plays where rush was to selected location (ignored if play_type is not 'rushing').
        :param rush_gap: optional str (must be 'end', 'tackle', or 'guard'), only includes plays where rush was to selected gap (ignored if play_type is not 'rushing' or if rush_location is 'middle').
        :return: pd.Dataframe containing the filtered pbp data.
        """

        # FILTER BY TEAM AND SIDE OF BALL
        team = team.upper()
        if team not in self.TEAMS:
            raise Exception(f'Invalid team selected ({team}). Must be valid NFL team abbreviation, such as "SEA" or "GB".')

        side_of_ball = side_of_ball.lower()
        if side_of_ball == 'offense':
            pbp = pbp[pbp['posteam'] == team]
        elif side_of_ball == 'defense':
            pbp = pbp[pbp['defteam'] == team]
        else:
            raise Exception(f'Invalid side of ball selected ({side_of_ball}). Must be "offense" or "defense".')

        # FILTER BY WEEKS (IF PROVIDED)
        if weeks is not None:
            if not isinstance(weeks, list) or not all(isinstance(week, int) for week in weeks) or len(weeks) <= 0:
                raise Exception(f'Invalid list of weeks provided ({weeks}). Must be list of integers.')
            pbp = pbp[pbp['week'] in weeks]

        # FILTER BY PLAY TYPE
        play_type = play_type.lower()
        if play_type in ['passing', 'receiving']:
            pbp = pbp[pbp['pass_attempt'] == 1]
            pbp = pbp[pbp['two_point_attempt'] == 0]
            pbp = pbp[pbp['sack'] == 0]

            # filter by pass length (if provided)
            if pass_len is not None:
                pass_len = pass_len.lower()
                if pass_len in ['short', 'deep']:
                    pbp = pbp[pbp['pass_length'] == pass_len]
                else:
                    raise Exception(f'Invalid pass length selected ({pass_len}). Must be "short" or "deep".')

            # filter by pass location (if provided)
            if pass_loc is not None:
                pass_loc = pass_loc.lower()
                if pass_loc in ['left', 'middle', 'right']:
                    pbp = pbp[pbp['pass_location'] == pass_loc]
                else:
                    raise Exception(f'Invalid pass location selected ({pass_loc}). Must be "left", "middle", or "right".')

        elif play_type == 'rushing':
            pbp = pbp[pbp['rush_attempt'] == 1]

            # filter by rush location (if provided)
            if rush_loc is not None:
                rush_loc = rush_loc.lower()
                if rush_loc in ['left', 'middle', 'right']:
                    if rush_loc == 'middle': # ignore rush gap if rush is to middle
                        rush_gap = None
                    pbp = pbp[pbp['run_location'] == rush_loc]
                else:
                    raise Exception(f'Invalid rush location selected ({rush_loc}). Must be "left", "middle", or "right".')

            # filter by rush gap (if provided and rush location was not 'middle')
            if rush_gap is not None:
                rush_gap = rush_gap.lower()
                if rush_gap in ['end', 'tackle', 'guard']:
                    pbp = pbp[pbp['run_gap'] == rush_gap]
                else:
                    raise Exception(f'Invalid rush gap selected ({rush_gap}). Must be "end", "tackle", or "guard".')

        else:
            raise Exception(f'Invalid play type selected ({play_type}). Must be "passing", "rushing", or "receiving".')

        # FILTER BY POSITION (IF PROVIDED) OF PASSER/RUSHER/RECEIVER (DEPENDING ON PLAY TYPE)
        if pos is not None:
            pos = pos.upper()
            if season is None:
                raise Exception(f'Position parameter was provided without season parameter. Must pass a season to filter by position.')

            if play_type == 'passing':
                receiver_positions = []
                for player_id in pbp['receiver_player_id']:
                    receiver_positions.append(self._get_player_pos(player_id, season))
                pbp['receiver_player_pos'] = receiver_positions
                pbp = pbp[pbp['receiver_player_pos'] == pos]

            elif play_type == 'rushing':
                rusher_positions = []
                for player_id in pbp['rusher_player_id']:
                    rusher_positions.append(self._get_player_pos(player_id, season))
                pbp['rusher_player_pos'] = rusher_positions
                pbp = pbp[pbp['rusher_player_pos'] == pos]

        return pbp

    def filter_pbp_advanced(self, pbp, downs=None, yds_until_fd=None, goal_to_go=None, qtrs=None, qtr_time_left=None, pt_diff=None):
        """
        Filters the provided pbp data based on the parameters selected.

        :param pbp: pd.Dataframe, pbp data to filter.
        :param downs: optional list[int] (ex: [3, 4]), only includes plays of the selected downs.
        :param yds_until_fd: optional non-zero int (ex: 5, -4), if positive, only includes plays that need that many yds or more until a first down,
                                                                if negative, only includes plays that need that many yds or less until a first down.
        :param goal_to_go: optional bool, if True, only includes plays that are goal to go,
                                          if False, only includes plays that are not goal to go.
        :param qtrs: optional list[int] (ex: [1, 2, 3]), only includes plays that occurred during the selected quarters (note: 5 = overtime).
        :param qtr_time_left: optional int (ex: 120, -60), if positive, only includes plays with that many sec or more left in the quarter,
                                                           if negative, only includes plays with that many sec or less left in the quarter.
        :param pt_diff: optional len 2 list[int] (ex: [-7, 7]), only includes played that occurred when the point differential of the team with possession
                                                                is in the selected range (inclusive). Positive indicates team is winning and negative indicates
                                                                team is losing.
        :param temp: optional non-zero int (ex: 65, -50), if positive, only includes plays from games where the temperature was that many degrees or higher,
                                                 if negative, only includes plays from games where the temperature was that many degrees or lower.
        :param wind: optional non-zero int (ex: 10, -15), if positive, only includes plays from games where the wind was that many mph or higher,
                                                 if negative, only includes plays from games where the wind was that many mph or lower.
        :return: pd.Dataframe containing the filtered pbp data.
        """

        # FILTER BY DOWNS (IF PROVIDED)
        if downs is not None:
            if not isinstance(downs, list) or not all(isinstance(down, int) for down in downs) or len(downs) <= 0:
                raise Exception(f'Invalid list of downs provided ({downs}). Must be list of integers.')
            pbp = pbp[pbp['down'].isin(downs)]

        # FILTER BY QUARTERS (IF PROVIDED)
        if qtrs is not None:
            if not isinstance(qtrs, list) or not all(isinstance(qtr, int) for qtr in qtrs) or len(qtrs) <= 0:
                raise Exception(f'Invalid list of quarters provided ({qtrs}). Must be list of integers.')
            pbp = pbp[pbp['qtr'].isin(qtrs)]

        # FILTER BY GOAL TO GO (IF PROVIDED)
        if goal_to_go is not None:
            if not isinstance(goal_to_go, bool):
                raise Exception(f'Invalid goal to go value provided ({goal_to_go}). Must be boolean.')
            if goal_to_go:
                pbp = pbp[pbp['goal_to_go'] == 1]
            if not goal_to_go:
                pbp = pbp[pbp['goal_to_go'] == 0]

        # FILTER BY YDS REMAINING UNTIL FIRST DOWN (IF PROVIDED)
        if yds_until_fd is not None:
            if not isinstance(yds_until_fd, int) or yds_until_fd == 0:
                raise Exception(f'Invalid yards until first down provided ({yds_until_fd}). Must be non-zero integer.')
            if yds_until_fd > 0:
                pbp = pbp[pbp['ydstogo'] >= yds_until_fd]
            elif yds_until_fd < 0:
                pbp = pbp[pbp['ydstogo'] <= abs(yds_until_fd)]

        # FILTER BY TIME LEFT IN THE QUARTER (IF PROVIDED)
        if qtr_time_left is not None:
            if not isinstance(qtr_time_left, int) or qtr_time_left == 0:
                raise Exception(f'Invalid time left in quarter provided ({qtr_time_left}). Must be non-zero integer.')
            if qtr_time_left > 0:
                pbp = pbp[pbp['quarter_seconds_remaining'] >= qtr_time_left]
            elif qtr_time_left < 0:
                pbp = pbp[pbp['quarter_seconds_remaining'] <= abs(qtr_time_left)]

        # FILTER BY POINT DIFFERENTIAL OF TEAM WITH POSSESSION (IF PROVIDED)
        if pt_diff is not None:
            if not isinstance(pt_diff, list) or len(pt_diff) != 2 or not all(isinstance(pts, int) for pts in pt_diff):
                raise Exception(f'Invalid point differential provided ({pt_diff}). Must be list of integers with exactly 2 elements.')
            pbp = pbp[(pbp['posteam_score'] - pbp['defteam_score']).isin(range(pt_diff[0], pt_diff[1] + 1))]

        return pbp

    def calc_pass_stats(self, pbp):
        """
        Calculates total attempts, completions, pass yds, pass tds, interceptions, yards per attempt, and yards per
        completion that occur in the provided plays.

        :param pbp: pd.Dataframe, play-by-play data to calculate stats based on.
        :return: dict where key = stat ('att', 'cmp', 'pass_yd', 'pass_td', 'int' 'ypatt', 'ypcmp') and value = value of the stat.
        """

        # sum up basic passing stats
        stats = {
            'att': int(pbp['pass_attempt'].sum()),
            'cmp': int(pbp['complete_pass'].sum()),
            'pass_yd': int(pbp['passing_yards'].sum()),
            'pass_td': int(pbp['pass_touchdown'].sum()),
            'int': int(pbp['interception'].sum()),
        }

        # calculate yards per attempt
        if stats['att'] == 0:
            stats['ypatt'] = 0
        else:
            stats['ypatt'] = round(stats['pass_yd'] / stats['att'], 2)

        # calculate yards per completion
        if stats['cmp'] == 0:
            stats['ypcmp'] = 0
        else:
            stats['ypcmp'] = round(stats['pass_yd'] / stats['cmp'], 2)

        return stats

    def calc_rush_stats(self, pbp):
        """
        Calculates total carries, rush yds, rush tds, and yards per carry that occur in the provided plays.

        :param pbp: pd.Dataframe, play-by-play data to calculate stats based on.
        :return: dict where key = stat ('car', 'rush_yd', 'rush_td', 'ypcar') and value = value of the stat.
        """

        # sum up basic rushing stats
        stats = {
            'car': int(pbp['rush_attempt'].sum()),
            'rush_yd': int(pbp['rushing_yards'].sum()),
            'rush_td': int(pbp['rush_touchdown'].sum()),
        }

        # calculate yards per carry
        if stats['car'] == 0:
            stats['ypcar'] = 0
        else:
            stats['ypcar'] = round(stats['rush_yd'] / stats['car'], 2)

        return stats