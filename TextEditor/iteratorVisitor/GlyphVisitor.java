package iteratorVisitor;

import composite.Character;
import composite.*;

/**
 * Implements a visitor class used to visit glyph objects.
 */

public abstract class GlyphVisitor {

    /**
     * Visits a glyph character
     *
     * @param character the character to be visited
     * @author John Gauci
     */
    public abstract void visitCharacter(Character character);

    /**
     * Visits a glyph rectangle
     *
     * @param rectangle the rectangle to be visited
     * @author John Gauci
     */
    public abstract void visitRectangle(Rectangle rectangle);

    /**
     * Visits a glyph composite
     *
     * @param composite the composite to be visited
     * @author John Gauci
     */
    public abstract void visitComposite(Composite composite);

    /**
     * Visits a glyph cursor
     *
     * @param cursor the cursor to be visited
     * @author John Gauci
     */
    public abstract void visitCursor(Cursor cursor);

    /**
     * Visits a glyph hyphen
     *
     * @param hyphen the hyphen to be visited
     * @author John Gauci
     */
    public abstract void visitHyphen(Hyphen hyphen);


}
