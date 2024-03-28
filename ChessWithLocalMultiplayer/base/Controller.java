package base;

/**
 * Implements an abstract strategy class for communicating between a subject and an observer.
 *
 * @author John Gauci
 */
public abstract class Controller {

    /**
     * Processes the given keyInput from a view
     *
     * @param keyInput the key input
     * @author John Gauci
     */
    public abstract void processKeyInput(KeyInput keyInput);

    /**
     * Processes the given mouseInput from a view
     *
     * @param mouseInput the mouse input
     * @author John Gauci
     */
    public abstract void processMouseInput(MouseInput mouseInput);
}
