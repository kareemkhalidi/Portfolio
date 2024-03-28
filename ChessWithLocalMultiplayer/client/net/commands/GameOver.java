package client.net.commands;

import base.Command;
import base.Communicator;
import client.game.ClientGameHandler;

/**
 * Command that handles game over on client
 *
 * @author John Gauci
 */

public class GameOver extends Command {
    /**
     * Handles the game over process for the client
     *
     * @param communicator the ClientGameHandler to operate upon
     * @author John Gauci
     */
    @Override
    public void execute(final Communicator communicator) {
        final ClientGameHandler clientGameHandler = (ClientGameHandler) communicator;
        clientGameHandler.getGameManager().pauseResumeGameOver();
        clientGameHandler.notifyObservers(); // todo see if this line needed
    }
}
