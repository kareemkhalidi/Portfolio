package client.game;

import base.Command;
import base.Communicator;
import base.Packet;
import base.Vector2;
import client.net.commands.HighlightTiles;
import game.Player;
import server.game.GameState;
import server.net.commands.Connect;
import server.net.commands.Disconnect;
import server.net.commands.MovePiece;

import java.util.List;
import java.util.Vector;

/**
 * Implementation of a ClientGameHandler to handle game functionality for the client/player.
 *
 * @author John Gauci
 */

public final class ClientGameHandler extends Communicator {

    private final Player player;
    private final GameManager gameManager;
    private final List<Vector2> highlightedSquares;

    /**
     * Initializes a ClientGameHandler with the given player and game manager
     *
     * @param player      the player for the ClientGameHandler
     * @param gameManager the game manager for the ClientGameHandler
     * @author John Gauci
     */
    public ClientGameHandler(final Player player, final GameManager gameManager) {
        super();
        this.setName("ClientGameHandler");
        this.setState(gameManager.getGameState());
        this.player = player;
        this.gameManager = gameManager;
        this.highlightedSquares = new Vector<>(20);

    }

    /**
     * Returns the list of currently highlighted squares
     *
     * @return list of positions of highlighted squares
     * @author Kareem Khalidi
     */
    public List<Vector2> getHighlightedSquares() {
        return this.highlightedSquares;
    }

    /**
     * Takes a position to add as a highlighted square
     *
     * @param square the square to be highlighted
     * @author Kareem Khalidi
     */
    public void highlightSquare(final Vector2 square) {
        this.highlightedSquares.add(square);
    }

    /**
     * Clears any currently highlighted squares
     *
     * @authro Kareem Khalidi
     */
    public void clearHighlightedSquares() {
        this.highlightedSquares.clear();
    }

    /**
     * Returns the GameState of the subject.
     *
     * @return the GameState of the subject
     * @author John Gauci
     */
    @Override
    public GameState getState() {
        return (GameState) super.getState();
    }

    /**
     * Connects to the game at the given host address with the given name and tank
     *
     * @author John Gauci
     */
    public void connect() {
        this.sendCommand(new Connect(this.player));
    }

    /**
     * Disconnects from the currently connected game
     *
     * @author John Gauci
     */
    public void disconnect() {
        this.sendCommand(new Disconnect(this.player));
        try {
            Thread.sleep(1_000L);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
        this.stop();
    }

    /**
     * Moves the piece at the given starting position to the given ending position
     *
     * @param startPosition the current position of the piece to be moved
     * @param endPosition   the destination of the piece to be moved
     * @author John Gauci
     */
    public void movePiece(final Vector2 startPosition, final Vector2 endPosition) {
        this.sendCommand(new MovePiece(startPosition, endPosition));
    }


    /**
     * Wrapper for easily sending commands
     *
     * @param command command to be sent
     * @author John Gauci
     */
    private void sendCommand(final Command command) {
        this.sendAllSockets(new Packet(command));
    }

    /**
     * Processes the current game tick, including setting Subject data with the data received from
     * the server, executing any server commands, and notifying observers
     *
     * @author John Gauci
     */
    @Override
    protected void processTick() {
        final var receivedPackets = this.getReceivedPackets();
        while (!receivedPackets.isEmpty()) {
            final Packet currentPacket = receivedPackets.poll();
            currentPacket.getData().ifPresent(this::setState);
            currentPacket.getCommand().ifPresent(command -> command.execute(this));
            this.addCommand(new HighlightTiles());
            this.executeAllCommands();
            this.notifyObservers();
        }
    }

    /**
     * Return the Game Manager that manages this ClientGameHandler
     *
     * @return the Game Manager for this ClientGameHandler
     * @author John Gauci
     */
    public GameManager getGameManager() {
        return this.gameManager;
    }

    /**
     * Stops the ClientGameHandler thread, all of its sockets, and clears all queues and storage
     *
     * @author John Gauci
     */
    @Override
    public void stop() {
        super.stop();
        this.setState(new GameState());
    }

    /**
     * Returns the player associated with this ClientGameHandler
     *
     * @return the player for this ClientGameHandler
     * @author John Gauci
     */
    public Player getPlayer() {
        return this.player;
    }

}
