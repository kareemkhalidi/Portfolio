package base;

/**
 * Implements an abstract MouseInput class used to provide information about a mouse input.
 *
 * @author John Gauci
 */
public abstract class MouseInput {
    private final int button;
    private final int row;
    private final int column;

    protected MouseInput(final int button, final int row, final int column) {
        super();

        this.button = button;
        this.row = row;
        this.column = column;
    }

    /**
     * Returns the button for this mouse input
     *
     * @return the button for this mouse input
     * @author John Gauci
     */
    public int getButton() {
        return this.button;
    }

    /**
     * Returns the row of this mouse input
     *
     * @return the row of this mouse input
     * @author John Gauci
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Returns the column of the mouse input
     *
     * @return the column of this mouse input
     * @author John Gauci
     */
    public int getColumn() {
        return this.column;
    }
}
