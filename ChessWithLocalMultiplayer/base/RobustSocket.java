package base;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

/**
 * Implementation of a RobustSocket class to work with sending and receiving of packets over a
 * network socket.
 *
 * @author John Gauci
 */

public class RobustSocket extends RobustThread {

    private final Socket socket;
    private final ObjectOutputStream output;
    private final ObjectInputStream input;
    private final Communicator communicator;
    private final long id;

    /**
     * Initializes a RobustSocket with the given communicator and remote host and port
     *
     * @param communicator the communicator to add to
     * @param host         remote address
     * @param port         remote port
     * @param id           the socket id
     * @author John Gauci
     */
    public RobustSocket(final Communicator communicator, final String host, final int port,
                        final long id) {
        super();
        this.setName("RobustClientSocket");
        this.id = id;
        this.communicator = communicator;
        try {
            this.socket = new Socket(host, port);
            this.socket.setReuseAddress(true);
            this.output = new ObjectOutputStream(this.socket.getOutputStream());
            this.input = new ObjectInputStream(this.socket.getInputStream());
            this.socket.setTcpNoDelay(true);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initializes a RobustSocket with the given communicator and socket
     *
     * @param communicator the communicator to add to
     * @param socket       the socket to enclose the RobustSocket around
     * @author John Gauci
     */
    public RobustSocket(final Communicator communicator, final Socket socket) {
        super();
        Thread.currentThread().setName("RobustServerSocket");
        this.setName("RobustServerSocket");
        this.id = new Random().nextLong();
        this.communicator = communicator;
        try {
            this.socket = socket;
            this.output = new ObjectOutputStream(this.socket.getOutputStream());
            this.input = new ObjectInputStream(this.socket.getInputStream());
            this.socket.setTcpNoDelay(true);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends the given packet to remote
     *
     * @param packet the packet to send
     * @author John Gauci
     */
    public void sendPacket(final Packet packet) {
        try {
            this.output.reset();
            packet.stampOutgoing(this.id);
            this.output.writeObject(packet);
            this.output.flush();
        } catch (final IOException ignored) {
        }
    }

    /**
     * Listens for incoming packets in a loop and adds them to the communicator.
     *
     * @author John Gauci
     */
    @Override
    public void loopExecute() {
        try {
            final Packet packet = (Packet) this.input.readObject();
            packet.stampIncoming(this.id);
            this.communicator.addPacket(packet);
        } catch (final IOException | ClassNotFoundException ignored) {
            if (this.isActive())
                this.stop();
        }
    }

    /**
     * Stops the listening loop and closes the underlying socket.
     *
     * @author John Gauci
     */
    @Override
    public void stop() {
        super.stop();
        this.communicator.removeSocket(this.id);
        try {
            this.socket.close();
        } catch (final IOException ignored) {
        }
    }

    /**
     * Returns the socket ID
     *
     * @return socket ID
     * @author John Gauci
     */
    public long getId() {
        return this.id;
    }

    /**
     * Returns whether the two sockets are equal
     *
     * @param o object to be checked for equality
     * @return whether two sockets are equal
     * @author John Gauci (IDE generated)
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final RobustSocket socket1 = (RobustSocket) o;
        if (!this.socket.equals(socket1.socket)) return false;
        if (!this.output.equals(socket1.output)) return false;
        return this.input.equals(socket1.input);
    }

    /**
     * Returns the hashcode for a RobustSocket object
     *
     * @return hashcode
     * @author John Gauci (IDE generated)
     */
    @Override
    public int hashCode() {
        int result = this.socket.hashCode();
        result = 31 * result + this.output.hashCode();
        result = 31 * result + this.input.hashCode();
        return result;
    }


}
