package base;

/**
 * Implements an abstract mediator class for communicating between a subject and an observer.
 *
 * @author John Gauci
 */
public abstract class Mediator {


    private final Subject subject;

    /**
     * Initializes a mediator with a subject to communicate to
     *
     * @param subject the subject to communicate to
     * @author John Gauci
     */
    public Mediator(Subject subject) {
        this.subject = subject;
    }

    /**
     * Returns the subject to communicate to
     *
     * @return the subject to communicate to
     * @author John Gauci
     */
    public Subject getSubject() {
        return subject;
    }
}
