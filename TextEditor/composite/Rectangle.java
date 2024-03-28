package composite;

import base.Window;
import iteratorVisitor.GlyphVisitor;
import iteratorVisitor.Iterator;
import iteratorVisitor.SingleIterator;

/**
 * Implements a rectangle for viewing in a document.
 *
 * @author John Gauci
 */

public class Rectangle extends Glyph {

    /**
     * Initializes a rectangle with the given attributes.
     *
     * @param width          the width of the rectangle
     * @param height         the height of the rectangle
     * @param primaryColor   the primary color of the rectangle
     * @param secondaryColor the secondary color of the rectangle
     * @author John Gauci
     */
    public Rectangle(int width, int height, int primaryColor, int secondaryColor) {
        super(width, height, primaryColor, secondaryColor);
    }

    /**
     * Draws the rectangle to the window.
     *
     * @param window the window to draw with
     * @author John Gauci
     */
    @Override
    public void draw(Window window) {
        window.drawRectangle(getRow(), getColumn(), getWidth(), getHeight(),
                getSecondaryColor(), getPrimaryColor());
    }

    /**
     * Unused.
     *
     * @param index the index of the glyph to be added
     * @param glyph glyph to be added
     * @author John Gauci
     */
    @Override
    public void add(int index, Glyph glyph) {

    }

    /**
     * Unused.
     *
     * @param index the index of the glyph to be removed
     * @return the removed glyph
     * @author John Gauci
     */
    @Override
    public Glyph remove(int index) {
        return null;
    }


    /**
     * Unused.
     *
     * @param child index of the glyph to be returned
     * @return John Gauci
     */
    @Override
    public Glyph get(int child) {
        return null;
    }

    /**
     * Returns the size of the rectangle
     *
     * @return size (1)
     * @author John Gauci
     */
    @Override
    public int size() {
        return 1;
    }

    /**
     * Creates and returns an iterator for the rectangle
     *
     * @return iterator
     * @author John Gauci
     */
    @Override
    public Iterator<Glyph> iterator() {
        return new SingleIterator<>(this);
    }

    /**
     * Visits the rectangles visitor
     *
     * @param visitor the visitor to visit
     * @author John Gauci
     */
    @Override
    public void accept(GlyphVisitor visitor) {
        visitor.visitRectangle(this);
    }

}
