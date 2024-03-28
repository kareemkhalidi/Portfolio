from PyQt6.QtCore import Qt
from PyQt6.QtWidgets import *

import matplotlib
matplotlib.use('QtAgg')
from matplotlib.backends.backend_qtagg import FigureCanvasQTAgg
from matplotlib.figure import Figure


class MPLCanvas(FigureCanvasQTAgg):
    def __init__(self, width=5, height=5, dpi=100):
        self.fig = Figure(figsize=(width, height), dpi=dpi)
        self.axes = self.fig.add_subplot(111)
        super().__init__(self.fig)


class View(QWidget):

    def __init__(self):
        super().__init__()
        # VARIABLES
        # layout/widget variables
        self.layout = QFormLayout()
        self.player_name_cb = QComboBox()
        self.amount_le = QLineEdit()
        self.stat_cb = QComboBox()
        self.enter_btn = QPushButton("Enter")
        self.stats_tbl = QTableWidget()
        self.mpl_canvas = MPLCanvas()
        # SET UP GUI
        self.setUpGUI()

    def setUpGUI(self):
        # SET UP LAYOUT
        # add player name combo box
        label = QLabel("Player:")
        label.setAlignment(Qt.AlignmentFlag.AlignRight)
        self.player_name_cb.setEditable(True)
        self.player_name_cb.setInsertPolicy(QComboBox.InsertPolicy.NoInsert)
        self.player_name_cb.completer().setCompletionMode(QCompleter.CompletionMode.PopupCompletion)
        self.layout.addRow(label, self.player_name_cb)
        # add amount line edit
        label = QLabel("Amount:")
        label.setAlignment(Qt.AlignmentFlag.AlignRight)
        self.layout.addRow(label, self.amount_le)
        # add stat combo box
        label = QLabel("Stat:")
        label.setAlignment(Qt.AlignmentFlag.AlignRight)
        self.layout.addRow(label, self.stat_cb)
        # add enter button
        self.layout.addRow(self.enter_btn)
        # add stats table
        self.stats_tbl.setColumnCount(3)
        self.stats_tbl.setRowCount(0)
        self.stats_tbl.setEditTriggers(QAbstractItemView.EditTrigger.NoEditTriggers)
        self.stats_tbl.setHorizontalHeaderLabels(["GAMES", "PERCENT", "COUNT"])
        # make all 3 columns stretch to fill the table
        self.stats_tbl.horizontalHeader().setSectionResizeMode(QHeaderView.ResizeMode.Stretch)
        # stretch vertically to fit header and 6 rows
        self.stats_tbl.verticalHeader().setSectionResizeMode(QHeaderView.ResizeMode.Stretch)
        self.layout.addRow(self.stats_tbl)
        # add mpl canvas
        self.layout.addRow(self.mpl_canvas)
        # set layout
        self.setLayout(self.layout)
        # SET UP WINDOW
        self.setWindowTitle("NBA Data")
        self.setFixedSize(self.sizeHint())

    def add_row_to_stats_tbl(self, label, percent, count):
        self.stats_tbl.insertRow(self.stats_tbl.rowCount())
        self.stats_tbl.setItem(self.stats_tbl.rowCount() - 1, 0, QTableWidgetItem(label))
        self.stats_tbl.setItem(self.stats_tbl.rowCount() - 1, 1, QTableWidgetItem(percent))
        self.stats_tbl.setItem(self.stats_tbl.rowCount() - 1, 2, QTableWidgetItem(count))

    def update_mpl_canvas(self, last_25, amount, stat):
        self.mpl_canvas.axes.cla()
        self.mpl_canvas.axes.bar(last_25['date'], last_25[stat])
        for i in range(len(last_25)):
            self.mpl_canvas.axes.text(i, last_25[stat][i], last_25[stat][i], ha='center', va='bottom')
            if last_25[stat][i] >= int(amount):
                self.mpl_canvas.axes.bar(i, last_25[stat][i], color='g')
            else:
                self.mpl_canvas.axes.bar(i, last_25[stat][i], color='r')
        self.mpl_canvas.axes.axhline(y=int(amount), color='g', linestyle='dashed')
        self.mpl_canvas.axes.tick_params(axis='x', labelrotation=90)
        self.mpl_canvas.fig.tight_layout()
        self.mpl_canvas.draw()
