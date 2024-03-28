package game.pieces;

import base.CastlingSide;
import base.Vector2;
import base.Window;
import server.game.GameState;

import java.util.ArrayList;
import java.util.Optional;

/**
 * Extends Piece, simulates a chess King
 *
 * @author Logan Sandlin
 * @author Alexander Cooper
 */
public final class King extends Piece {

    /**
     * Constructor for King
     */
    private King(final int boardRow, final int boardColumn, final ChessColor chessColor, final int row,
	    final int column) {
	super(boardRow, boardColumn, chessColor, row, column, 'k');
    }

    /**
     * Used to make a Chess Piece without worrying about UI stuff
     *
     * @param boardRow    the board row
     * @param boardColumn the board column
     * @param chessColor  the piece color
     * @return new King
     * @author Logan Sandlin
     * @author John Gauci
     */
    public static King newKing(final int boardRow, final int boardColumn, final ChessColor chessColor) {
        return new King(boardRow, boardColumn, chessColor, boardRow, boardColumn);
    }

    /**
     * Moves a king.
     *
     * @param gameState   the game's state.
     * @param destination the king's destination.
     * @author Logan Sandlin
     */
    @Override
    public void move(final GameState gameState, final Vector2 destination) {
        final Optional<Piece> possibleMovingPiece = gameState.getPiece(this.getPosition());
        if (possibleMovingPiece.isPresent()) {
            final Piece movingPiece = possibleMovingPiece.get();
            final Optional<Piece> possibleCapture = gameState.getPiece(destination);
            if (possibleCapture.isPresent()) {
                final Piece capture = possibleCapture.get();
                gameState.removeActivePiece(capture);
                gameState.resetHalfMoves();
            } else if (Math.abs(destination.x() - movingPiece.getBoardColumn()) == 2) {
                final int col = destination.x() < movingPiece.getBoardColumn() ? 0 : 7;
                final var possibleRook = gameState.getPiece(new Vector2(col, movingPiece.getBoardRow()));
                if (possibleRook.isPresent() && possibleRook.get() instanceof final Rook rook) {
                    if (col == 0)
                        rook.setBoardColumn(3);
                    else
                        rook.setBoardColumn(5);
                    rook.updateCanvasRowColumn();
                }
                gameState.incrementHalfMoves();
            } else {
                gameState.incrementHalfMoves();
            }
            if (!this.hasMoved()) {
                gameState.revokeCastlingRights(CastlingSide.KING);
                gameState.revokeCastlingRights(CastlingSide.QUEEN);
                this.setMoved();
            }
            movingPiece.setBoardColumn(destination.x());
            movingPiece.setBoardRow(destination.y());
        }
        this.updateCanvasRowColumn();
    }

    /**
     * Returns the potential moves for a King
     *
     * @param gameState the current game state
     * @return potential moves
     * @author Logan Sandlin
     * @author Alexander Cooper
     */
    public ArrayList<Vector2> getPotentialMoves(final GameState gameState) {
        final ArrayList<Vector2> moves = this.getRegularMoves(gameState);
        for (final var right : gameState.getCastlingRights()) {
            if (!gameState.positionInCheck(this.getPosition())) {
                final int dir = right == CastlingSide.KING ? 1 : -1;
                boolean canCastle = true;
                for (int x = this.getBoardColumn() + dir; x >= 1 && x < 7; x += dir) {
                    final var possibleCollision = new Vector2(x, this.getBoardRow());
                    if (gameState.getPiece(possibleCollision).isPresent() || gameState.positionInCheck(possibleCollision)) {
                        canCastle = false;
                        break;
                    }
                }
                if (canCastle)
                    moves.add(new Vector2(this.getBoardColumn() + dir * 2, this.getBoardRow()));
            }
        }

        return moves;
    }

    /**
     * Gets regular moves for the king.
     *
     * @param gameState the state of the game.
     * @return the king's regular moves.
     * @author Logan Sandlin
     * @author Alexander Cooper
     */
    private ArrayList<Vector2> getRegularMoves(final GameState gameState) {
        final ArrayList<Vector2> moves = new ArrayList<>(8);
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x == 0 && y == 0)
                    continue;
                final Vector2 possibleMove = new Vector2(this.getBoardColumn() + x, this.getBoardRow() + y);
                if (Piece.isInRange(possibleMove) && !gameState.positionInCheck(possibleMove)) {
                    final var possibleCollision = gameState.getPiece(possibleMove);
                    if (possibleCollision.isEmpty() || possibleCollision.get().getChessColor() != this.getChessColor())
                        moves.add(possibleMove);
                }
            }
        }
        return moves;
    }

    /**
     * Gets the kings potential threats. This method has to be separate because calling getPotentialMoves
     * requires calling positionInCheck, which calls getPotentialMoves creating an infinite loop.
     *
     * @param gameState the state of the game.
     * @return the king's potential threats.
     * @author Alexander Cooper
     */
    @Override
    public ArrayList<Vector2> getPotentialThreats(final GameState gameState) {
        final ArrayList<Vector2> moves = new ArrayList<>(8);
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if (x == 0 && y == 0)
                    continue;
                final Vector2 possibleMove = new Vector2(this.getBoardColumn() + x, this.getBoardRow() + y);
                if (Piece.isInRange(possibleMove)) {
                    final var possibleCollision = gameState.getPiece(possibleMove);
                    if (possibleCollision.isEmpty() || possibleCollision.get().getChessColor() != this.getChessColor())
                        moves.add(possibleMove);
                }
            }
        }
        return moves;
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
                    this.getHeight(), 5);
            case BLACK -> window.drawPicture(this.getRow(), this.getColumn(), this.getWidth(),
                    this.getHeight(), 11);
        }
    }
}
