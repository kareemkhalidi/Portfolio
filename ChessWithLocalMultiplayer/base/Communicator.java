package base;

import java.util.AbstractList;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Abstract communicator class for tick-based processing of packets and commands and managing
 * sockets
 *
 * @author John Gauci
 */

public abstract class Communicator extends Subject {
    private static final int DEFAULT_TICK_RATE = 20;
    private final long tickFrequency;
    private final int tickRate;
    private final int maximumConnections;

    private final LinkedBlockingQueue<Packet> receivedPackets;

    private final LinkedBlockingQueue<Command> commands;

    private final AbstractList<RobustSocket> sockets;


    /**
     * Initializes a communicator with the given tick rate
     *
     * @author John Gauci
     */
    protected Communicator(final int maximumConnections) {
        super();
        this.tickFrequency = (long) (1.0 / (Communicator.DEFAULT_TICK_RATE / 1000.0));
        this.tickRate = Communicator.DEFAULT_TICK_RATE;
        this.maximumConnections = maximumConnections;
        this.receivedPackets = new LinkedBlockingQueue<>();
        this.commands = new LinkedBlockingQueue<>();
        this.sockets = new Vector<>(10);
    }

    /**
     * Initializes a communicator with a default tick rate of 120
     *
     * @author John Gauci
     */
    protected Communicator() {
        this(10);
    }

    /**
     * Returns the tick rate of the Communicator
     *
     * @return tick rate
     * @author John Gauci
     */
    public int getTickRate() {
        return this.tickRate;
    }


    /**
     * Adds a socket that can be used to send and receive packets from
     *
     * @param socket socket to be added
     * @author John Gauci
     */
    public void addSocket(final RobustSocket socket) {
        this.sockets.add(socket);
    }

    /**
     * Adds the given packet to the queue of received packets
     *
     * @param packet received packet
     * @author John Gauci
     */
    void addPacket(final Packet packet) {
        this.receivedPackets.add(packet);
    }

    /**
     * Calls process tick at the specified tick rate in a loop
     *
     * @author John Gauci
     */
    @Override
    public final void loopExecute() {
        try {
            final long timeStart = System.currentTimeMillis();
            this.processTick();
            final long timeEnd = System.currentTimeMillis();
            final long sleepTime = this.tickFrequency - (timeEnd - timeStart);
            if (sleepTime >= 0L) Thread.sleep(sleepTime);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends the given packet to all added sockets
     *
     * @param packet packet to be sent
     * @author John Gauci
     */
    public void sendAllSockets(final Packet packet) {
        this.sockets.forEach(socket -> socket.sendPacket(packet));
    }

    /**
     * Sends the given packet to the socket with the given id
     *
     * @param id     socket id
     * @param packet packet to be sent
     * @author John Gauci
     */
    public void sendUnique(final long id, final Packet packet) {
        for (final RobustSocket socket : this.sockets) {
            if (socket.getId() == id) {
                socket.sendPacket(packet);
            }
        }

    }

    /**
     * Removes management of the socket with the given id
     *
     * @param id socket id
     * @author John Gauci
     */
    public void removeSocket(final long id) {
        this.sockets.removeIf(robustSocket -> robustSocket.getId() == id);
    }

    /**
     * Adds the given command to the queue of commands
     *
     * @param command the command to be added
     * @author John Gauci
     */
    public void addCommand(final Command command) {
        this.commands.add(command);
    }

    /**
     * Executes all the commands found in the queue when called
     *
     * @author John Gauci
     */
    public void executeAllCommands() {
        final Queue<Command> tempCommands = new LinkedBlockingQueue<>(this.commands);
        this.commands.clear();
        while (!tempCommands.isEmpty())
            tempCommands.poll().execute(this);
    }

    /**
     * Stops the communicator thread, all of its sockets, and clears all queues and storage
     *
     * @author John Gauci
     */
    @Override
    public void stop() {
        super.stop();
        this.receivedPackets.clear();
        this.commands.clear();
    }

    /**
     * Code to be executed each tick
     *
     * @author John Gauci
     */
    protected abstract void processTick();

    /**
     * Returns whether the Communicator is open to receiving new connections
     *
     * @return whether the Communicator is open to new connections
     * @author John Gauci
     */
    public boolean isOpen() {
        return this.sockets.size() < this.maximumConnections;
    }

    /**
     * Returns the queue of received packets
     *
     * @return queue of received packets
     * @author John Gauci
     */
    public LinkedBlockingQueue<Packet> getReceivedPackets() {
        return this.receivedPackets;
    }
}
