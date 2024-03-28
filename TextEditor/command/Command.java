package command;

/**
 * Abstract command class for any actions that may be performed to the document
 *
 * @author John Gauci
 * @author Kareem Khalidi
 */
abstract class Command {

    /**
     * Executes the command
     *
     * @author John Gauci
     * @author Kareem Khalidi
     */
    public abstract void execute();

    /**
     * Undoes the command
     *
     * @author John Gauci
     * @author Kareem Khalidi
     */
    public abstract void unExecute();

}
