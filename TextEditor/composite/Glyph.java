package composite;

import base.Window;
import iteratorVisitor.GlyphVisitor;
import iteratorVisitor.Iterator;

/**
 * Implements an abstract Glyph class with document visual functionality.
 *
 * @author John Gauci
 */

public abstract class Glyph {
    private final int primaryColor;
    private final int secondaryColor;
    private int row;
    private int column;
    private int width;
    private int height;
    private boolean visible;

    /**
     * Initializes a glyph with the given attributes.
     *
     * @param width          the width of the glyph
     * @param height         the height of the glyph
     * @param primaryColor   the primary color of the glyph
     * @param secondaryColor the secondary color of the glyph
     * @author John Gauci
     */
    public Glyph(int width, int height, int primaryColor, int secondaryColor) {
        this.width = width;
        this.height = height;
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
        visible = true;
    }


    /**
     * Returns the row of the glyph.
     *
     * @return the row
     * @author John Gauci
     */
    public int getRow() {
        return row;
    }

    /**
     * Sets the row of the glyph.
     *
     * @param row the row to set to
     * @author John Gauci
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Gets the column of the glyph.
     *
     * @return the column
     * @author John Gauci
     */
    public int getColumn() {
        return column;
    }

    /**
     * Sets the column of the glyph.
     *
     * @param column the column to set to
     * @author John Gauci
     */
    public void setColumn(int column) {
        this.column = column;
    }

    /**
     * Gets the width of the glyph.
     *
     * @return the width
     * @author John Gauci
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the width of the glyph
     *
     * @param width the width to set to
     * @author John Gauci
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Gets the height of the glyph.
     *
     * @return the height
     * @author John Gauci
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets the height of the glyph
     *
     * @param height the height to set to
     * @author John Gauci
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Gets the primary color of the glyph.
     *
     * @return the primary color
     * @author John Gauci
     */
    public int getPrimaryColor() {
        return primaryColor;
    }

    /**
     * Returns whether the glyph is visible
     *
     * @return visible status
     * @author John Gauci
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets the glyph to be visible
     *
     * @author John Gauci
     */
    public void setVisible() {
        this.visible = true;
    }

    /**
     * Sets the glyph to be invisible
     *
     * @author John Gauci
     */
    public void setInvisible() {
        this.visible = false;
    }

    /**
     * Returns an iterator for the glyph
     *
     * @return glyph iterator
     * @author John Gauci
     */
    public abstract Iterator<Glyph> iterator();

    /**
     * Visits the glyph visitor of this glyph
     *
     * @param visitor the GlyphVisitor to visit
     * @author John Gauci
     */
    public abstract void accept(GlyphVisitor visitor);

    /**
     * Gets the glyph at the given index
     *
     * @param index index of the glyph to be returned
     * @return glyph at index
     * @author John Gauci
     */
    public abstract Glyph get(int index);

    /**
     * Draws the glyph using the given window.
     *
     * @param window the window to draw with
     * @author John Gauci
     */
    public abstract void draw(Window window);

    /**
     * Adds the glyph to the glyph
     *
     * @param index the index of the glyph to be added
     * @param glyph glyph to be added
     * @author John Gauci
     */
    public abstract void add(int index, Glyph glyph);

    /**
     * Removes the given glyph from the glyph
     *
     * @param index the index of the glyph to be removed
     * @return the removed glyph
     * @author John Gauci
     */
    public abstract Glyph remove(int index);


    /**
     * Returns the size of the glyph
     *
     * @return glyph size
     * @author John Gauci
     */
    public abstract int size();

    public int getSecondaryColor() {
        return secondaryColor;
    }
}
