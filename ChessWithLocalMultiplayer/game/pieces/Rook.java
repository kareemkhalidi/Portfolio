package game.pieces;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import base.CastlingSide;
import base.Vector2;
import base.Window;
import server.game.GameState;

/**
 * Extends Piece, simulates a chess Rook
 *
 * @author Logan Sandlin
 */
public final class Rook extends Piece {

    /**
     * Constructor for Rook
     */
    private Rook(final int boardRow, final int boardColumn, final ChessColor chessColor, final int row,
	    final int column) {
	super(boardRow, boardColumn, chessColor, row, column, 'r');
    }

    /**
     * Used to make a Chess Piece without worrying about UI stuff
     *
     * @param boardRow    the board row
     * @param boardColumn the board column
     * @param chessColor  the piece color
     * @return new Rook
     * @author Logan Sandlin
     * @author John Gauci
     */
    public static Rook newRook(final int boardRow, final int boardColumn, final ChessColor chessColor) {
	return new Rook(boardRow, boardColumn, chessColor, boardRow, boardColumn);
    }

    /**
     * Moves a rook.
     *
     * @param gameState   the game's state.
     * @param destination the rook's destination.
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
	    } else {
		gameState.incrementHalfMoves();
	    }
	    if (!this.hasMoved()) {
		if (this.getBoardColumn() == 0)
		    gameState.revokeCastlingRights(CastlingSide.QUEEN);
		else if (this.getBoardColumn() == 7)
		    gameState.revokeCastlingRights(CastlingSide.KING);
		this.setMoved();
	    }
	    movingPiece.setBoardColumn(destination.x());
	    movingPiece.setBoardRow(destination.y());
	}
	this.updateCanvasRowColumn();
    }

    /**
     * Trys to add a move to the list of potential moves.
     *
     * @param gameState    the state of the game.
     * @param moves        the list of moves.
     * @param possibleMove the possible move to try to add.
     * @return false if the line of valid moves has stopped.
     * @author Alexander Cooper
     */
    private boolean tryAddPotentialMove(final GameState gameState, final Collection<Vector2> moves,
	    final Vector2 possibleMove) {
	final var possibleCollision = gameState.getPiece(possibleMove);
	if (possibleCollision.isPresent()) {
	    if (possibleCollision.get().getChessColor() != this.getChessColor())
		moves.add(possibleMove);
	    return false;
	}
	moves.add(possibleMove);
	return true;
    }

    /**
     * Makes and returns  a list of the spaces accessible to the piece
     *
     * @param gameState the current game state
     * @return a list of the spaces accessible to the piece
     * @author Logan Sandlin
     */
    public ArrayList<Vector2> getPotentialMoves(final GameState gameState) {
	final ArrayList<Vector2> moves = new ArrayList<>(8);
	for (int col = this.getBoardColumn() - 1; col >= 0; col--) {
	    if (!this.tryAddPotentialMove(gameState, moves, new Vector2(col, this.getBoardRow())))
		break;
	}
	for (int col = this.getBoardColumn() + 1; col < 8; col++) {
	    if (!this.tryAddPotentialMove(gameState, moves, new Vector2(col, this.getBoardRow())))
		break;
	}
	for (int row = this.getBoardRow() - 1; row >= 0; row--) {
	    if (!this.tryAddPotentialMove(gameState, moves, new Vector2(this.getBoardColumn(), row)))
		break;
	}
	for (int row = this.getBoardRow() + 1; row < 8; row++) {
	    if (!this.tryAddPotentialMove(gameState, moves, new Vector2(this.getBoardColumn(), row)))
		break;
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
	case WHITE -> window.drawPicture(this.getRow(), this.getColumn(), this.getWidth(), this.getHeight(), 3);
	case BLACK -> window.drawPicture(this.getRow(), this.getColumn(), this.getWidth(), this.getHeight(), 9);
	}
    }
}
