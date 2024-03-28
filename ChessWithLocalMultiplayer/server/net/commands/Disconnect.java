package server.net.commands;

import base.Command;
import base.Communicator;
import game.Player;
import server.game.ServerGameHandler;

/**
 * Disconnect command that disconnects a player from the server
 *
 * @author John Gauci
 */
public class Disconnect extends Command {

    private final Player player;

    /**
     * Initializes a new disconnect command with the player that is disconnecting.
     *
     * @param player the player that is disconnecting
     * @author John Gauci
     */
    public Disconnect(final Player player) {
        super();
        this.player = player;
    }

    /**
     * Handles the disconnecting of a player from the server
     *
     * @param communicator the ServerGameHandler to operate upon
     * @author John Gauci
     */
    @Override
    public void execute(final Communicator communicator) {
        final ServerGameHandler serverGameHandler = (ServerGameHandler) communicator;
        serverGameHandler.getState().removePlayer(this.player);
    }
}
