package client.net.commands;

import base.Command;
import base.Communicator;
import client.game.ClientGameHandler;

/**
 * Loads the highlighted titles
 *
 * @author John Gauci
 */
public class HighlightTiles extends Command {


    /**
     * Loads the highlighted tiles from the local game state
     *
     * @param communicator the ServerGameHandler to operate upon
     * @author John Gauci
     */
    @Override
    public void execute(final Communicator communicator) {
        final var clientGameHandler = (ClientGameHandler) communicator;
        final var state = clientGameHandler.getState();
        state.getBoard().highlightSquare(clientGameHandler.getHighlightedSquares());
    }
}
