from model import Model
from view import View
from controller import Controller
from PyQt6.QtWidgets import QApplication
import sys


def main():
    app = QApplication(sys.argv)
    view = View()
    model = Model()
    controller = Controller(model, view)
    controller.connectSignals()
    app.exec()


if __name__ == '__main__':
    main()
