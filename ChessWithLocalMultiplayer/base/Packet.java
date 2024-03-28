package base;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

/**
 * Implementation of a network packet used to send state and (a) command.
 *
 * @author John Gauci
 */

public class Packet implements Serializable {
    private final State state;
    private final Command command;
    private InetAddress senderIP;
    private long senderID;
    private long senderTime;
    private InetAddress receiverIP;
    private long receiverID;
    private long receiverTime;
    private long ping;


    /**
     * Initializes a Packet with the given state and command
     *
     * @param state   the state for the packet
     * @param command the command for the packet
     * @author John Gauci
     */
    public Packet(final State state, final Command command) {
        super();
        this.state = state;
        this.command = command;
        this.ping = Long.MIN_VALUE;
        this.senderTime = Long.MIN_VALUE;
        this.receiverTime = Long.MIN_VALUE;
        this.senderIP = null;
        this.senderID = Long.MIN_VALUE;
        this.receiverIP = null;
        this.receiverID = Long.MIN_VALUE;
    }

    /**
     * Initializes a Packet with the given state
     *
     * @param state the state for the packet
     * @author John Gauci
     */
    public Packet(final State state) {
        this(state, null);
    }

    /**
     * Initializes a Packet with the given command
     *
     * @param command the command for the packet
     * @author John Gauci
     */
    public Packet(final Command command) {
        this(null, command);
    }

    /**
     * Stamps the outgoing packet with the sender's time, IP, and given socket ID
     *
     * @param senderID sender socket ID
     * @author John Gauci
     */
    public void stampOutgoing(final long senderID) {
        this.senderID = senderID;
        try {
            this.senderIP = InetAddress.getLocalHost();
        } catch (final UnknownHostException e) {
            throw new RuntimeException(e);
        }
        this.senderTime = System.currentTimeMillis();
    }

    /**
     * Stamps the received packet with the receiver's time, IP, and given socket ID
     *
     * @param receiverID receiver socket ID
     * @author John Gauci
     */
    public void stampIncoming(final long receiverID) {
        this.receiverTime = System.currentTimeMillis();
        this.receiverID = receiverID;
        try {
            this.receiverIP = InetAddress.getLocalHost();
        } catch (final UnknownHostException e) {
            throw new RuntimeException(e);
        }
        this.ping = this.receiverTime - this.senderTime;
    }

    /**
     * Returns the optional state
     *
     * @return optional state
     * @author John Gauci
     */
    public Optional<State> getData() {
        return Optional.ofNullable(this.state);
    }

    /**
     * Returns the optional command
     *
     * @return optional command
     * @author John Gauci
     */
    public Optional<Command> getCommand() {
        return Optional.ofNullable(this.command);
    }

    /**
     * Returns the ping, or packet latency, between sender and receiver
     *
     * @return packet latency
     * @author John Gauci
     */
    public long getPing() {
        return this.ping;
    }

    /**
     * Returns the receiver's IP
     *
     * @return receiver's IP
     * @author John Gauci
     */
    public InetAddress getReceiverIP() {
        return this.receiverIP;
    }

    /**
     * Returns the sender's IP
     *
     * @return sender's IP
     * @author John Gauci
     */
    public InetAddress getSenderIP() {
        return this.senderIP;
    }

    /**
     * Return's the sender's socket ID
     *
     * @return sender's socket ID
     * @author John Gauci
     */
    public long getSenderID() {
        return this.senderID;
    }

    /**
     * Return's the receiver's socket ID
     *
     * @return receiver's socket ID
     * @author John Gauci
     */
    public long getReceiverID() {
        return this.receiverID;
    }
}
