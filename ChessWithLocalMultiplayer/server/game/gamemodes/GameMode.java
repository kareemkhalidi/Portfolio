package server.game.gamemodes;

import base.GameOverType;
import server.game.GameState;

import java.io.Serializable;

/**
 * Abstract game mode class
 *
 * @author John Gauci
 */
public abstract class GameMode implements Serializable {

    private final int playerLimit;
    private final int timeLimitMinutes;
    private final boolean noTimeLimit;

    /**
     * Constructor for a game mode.
     *
     * @author John Gauci
     */
    public GameMode(final int playerLimit, final int timeLimitMinutes) {
        super();
        this.playerLimit = playerLimit;
        this.timeLimitMinutes = timeLimitMinutes;
        this.noTimeLimit = false;
    }

    /**
     * Sets a game mode with a player limit, but no time limit.
     *
     * @param playerLimit the player limit.
     * @author Alexander Cooper
     */
    protected GameMode(final int playerLimit) {
        super();
        this.playerLimit = playerLimit;
        this.timeLimitMinutes = Integer.MAX_VALUE;
        this.noTimeLimit = true;
    }

    /**
     * Returns the time limit for the game mode in minutes
     *
     * @return time limit
     * @author John Gauci
     */
    public int getTimeLimitMinutes() {
        return this.timeLimitMinutes;
    }

    /**
     * Abstract method that all game modes must implement that checks if the game is over
     *
     * @param gameState the current game state
     * @return GameOverType representing the type of game over.
     * @author John Gauci
     */
    public abstract GameOverType isOver(GameState gameState);

    /**
     * Returns the player limit for this game mode
     *
     * @return player limit
     * @author John Gauci
     */
    public int getPlayerLimit() {
        return this.playerLimit;
    }

    /**
     * Determines if the turns are times.
     *
     * @return true if turns are timed
     * @author Alexander Cooper
     */
    public boolean isTimed() { return !this.noTimeLimit; }
}
