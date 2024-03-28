package decorator;

import composite.Glyph;

/**
 * Abstract decorator class
 *
 * @author John Gauci
 */
public abstract class Decorator extends Glyph {

    /**
     * constructor for decorator that initializes all values to 0
     *
     * @author John Gauci
     */
    public Decorator() {
        super(0, 0, 0, 0);
    }
}
