package server.net.commands;

import base.Command;
import base.Communicator;
import base.Packet;
import client.net.commands.GameOver;
import server.game.ServerGameHandler;

/**
 * Check game over command that checks if the game is over when executed
 *
 * @author John Gauci
 */
public class CheckGameOver extends Command {


    /**
     * Checks to see if the game is over and ends it if so
     *
     * @param communicator the ServerGameHandler to operate upon
     * @author John Gauci
     */
    @Override
    public void execute(final Communicator communicator) {
        final var serverGameHandler = (ServerGameHandler) communicator;
        final var state = serverGameHandler.getState();
        if (state.isGameOver()) {
            serverGameHandler.sendAllSockets(new Packet(serverGameHandler.getState(), new GameOver()));
        }
    }
}
