package server.net.commands;


import base.Command;
import base.Communicator;
import game.Player;
import server.game.ServerGameHandler;

/**
 * Command for connecting to server
 *
 * @author John Gauci
 */

public class Connect extends Command {

    private final Player player;

    /**
     * Initializes a new connect command with the player that is connecting.
     *
     * @param player the player that is connecting
     * @author John Gauci
     */
    public Connect(final Player player) {
        super();
        this.player = player;
    }

    /**
     * Handles the connecting of a player to the server
     *
     * @param communicator the ServerGameHandler to operate upon
     * @author John Gauci
     */
    @Override
    public void execute(final Communicator communicator) {
        final ServerGameHandler serverGameHandler = (ServerGameHandler) communicator;
        serverGameHandler.getState().addPlayer(this.player);
    }


}
