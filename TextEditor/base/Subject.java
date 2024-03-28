package base;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements a subject used for working with information and updating observers.
 *
 * @author John Gauci
 */

public abstract class Subject {

    private final List<Observer> observers;
    private State state;

    /**
     * Initializes a subject which can hold multiple observers.
     *
     * @author John Gauci
     */
    public Subject() {
        observers = new ArrayList<>();
    }

    /**
     * Notifies all the attached observers by calling update on each one.
     *
     * @author John Gauci
     */
    public final void notifyObservers() {
        observers.forEach(Observer::update);
    }

    /**
     * Attaches the given observer to the subject.
     *
     * @param observer the observer to be attached
     * @author John Gauci
     */
    public final void attachObserver(Observer observer) {
        observers.add(observer);
    }

    /**
     * Detaches the given observer from the subject
     *
     * @param observer the observer to be detached
     * @author John Gauci
     */
    public final void detachObserver(Observer observer) {
        observers.remove(observer);
    }

    /**
     * Returns the state of the subject.
     *
     * @return the state of the subject
     * @author John Gauci
     */
    public State getState() {
        return state;
    }

    /**
     * Sets the state of the subject to the given state.
     *
     * @param state the state to be set to
     * @author John Gauci
     */
    public void setState(State state) {
        this.state = state;
    }


}
