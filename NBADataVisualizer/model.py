import pandasql as pdsql
import nba_api.stats.endpoints.playergamelog as pgl
import nba_api.stats.static.players as players


def get_player_id(player_name):
    # get dict of all players
    player_dict = players.get_players()
    # find player id for player_name
    player = [player for player in player_dict if player['full_name'] == player_name][0]
    return player['id']


def get_player_name_list():
    # get dict of all players
    player_dict = players.get_players()
    # get list of player names
    player_name_list = [player['full_name'] for player in player_dict]
    return player_name_list


def get_player_game_log(player_id):
    # get game log for player_name
    game_log = pgl.PlayerGameLog(player_id).get_data_frames()[0]
    # select player combo columns
    combos_log = pdsql.sqldf("SELECT GAME_DATE AS date, SUM(PTS + REB) AS pointsrebounds, "
                             "SUM(PTS + AST) AS pointsassists, SUM(REB + AST) AS reboundsassists, "
                             "SUM(PTS + REB + AST) AS pointsreboundsassists "
                             "FROM game_log "
                             "GROUP BY GAME_DATE", locals())
    # return game log as pandas dataframe with only relevant and renamed columns
    return pdsql.sqldf("SELECT game_log.GAME_DATE AS date, game_log.MATCHUP AS matchup, game_log.WL AS result, "
                       "game_log.MIN AS minutes, game_log.PTS AS points, game_log.REB AS rebounds, game_log.AST AS assists, "
                       "game_log.STL AS steals, game_log.BLK as blocks, combos_log.pointsrebounds, combos_log.pointsassists, "
                       "combos_log.reboundsassists, combos_log.pointsreboundsassists "
                       "FROM game_log "
                       "INNER JOIN combos_log ON game_log.GAME_DATE=combos_log.date", locals())


# gets how often player has gone over amount in stat (ex: over 10 points)
def get_chance_of_hitting_stat(game_log, stat, amount):
    # calculate how often player has gone over amount in stat
    over_count = pdsql.sqldf("SELECT COUNT(1) AS over_count "
                             "FROM game_log "
                             "WHERE " + stat + " >= " + amount, locals())
    # calculate total games played
    total_games = pdsql.sqldf("SELECT COUNT(1) AS total_games "
                              "FROM game_log", locals())
    return over_count['over_count'][0], total_games['total_games'][0]


# gets how often player has gone over amount in stat for last x games
def get_chance_of_hitting_stat_last_x(game_log, stat, amount, lastXGames):
    # get last x games from game log
    game_log_last_x = game_log.head(lastXGames)
    # calculate how often player has gone over amount in stat
    over_count = pdsql.sqldf("SELECT COUNT(1) AS over_count "
                             "FROM game_log_last_x "
                             "WHERE " + stat + " >= " + amount, locals())
    return over_count['over_count'][0]


class Model:
    def __init__(self):
        self.player_name = None
        self.player_id = None
        self.amount = None
        self.stat = None
        self.game_log = None
        self.stats = ['points', 'rebounds', 'assists', 'steals', 'blocks', 'points + rebounds', 'points + assists',
                        'rebounds + assists', 'points + rebounds + assists']

    def set_player(self, player_name):
        self.player_name = player_name
        self.player_id = get_player_id(player_name)
        self.game_log = get_player_game_log(self.player_id)

    def set_amount(self, amount):
        self.amount = amount

    def set_stat(self, stat):
        self.stat = stat.replace(' + ', '')

    def get_player_game_log(self):
        return self.game_log

    def get_chance_of_hitting_stat(self):
        return get_chance_of_hitting_stat(self.game_log, self.stat, self.amount)

    def get_chance_of_hitting_stat_last_x(self, lastXGames):
        return get_chance_of_hitting_stat_last_x(self.game_log, self.stat, self.amount, lastXGames)

    def get_last_25_games(self):
        return self.game_log.head(25)
