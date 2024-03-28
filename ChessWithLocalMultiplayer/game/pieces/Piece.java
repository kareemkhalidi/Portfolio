package game.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import base.Vector2;
import client.game.PlayingUI;
import game.Entity;
import server.game.GameState;

import java.util.*;

/**
 * Base class for chess pieces
 *
 * @author Logan Sandlin
 */
public abstract class Piece extends Entity {

    private static final int PIECE_WIDTH = 80;
    private static final int PIECE_HEIGHT = 80;
    private final ChessColor chessColor;
    private final char pieceLetter;
    private int boardRow;
    private int boardColumn;
    private boolean moved;

    /**
     * Piece Constructor.
     *
     * @param chessColor Entity object constructor.
     * @author Logan Sandlin
     */
    protected Piece(final int boardRow, final int boardColumn, final ChessColor chessColor, final int row,
	    final int column, final char pieceLetter) {
	super(row, column, Piece.PIECE_WIDTH, Piece.PIECE_HEIGHT, -1, chessColor.ordinal() + 1);
	this.boardRow = boardRow;
	this.boardColumn = boardColumn;
	this.chessColor = chessColor;
	this.pieceLetter = pieceLetter;
	this.moved = false;

        this.updateCanvasRowColumn();
    }

    /**
     * Determines if a point is in range of the board.
     *
     * @param point the point to check if in range.
     * @return a value determining if the point is in range.
     * @author Alexander Cooper
     */
    static boolean isInRange(final Vector2 point) {
	    return (point.x() >= 0 && point.x() < 8) && (point.y() >= 0 && point.y() < 8);
    }

    /**
     * Returns the chess color of the piece
     *
     * @return piece color
     * @author John Gauci
     */
    public ChessColor getChessColor() {
        return this.chessColor;
    }

    /**
     * Returns whether a piece has moved
     *
     * @return whether a piece has moved
     * @author Alexander Cooper
     */
    boolean hasMoved() {
        return this.moved;
    }

    /**
     * Sets a piece as moved
     *
     * @author Alexander Cooper
     */
    public void setMoved() {
        this.moved = true;
    }

    /**
     * Handles captures and updates the piece movement
     *
     * @param gameState   the current game state
     * @param destination the destination position of the piece
     * @author Logan Sandlin
     */
    public void move(final GameState gameState, final Vector2 destination) {
        final Optional<Piece> possibleMovingPiece = gameState.getPiece(new Vector2(this.boardColumn, this.boardRow));
        if (possibleMovingPiece.isPresent()) {
            final Piece movingPiece = possibleMovingPiece.get();
            final Optional<Piece> possibleCapture = gameState.getPiece(destination);
            if (possibleCapture.isPresent()) {
                final Piece capture = possibleCapture.get();
                gameState.removeActivePiece(capture);
                gameState.resetHalfMoves();
            } else {
                gameState.incrementHalfMoves();
            }
            movingPiece.boardColumn = destination.x();
            movingPiece.boardRow = destination.y();
            if (!this.hasMoved())
                this.setMoved();
        }
        this.updateCanvasRowColumn();
    }

    /**
     * Simulates a move without permanently modifying the board, that way it can be seen if the king is put in check
     *
     * @param gameState
     * @param destination
     * @return an optional simulated captured piece
     * @author Logan Sandlin
     */
    private Optional<Piece> simulateMove(final GameState gameState, final Vector2 destination) {
        final Optional<Piece> possibleMovingPiece = gameState.getPiece(new Vector2(this.boardColumn, this.boardRow));
        if (possibleMovingPiece.isPresent()) {
            final Piece movingPiece = possibleMovingPiece.get();
            final Optional<Piece> possibleCapture = gameState.getPiece(destination);
            Piece capture = null;
            if (possibleCapture.isPresent()) {
                capture = possibleCapture.get();
                gameState.removeActivePiece(capture);
            }
            movingPiece.boardColumn = destination.x();
            movingPiece.boardRow = destination.y();
            return Optional.ofNullable(capture);
        }
        return Optional.empty();
    }


    /**
     * Helper for the different potential move functions
     *
     * @param col
     * @param row
     * @param xDirection
     * @param yDirection
     * @param intrinsicMoves
     * @author Logan Sandlin
     */
    void getIntrinsicMoves(final int col, final int row, final int xDirection, final int yDirection,
                           final Collection<Vector2> intrinsicMoves) {
        int destinationRow = row;
        int destinationColumn = col;
        while (destinationColumn < 8 && destinationRow < 8 && destinationColumn >= 0 && destinationRow >= 0) {
            intrinsicMoves.add(new Vector2(destinationColumn, destinationRow));
            destinationColumn += xDirection;
            destinationRow += yDirection;
        }
    }

