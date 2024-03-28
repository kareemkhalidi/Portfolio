package base;

import java.io.Serializable;

/**
 * Implementation of a User class that represents a connected user.
 *
 * @author John Gauci
 */
public class User implements Serializable {
    private final String name;
    private final long id;
    private long ping;

    /**
     * Initializes a user with the given name
     *
     * @param name the name of the user
     * @author John Gauci
     */
    public User(final String name) {
        super();
        if (name.isBlank())
            this.name = "Default";
        else
            this.name = name;
        this.id = new java.util.Random().nextLong();
        this.ping = Long.MIN_VALUE;
    }

    /**
     * Returns the playerStats id to the user.
     *
     * @return the player's id.
     * @author John Gauci
     */
    public long getId() {
        return this.id;
    }

    /**
     * Returns the playerStats name to the user.
     *
     * @return the playerStats name.
     * @author John Gauci
     */
    public String getName() {
        return this.name;
    }

    /**
     * Overridden equals method, checks if player == o
     *
     * @param o the object to compare the player to.
     * @return boolean representing player == o.
     * @author John Gauci
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;

        return this.id == user.id;
    }

    /**
     * Returns a hash value for the player.
     *
     * @return integer hash for the player.
     * @author John Gauci
     */
    @Override
    public int hashCode() {
        return (int) (this.id ^ (this.id >>> 32));
    }

    /**
     * Returns the ping of the user
     *
     * @return the ping of the user
     * @author John Guaci
     */
    public long getPing() {
        return this.ping;
    }

    /**
     * Sets the ping of the user to the given ping
     *
     * @param ping the ping for the user
     * @author John Gauci
     */
    public void setPing(final long ping) {
        this.ping = ping;
    }
}
