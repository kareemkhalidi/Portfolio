package base;

/**
 * Implements an abstract Observer class for observing a subject.
 *
 * @author John Gauci
 */
public abstract class Observer {

    private final Subject subject;
    private State state;

    /**
     * Initializes an observer with the subject to observe.
     *
     * @param subject the subject to observe.
     * @author John Gauci
     */
    public Observer(Subject subject) {
        this.subject = subject;
        state = subject.getState();
    }

    /**
     * Updates the subject by retrieving the state of the subject.
     *
     * @author John Gauci
     */
    public void update() {
        state = subject.getState();
    }

    /**
     * Gets the state of the subject.
     *
     * @return the state of the subject.
     * @author John Gauci
     */
    public State getState() {
        return state;
    }


}
