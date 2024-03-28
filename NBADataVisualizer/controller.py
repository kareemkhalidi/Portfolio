from model import get_player_name_list


class Controller:
    def __init__(self, model, view):
        self.model = model
        self.view = view

    def connectSignals(self):
        self.view.player_name_cb.addItems(get_player_name_list())
        self.view.stat_cb.addItems(self.model.stats)
        self.view.enter_btn.clicked.connect(self.calculate_and_display_chance_of_hitting_amount_of_stat)
        self.view.show()

    def calculate_and_display_chance_of_hitting_amount_of_stat(self):
        # set model values
        self.model.set_player(self.view.player_name_cb.currentText())
        self.model.set_amount(self.view.amount_le.text())
        self.model.set_stat(self.view.stat_cb.currentText())
        # update stats table
        self.update_stats_table()
        # draw mpl canvas
        self.view.update_mpl_canvas(self.model.get_last_25_games(), self.model.amount, self.model.stat)

    def update_stats_table(self):
        # clear stats table
        self.view.stats_tbl.setRowCount(0)
        # for last [5, 10, 15, 20, 25] games, add row to stats table
        for i in range(5, 30, 5):
            over_count = self.model.get_chance_of_hitting_stat_last_x(i)
            self.view.add_row_to_stats_tbl("Last " + str(i) + " Games", str(over_count / i * 100) + "%",
                                           str(over_count) + "/" + str(i))
        # for season, add row to stats table
        over_count, total_games = self.model.get_chance_of_hitting_stat()
        self.view.add_row_to_stats_tbl("Season", str(over_count / total_games * 100) + "%",
                                       str(over_count) + "/" + str(total_games))
