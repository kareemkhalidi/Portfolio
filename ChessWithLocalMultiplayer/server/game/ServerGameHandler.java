package server.game;

import base.Communicator;
import base.Packet;
import server.game.gamemodes.GameMode;
import server.net.commands.CheckGameOver;

/**
 * Server game handler class that handles all server side calculations, data, etc.
 *
 * @author John Gauci
 */
public final class ServerGameHandler extends Communicator {


    /**
     * Initializes a ServerGameHandler with the given game state
     *
     * @param gameState the game state
     * @author John Gauci
     */
    public ServerGameHandler(final GameState gameState) {
        super(gameState.getGameMode().getPlayerLimit());
        this.setName("ServerGameHandler");
        this.setState(gameState);
    }

    /**
     * Processes the current game tick
     *
     * @author John Gauci
     */
    @Override
    protected void processTick() {
        final var receivedPackets = this.getReceivedPackets();
        while (!receivedPackets.isEmpty()) {
            final var currentPacket = receivedPackets.poll();
            currentPacket.getCommand().ifPresent(command -> command.execute(this));
            this.getState().getPlayer(currentPacket.getSenderID()).ifPresent(player -> player.setPing(currentPacket.getPing()));
        }
        if (!this.getState().hasGameEnded()) {
            this.getState().decrementTurnClock();
            final var checkGameOver = new CheckGameOver();
            checkGameOver.execute(this);
        }
        this.executeAllCommands();
        this.sendAllSockets(new Packet(this.getState()));
    }


    /**
     * Gets the games data
     *
     * @return GameState the games data
     * @author John Gauci
     */
    @Override
    public GameState getState() {
        return (GameState) super.getState();
    }

}






