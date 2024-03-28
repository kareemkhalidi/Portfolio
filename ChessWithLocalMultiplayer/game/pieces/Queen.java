package game.pieces;

import base.Vector2;
import base.Window;
import server.game.GameState;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Extends Piece, simulates a chess Queen
 *
 * @author Logan Sandlin
 */
public final class Queen extends Piece {

    /**
     * Constructor for Queen
     */
    private Queen(final int boardRow, final int boardColumn, final ChessColor chessColor, final int row, final int column) {
        super(boardRow, boardColumn, chessColor, row, column, 'q');
    }

    /**
     * Used to make a Chess Piece without worrying about UI stuff
     *
     * @param boardRow    the board row
     * @param boardColumn the board column
     * @param chessColor  the piece color
     * @return new Queen
     * @author Logan Sandlin
     * @author John Gauci
     */
    public static Queen newQueen(final int boardRow, final int boardColumn, final ChessColor chessColor) {
        return new Queen(boardRow, boardColumn, chessColor, boardRow, boardColumn);
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
        final Queue<Vector2> intrinsicMoves = new LinkedList<>();
        this.getIntrinsicMoves(col, row + 1, 0, 1, intrinsicMoves);
        this.pruneCollisions(gameState, intrinsicMoves, potentialMoves);
        this.getIntrinsicMoves(col - 1, row, -1, 0, intrinsicMoves);
        this.pruneCollisions(gameState, intrinsicMoves, potentialMoves);
        this.getIntrinsicMoves(col, row - 1, 0, -1, intrinsicMoves);
        this.pruneCollisions(gameState, intrinsicMoves, potentialMoves);
        this.getIntrinsicMoves(col + 1, row, 1, 0, intrinsicMoves);
        this.pruneCollisions(gameState, intrinsicMoves, potentialMoves);
        this.getIntrinsicMoves(col + 1, row + 1, 1, 1, intrinsicMoves);
        this.pruneCollisions(gameState, intrinsicMoves, potentialMoves);
        this.getIntrinsicMoves(col - 1, row + 1, -1, 1, intrinsicMoves);
        this.pruneCollisions(gameState, intrinsicMoves, potentialMoves);
        this.getIntrinsicMoves(col + 1, row - 1, 1, -1, intrinsicMoves);
        this.pruneCollisions(gameState, intrinsicMoves, potentialMoves);
        this.getIntrinsicMoves(col - 1, row - 1, -1, -1, intrinsicMoves);
        this.pruneCollisions(gameState, intrinsicMoves, potentialMoves);

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
                    this.getHeight(), 4);
            case BLACK -> window.drawPicture(this.getRow(), this.getColumn(), this.getWidth(),
                    this.getHeight(), 10);
        }
    }
}
