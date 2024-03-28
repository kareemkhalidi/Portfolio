package base;

/**
 * Implements an abstract threaded Observer class for observing a subject.
 *
 * @author John Gauci
 */
public abstract class Observer extends RobustThread {

    private final Subject subject;
    private State state;

    /**
     * Initializes an observer with the subject to observe.
     *
     * @author John Gauci
     */
    protected Observer(final Subject subject) {
        super();
        this.subject = subject;
        this.state = subject.getState();
    }


    /**
     * Returns the subject
     *
     * @return the subject
     * @author John Gauci
     */
    public Subject getSubject() {
        return this.subject;
    }

    /**
     * Updates the subject by retrieving the state of the subject.
     *
     * @author John Gauci
     */
    public void update() {
        this.state = this.subject.getState();
    }

    /**
     * Gets the state of the subject.
     *
     * @return the state of the subject.
     * @author John Gauci
     */
    public State getState() {
        return this.state;
    }


}
