package base;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Implements an abstract threaded subject class used for working with information and updating
 * observers.
 *
 * @author John Gauci
 */

public abstract class Subject extends RobustThread {

    private final List<Observer> observers;

    private final AtomicReference<State> state;

    /**
     * Initializes a subject which can hold multiple observers.
     *
     * @author John Gauci
     */
    Subject() {
        super();
        this.state = new AtomicReference<>();
        this.observers = new ArrayList<>(5);
    }

    /**
     * Notifies all the attached observers by calling update on each one.
     *
     * @author John Gauci
     */
    public void notifyObservers() {
        this.observers.forEach(Observer::update);
    }

    public void addObserver(final Observer observer) {
        this.observers.add(observer);
    }


    /**
     * Returns the data of the subject.
     *
     * @return the data of the subject
     * @author John Gauci
     */
    public State getState() {
        return this.state.getAcquire();
    }

    /**
     * Sets the state of the subject to the given state.
     *
     * @param state the state to be setPlaying to
     * @author John Gauci
     */
    public void setState(final State state) {
        this.state.setRelease(state);
    }


}
