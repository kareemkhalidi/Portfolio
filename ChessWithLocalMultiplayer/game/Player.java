package game;


import base.GameOverType;
import base.User;
import game.pieces.ChessColor;

/**
 * Represents a player in the game.
 *
 * @author John Gauci
 */
public class Player extends User {

    private final ChessColor chessColor;

    /**
     * Constructor for a new player object
     *
     * @param name       the playerStats name
     * @param chessColor the player piece color
     * @author John Gauci
     */
    public Player(final String name, final ChessColor chessColor) {
        super(name);
        this.chessColor = chessColor;
    }

    /**
     * Returns the chess color of the player
     *
     * @return player piece color
     * @author John Gauci
     */
    public ChessColor getChessColor() {
        return this.chessColor;
    }

    /**
     * Returns whether two players are equal
     *
     * @param o the player to compare the player to.
     * @return whether two players are equal
     * @author John Gauci (IDE generated)
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Player player)) return false;
        if (!super.equals(o)) return false;

        return this.chessColor == player.chessColor;
    }

    /**
     * Returns the hashcode for a player
     *
     * @return hashcode for a player
     * @author John Gauci (IDE generated)
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (this.chessColor != null ? this.chessColor.hashCode() : 0);
        return result;
    }
}
