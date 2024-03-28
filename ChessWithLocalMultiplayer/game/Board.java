package game;

import base.Vector2;
import base.Window;
import client.game.PlayingUI;

import java.util.List;
import java.util.Vector;


/**
 * Board object that loads and represents the games board.
 *
 * @author Kareem Khalidi
 */
public class Board extends Entity {

    private transient List<Vector2> highlights;

    /**
     * Constructor for a board.
     *
     * @author Kareem Khalidi
     */
    public Board() {
        super(-1, -1, PlayingUI.CANVAS_DIMENSIONS, PlayingUI.CANVAS_DIMENSIONS, -1, -1);
        this.highlights = new Vector<>(20);
    }

    /**
     * Highlights the squares on the board at the given position
     *
     * @param squares the squares to be highlighted
     * @author Kareem Khalidi
     */
    public void highlightSquare(final List<Vector2> squares) {
        this.highlights = squares;
    }

    /**
     * Overridden draw method for a map that draws the board.
     *
     * @param window the window to draw the map in.
     * @author Kareem Khalidi
     */
    @Override
    public void draw(final Window window) {
        //draw the board itself
        boolean curColor = false;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (curColor) {
                    window.drawRectangle((PlayingUI.CANVAS_DIMENSIONS / 8) * i, (PlayingUI.CANVAS_DIMENSIONS / 8) * j,
                            (PlayingUI.CANVAS_DIMENSIONS / 8), (PlayingUI.CANVAS_DIMENSIONS / 8),
                            Window.COLOR_GRAY, Window.COLOR_GRAY, 255);
                    curColor = false;
                } else {
                    window.drawRectangle((PlayingUI.CANVAS_DIMENSIONS / 8) * i, (PlayingUI.CANVAS_DIMENSIONS / 8) * j,
                            (PlayingUI.CANVAS_DIMENSIONS / 8), (PlayingUI.CANVAS_DIMENSIONS / 8),
                            Window.COLOR_WHITE, Window.COLOR_WHITE, 255);
                    curColor = true;
                }
                final var boardRow = -(i) + 7;
                if (this.highlights != null && this.highlights.contains(new Vector2(j, boardRow))) {
                    window.drawRectangle((PlayingUI.CANVAS_DIMENSIONS / 8) * i, (PlayingUI.CANVAS_DIMENSIONS / 8) * j,
                            (PlayingUI.CANVAS_DIMENSIONS / 8), (PlayingUI.CANVAS_DIMENSIONS / 8),
                            Window.COLOR_RED, Window.COLOR_RED, 255);

                }
            }
            curColor = !curColor;
        }
    }

}