    /**
     * Takes all the possible moves without considering the game state and prunes the
     * inaccessible ones
     *
     * @param gameState
     * @param intrinsicMoves
     * @param noColns
     * @author Logan Sandlin
     */
    void pruneCollisions(final GameState gameState, final Queue<Vector2> intrinsicMoves, final Collection<Vector2> noColns) {
        while (!intrinsicMoves.isEmpty() && gameState.getPiece(intrinsicMoves.peek()).isEmpty()) {
            noColns.add(intrinsicMoves.remove());
        }
        if (!intrinsicMoves.isEmpty()) {
            if (gameState.getPiece(intrinsicMoves.peek()).get().chessColor == this.chessColor) {
                intrinsicMoves.remove();
            } else {
                noColns.add(intrinsicMoves.remove());
            }
        }
        intrinsicMoves.clear();
    }

    /**
     * Gets the potential moves for a piece
     *
     * @param gameState the current game state
     * @return potential moves
     * @author Logan Sandlin
     */
    public abstract List<Vector2> getPotentialMoves(GameState gameState);

    /**
     * Used to get a pieces potential threats. Most of the time this method can just return the pieces
     * potential moves, but sometimes, as in the case of castling, a move does not represent a threat,
     * but can cause endless recursion and should be left out of the potential threats.
     *
     * @param gameState the state of the game.
     * @return the potential threats.
     * @author Alexander Cooper
     */
    public List<Vector2> getPotentialThreats(final GameState gameState) {
        return this.getPotentialMoves(gameState);
    }

    /**
     * Returns the board row
     *
     * @return the board row
     * @author John Gauci
     */
    public int getBoardRow() {
        return this.boardRow;
    }

    /**
     * Sets the board row
     *
     * @param boardRow the board row to be set to
     * @author John Gauci
     */
    void setBoardRow(final int boardRow) {
        this.boardRow = boardRow;
    }

    /**
     * Returns the board column
     *
     * @return the board column
     * @author John Gauci
     */
    public int getBoardColumn() {
        return this.boardColumn;
    }

    /**
     * Sets the board column
     *
     * @param boardColumn the board column to be set to
     * @author John Gauci
     */
    void setBoardColumn(final int boardColumn) {
        this.boardColumn = boardColumn;
    }

    /**
     * Gets the position of the piece as a Vector2
     *
     * @return Vector2 piece position
     * @author John Gauci
     */
    public Vector2 getPosition() {
        return new Vector2(this.boardColumn, this.boardRow);
    }

    final void updateCanvasRowColumn() {
        this.setRow((PlayingUI.CANVAS_DIMENSIONS / 8) * (-(this.boardRow - 7)) + 10);
        this.setColumn((PlayingUI.CANVAS_DIMENSIONS / 8) * this.boardColumn + 10);
    }

    /**
     * Returns the valid moves for a piece
     *
     * @param gameState the current game state
     * @return valid moves
     * @author Logan Sandlin
     */
    public List<Vector2> getValidMoves(final GameState gameState) {
        final Iterable<Vector2> potentialMoves = this.getPotentialMoves(gameState);
        final List<Vector2> validMoves = new ArrayList<>(8);
        for (final Vector2 move : potentialMoves) {
            final Vector2 start = new Vector2(this.boardColumn, this.boardRow);
            final var possibleCapture = this.simulateMove(gameState, move);
            if (!gameState.isKingInCheck())
                validMoves.add(move);
            this.simulateMove(gameState, start);
            possibleCapture.ifPresent(gameState::addActivePiece);
        }
        return validMoves;
    }

    /**
     * Returns the piece letter
     *
     * @return piece letter
     * @author John Gauci
     */
    public char getPieceLetter() {
        return (switch (this.chessColor) {
            case WHITE -> Character.toUpperCase(this.pieceLetter);
            case BLACK -> Character.toLowerCase(this.pieceLetter);
        });
    }

    /**
     * Returns whether a piece is equal
     *
     * @param o the piece to compare this entity to.
     * @return whether the pieces are equal
     * @author John Gauci (IDE generated)
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Piece piece)) return false;
        if (!super.equals(o)) return false;
        if (this.boardRow != piece.boardRow) return false;
        if (this.boardColumn != piece.boardColumn) return false;
        return this.chessColor == piece.chessColor;
    }

    /**
     * Returns the hashcode for a piece
     *
     * @return hashcode for a piece
     * @author John Gauci (IDE generated)
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + this.boardRow;
        result = 31 * result + this.boardColumn;
        result = 31 * result + this.chessColor.hashCode();
        return result;
    }

}
