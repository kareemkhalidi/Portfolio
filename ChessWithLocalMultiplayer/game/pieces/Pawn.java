package game.pieces;

import base.Vector2;
import base.Window;
import server.game.GameState;

import java.util.*;

/**
 * Extends Piece, simulates a chess Pawn
 *
 * @author Logan Sandlin
 * @author Alexander Cooper
 */
public final class Pawn extends Piece {

    private boolean atTwoSpace;

    /**
     * Constructor for Pawn
     */
    private Pawn(final int boardRow, final int boardColumn, final ChessColor chessColor, final int row, final int column) {
        super(boardRow, boardColumn, chessColor, row, column, 'p');
        this.atTwoSpace = false;
    }

    /**
     * Used to make a Chess Piece without worrying about UI stuff
     *
     * @param boardRow    the board row
     * @param boardColumn the board column
     * @param chessColor  the piece color
     * @return new Pawn
     * @author Logan Sandlin
     * @author John Gauci
     */
    public static Pawn newPawn(final int boardRow, final int boardColumn, final ChessColor chessColor) {
        return new Pawn(boardRow, boardColumn, chessColor, boardRow, boardColumn);
    }

    @Override
    public ArrayList<Vector2> getPotentialThreats(final GameState gameState) {
        final Iterable<Vector2> potentialMoves = this.getPotentialMoves(gameState);
        final ArrayList<Vector2> threats = new ArrayList<>(8);
        final int col = this.getBoardColumn();
        for (final Vector2 move : potentialMoves) {
            if (move.x() != col) {
                threats.add(move);
            }
        }
        return threats;
    }

    /**
     * Moves a pawn
     *
     * @param gameState   the current game state
     * @param destination the destination position of the piece
     * @author Logan Sandlin
     */
    @Override
    public void move(final GameState gameState, final Vector2 destination) {
        final Optional<Piece> possibleMovingPiece = gameState.getPiece(new Vector2(this.getBoardColumn(),
                this.getBoardRow()));
        if (possibleMovingPiece.isPresent() && possibleMovingPiece.get() instanceof final Pawn movingPiece) {
            final int dir = movingPiece.getChessColor() == ChessColor.WHITE ? 1 : -1;
            if (movingPiece.atTwoSpace) {
                this.atTwoSpace = false;
            } else if (!movingPiece.hasMoved()) {
                if (destination.y() == (movingPiece.getBoardRow() + 2 * dir)) {
                    movingPiece.atTwoSpace = true;
                    gameState.addEnPassantTarget(new Vector2(destination.x(), destination.y() - dir));
                } else {
                    this.atTwoSpace = false;
                }
                movingPiece.setMoved();
            } else {
                movingPiece.atTwoSpace = false;
            }
            final Optional<Piece> possibleCapture = gameState.getPiece(destination);
            if (possibleCapture.isPresent()) {
                final Piece capture = possibleCapture.get();
                gameState.removeActivePiece(capture);
                movingPiece.setBoardColumn(destination.x());
                movingPiece.setBoardRow(destination.y());
            } else if (gameState.getEnPassantTarget().isPresent() && gameState.getEnPassantTarget().get().equals(destination)) {
                final var possibleEnPassantCapture = gameState.getPiece(new Vector2(destination.x(), destination.y() - dir));
                if (possibleEnPassantCapture.isPresent() && possibleEnPassantCapture.get().getChessColor() != this.getChessColor() &&
                        possibleEnPassantCapture.get() instanceof final Pawn pawnTarget) {
                    gameState.removeActivePiece(pawnTarget);
                    gameState.removeEnPassantTarget();
                    movingPiece.setBoardColumn(destination.x());
                    movingPiece.setBoardRow(destination.y());
                }
            }
            if ((this.getChessColor() == ChessColor.WHITE) && destination.y() == 7 ||
                    (this.getChessColor() == ChessColor.BLACK) && destination.y() == 0) {
                final Queen newQueen = Queen.newQueen(destination.y(), destination.x(),
                        this.getChessColor());
                gameState.removeActivePiece(movingPiece);
                gameState.addActivePiece(newQueen); //is supposed to be any piece besides king
            } else {
                movingPiece.setBoardColumn(destination.x());
                movingPiece.setBoardRow(destination.y());
            }

        }
        gameState.resetHalfMoves();
        this.updateCanvasRowColumn();
    }

    /**
     * Returns the potential moves for a Bishop
     *
     * @param gameState the current game state
     * @return potential moves
     * @author Logan Sandlin
     */
    public List<Vector2> getPotentialMoves(final GameState gameState) {
        final int dir = this.getChessColor() == ChessColor.WHITE ? 1 : -1;

        final Queue<Vector2> possibleMoves = new LinkedList<>();
        final Vector2 possibleMove = new Vector2(this.getBoardColumn(), this.getBoardRow() + dir);
        if (Piece.isInRange(possibleMove)) {
            final var possibleCollision1 = gameState.getPiece(possibleMove);
            if (possibleCollision1.isEmpty()) {
                possibleMoves.add(possibleMove);
                if (!this.hasMoved()) {
                    final Vector2 possibleMove2 = new Vector2(this.getBoardColumn(), this.getBoardRow() + dir * 2);
                    final var possibleCollision2 = gameState.getPiece(possibleMove2);
                    if (possibleCollision2.isEmpty())
                        possibleMoves.add(new Vector2(this.getBoardColumn(), this.getBoardRow() + dir * 2));
                }
            }
        }
        final List<Vector2> potentialMoves = new ArrayList<>(8);
        this.pruneCollisions(gameState, possibleMoves, potentialMoves);

        for (int x = -1; x <= 1; x += 2) {
            final Vector2 potentialCapturePosition = new Vector2(this.getBoardColumn() + x, this.getBoardRow() + dir);
            if (Piece.isInRange(potentialCapturePosition)) {
                final Optional<Piece> potentialCapture = gameState.getPiece(potentialCapturePosition);
                if (potentialCapture.isPresent() && potentialCapture.get().getChessColor() != this.getChessColor()) {
                    possibleMoves.add(potentialCapturePosition);
                    this.pruneCollisions(gameState, possibleMoves, potentialMoves);
                } else if (gameState.getEnPassantTarget().isPresent()) { // Check for En Passant
                    final Vector2 target = gameState.getEnPassantTarget().get();
                    if (Math.abs(this.getBoardColumn() - target.x()) == 1 && this.getBoardRow() + dir == target.y()) {
                        final var possibleTarget = gameState.getPiece(new Vector2(target.x(), target.y() - dir));
                        if (possibleTarget.isPresent() && possibleTarget.get().getChessColor() != this.getChessColor() && possibleTarget.get() instanceof Pawn) {
                            potentialMoves.add(target);
                            this.pruneCollisions(gameState, possibleMoves, potentialMoves);
                        }
                    }
                }
            }
        }

        return potentialMoves;
    }

    public boolean isAtTwoSpace() {
        return this.atTwoSpace;
    }

    public void setAtTwoSpace(final boolean atTwoSpace) {
        this.atTwoSpace = atTwoSpace;
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
                    this.getHeight(), 0);
            case BLACK -> window.drawPicture(this.getRow(), this.getColumn(), this.getWidth(),
                    this.getHeight(), 6);
        }
    }
}
