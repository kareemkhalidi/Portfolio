package game.pieces;

import base.Vector2;
import base.Window;
import server.game.GameState;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Extends Piece, simulates a chess Bishop
 *
 * @author Logan Sandlin
 */
public final class Bishop extends Piece {

    /**
     * Constructor for Bishop
     */
    private Bishop(final int boardRow, final int boardColumn, final ChessColor chessColor, final int row, final int column) {
        super(boardRow, boardColumn, chessColor, row, column, 'b');
    }

    /**
     * Used to make a Chess Piece without worrying about UI stuff
     *
     * @param boardRow    the board row
     * @param boardColumn the board column
     * @param chessColor  the piece color
     * @return new Bishop
     * @author Logan Sandlin
     * @author John Gauci
     */
    public static Bishop newBishop(final int boardRow, final int boardColumn, final ChessColor chessColor) {
        return new Bishop(boardRow, boardColumn, chessColor, boardRow, boardColumn);
    }

    /**
     * Returns the potential moves for a Bishop
     *
     * @param gameState the current game state
     * @return potential moves
     * @author Logan Sandlin
     */
    public ArrayList<Vector2> getPotentialMoves(final GameState gameState) {
        final ArrayList<Vector2> potentialMoves = new ArrayList<>(8);
        final int col = this.getBoardColumn();
        final int row = this.getBoardRow();
        final Queue<Vector2> possibleMoves = new LinkedList<>();
        this.getIntrinsicMoves(col + 1, row + 1, 1, 1, possibleMoves);
        this.pruneCollisions(gameState, possibleMoves, potentialMoves);
        this.getIntrinsicMoves(col - 1, row + 1, -1, 1, possibleMoves);
        this.pruneCollisions(gameState, possibleMoves, potentialMoves);
        this.getIntrinsicMoves(col + 1, row - 1, 1, -1, possibleMoves);
        this.pruneCollisions(gameState, possibleMoves, potentialMoves);
        this.getIntrinsicMoves(col - 1, row - 1, -1, -1, possibleMoves);
        this.pruneCollisions(gameState, possibleMoves, potentialMoves);

        return potentialMoves;
    }

    /**
     * Draws the chess piece in the UI
     *
     * @param window the window to draw the entity in
     * @author Kareem Khalidi
     */
    @Override
    public void draw(final Window window) {
        switch (this.getChessColor()) {
            case WHITE -> window.drawPicture(this.getRow(), this.getColumn(), this.getWidth(),
                    this.getHeight(), 2);
            case BLACK -> window.drawPicture(this.getRow(), this.getColumn(), this.getWidth(),
                    this.getHeight(), 8);
        }
    }
}
