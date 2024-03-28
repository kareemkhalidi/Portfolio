package server.net;

import base.Communicator;
import base.RobustSocket;
import base.RobustThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class that handles game connections
 *
 * @author John Gauci
 */
public class ConnectionHandler extends RobustThread {

    private final ServerSocket serverSocket;
    private final Communicator communicator;
    private final ExecutorService pool;

    /**
     * Constructor for connection handler
     *
     * @param port         the port to connect to
     * @param communicator the communicator for the connection handler
     * @author John Gauci
     */
    public ConnectionHandler(final int port, final Communicator communicator) {
        super();
        this.setName("ConnectionHandler");
        this.communicator = communicator;
        try {
            this.serverSocket = new ServerSocket(port);
            this.pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() / 4);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Run code in loop.
     *
     * @author John Gauci
     */
    @Override
    public void loopExecute() {
        try {
            if (this.communicator.isOpen()) {
                final var socket = new RobustSocket(this.communicator, this.serverSocket.accept());
                this.communicator.addSocket(socket);
                this.pool.execute(socket);
            } else {
                super.forceStop();
            }
        } catch (final IOException ignored) {
        }
    }

    /**
     * Stops the connection handler
     *
     * @author John Gauci
     */
    @Override
    public void stop() {
        this.pool.shutdown();
        super.forceStop();
        try {
            this.serverSocket.close();
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }
}


