package base;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementation of an abstract RobustThread for simplified starting and stopping of loop threads.
 *
 * @author John Gauci
 */

public abstract class RobustThread implements Runnable {

    private static final AtomicInteger THREAD_COUNT = new AtomicInteger(0);
    private final AtomicBoolean active = new AtomicBoolean(false);
    private String name = "Unnamed";
    private Thread thread;

    protected RobustThread() {
        super();
        this.thread = new Thread();
    }

    /**
     * Sets the name of this RobustThread
     *
     * @param name thread name
     * @author John Gauci
     */
    public final void setName(final String name) {
        this.name = name;
    }

    /**
     * Starts the RobustThread
     *
     * @author John Gauci
     */
    public void start() {
        this.active.setRelease(true);
        this.thread = new Thread(this, this.name + " RobustThread-" + RobustThread.THREAD_COUNT.getAndIncrement());
        this.thread.start();
    }

    /**
     * Code to be run in thread loop.
     *
     * @author John Gauci
     */
    protected abstract void loopExecute();

    /**
     * Stops the thread, successful if loop is not blocked
     *
     * @author John Gauci
     */
    public void stop() {
        if (this.active.getAcquire()) {
            RobustThread.THREAD_COUNT.decrementAndGet();
            this.active.setRelease(false);
        }
    }

    /**
     * Stops the thread, successful regardless whether the loop is blocked
     *
     * @author John Gauci
     */
    public void forceStop() {
        if (this.active.getAcquire()) {
            RobustThread.THREAD_COUNT.decrementAndGet();
            this.active.setRelease(false);
            this.thread.interrupt();
        }
    }

    /**
     * Runs the code implemented in loop execute in a loop
     *
     * @author John Gauci
     */
    @Override
    public final void run() {
        this.active.setRelease(true);
        while (this.active.getAcquire()) {
            this.loopExecute();
        }
    }

    /**
     * Returns whether the thread is active
     *
     * @return whether the thread is active
     * @author John Gauci
     */
    public boolean isActive() {
        return this.active.getAcquire();
    }


}
