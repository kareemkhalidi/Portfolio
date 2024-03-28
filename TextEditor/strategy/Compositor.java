package strategy;

import composite.Composite;

/**
 * The compositor class
 *
 * @author John Gauci
 */
public abstract class Compositor {

    private Composite composite;

    /**
     * Abstract compose method
     *
     * @author John Gauci
     */
    public abstract void compose();

    /**
     * Returns the compositors composite
     *
     * @return composite
     * @author John Gauci
     */
    public Composite getComposite() {
        return composite;
    }

    /**
     * Sets the compositors composite to the provided composite
     *
     * @param composite the composite to set to
     * @author John Gauci
     */
    public void setComposite(Composite composite) {
        this.composite = composite;
    }
}
