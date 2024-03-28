package command;

import java.util.LinkedList;

/**
 * A list that stores all the performed commands and has a
 * pointer to the most recent command
 *
 * @author John Gauci
 * @author Kareem Khalidi
 */
public class CommandList {

    private final LinkedList<Command> commandList;
    private int currentCommand;

    /**
     * Constructor for a new command list object
     *
     * @author John Gauci
     * @author Kareem Khalidi
     */
    public CommandList() {
        commandList = new LinkedList<>();
        currentCommand = 0;
    }

    /**
     * Adds a new command to the command list and executes
     *
     * @param command the command to add to the list
     * @author John Gauci
     * @author Kareem Khalidi
     */
    public void addAndExecuteCommand(Command command) {
        if (commandList.size() > 1000) {
            commandList.clear();
        }
        commandList.add(currentCommand, command);
        command.execute();
    }

    /**
     * Undoes the most recently performed command
     *
     * @author John Gauci
     * @author Kareem Khalidi
     */
    public void undo() {
        if (currentCommand < commandList.size()) {
            commandList.get(currentCommand).unExecute();
            currentCommand++;
        }
    }

    /**
     * Redoes the most recently undone command
     *
     * @author John Gauci
     * @author Kareem Khalidi
     */
    public void redo() {
        if (currentCommand > 0) {
            currentCommand--;
            commandList.get(currentCommand).execute();
        }
    }

}