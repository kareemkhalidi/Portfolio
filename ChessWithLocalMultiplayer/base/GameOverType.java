package base;

/**
 * Enum used to differentiate between the causes of the game ending, or not ending.
 *
 * @author Alexander Cooper
 */
public enum GameOverType {
    /**
     * Game ended in checkmate
     */
    CHECKMATE,
    /**
     * Game ended in stalemate
     */
    STALEMATE,
    /**
     * Game ended in draw that was not stalemate
     */
    DRAW,
    /**
     * Game ended with time running out
     */
    TIME_OUT,
    /**
     * Game is not over
     */
    NOT_OVER
}
