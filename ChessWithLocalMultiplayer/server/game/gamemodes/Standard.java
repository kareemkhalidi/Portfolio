package server.game.gamemodes;

import base.GameOverType;
import game.pieces.King;
import game.pieces.Piece;
import server.game.GameState;

import java.util.ArrayList;

/**
 * A standard Chess game mode
 *
 * @author Alexander Cooper
 * @author John Gauci
 */
public class Standard extends GameMode {
    /**
     * Constructor for a game mode.
     *
     * @param playerLimit      the player limit for the game mode
     * @param timeLimitMinutes the time limit for a turn in minutes.
     * @author John Gauci
     */
    public Standard(final int playerLimit, final int timeLimitMinutes) {
        super(playerLimit, timeLimitMinutes);
    }

    /**
     * Initializes a new game mode with a default player limit of 2 and time limit of 30
     *
     * @author John Gauci
     */
    public Standard() {
        super(2);
    }

    /**
     * Determines if the game is over.
     *
     * @param gameState the state of the game.
     * @return GameOverType representing the type of game over.
     * @author Alexander Cooper
     */
    @Override
    public GameOverType isOver(final GameState gameState) {
        if (gameState.getHalfMoves() == 100)
            return GameOverType.DRAW;

        if (this.isTimed() && gameState.getTurnClock() <= 0)
            return GameOverType.TIME_OUT;

        boolean playerHasMoves = false;
        King king = null;
        for (final Piece piece : new ArrayList<>(gameState.getActivePieces())) {
            if (gameState.getActivePlayer().isPresent()) {
                if (gameState.getActivePlayer().get().getChessColor() == piece.getChessColor()) {
                    if (!piece.getValidMoves(gameState).isEmpty())
                        playerHasMoves = true;
                    if (piece instanceof King)
                        king = (King) piece;
                }
            }
        }

        if (!playerHasMoves) {
            if (king != null && gameState.positionInCheck(king.getPosition()))
                return GameOverType.CHECKMATE;
            else
                return GameOverType.STALEMATE;
        }

        return GameOverType.NOT_OVER;
    }
}
