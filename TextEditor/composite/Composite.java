package composite;

import base.Window;
import iteratorVisitor.GlyphVisitor;
import iteratorVisitor.Iterator;
import iteratorVisitor.ListIterator;
import mvc.DocumentState;
import strategy.Compositor;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements a composite which can store and operate on multiple glyphs.
 */

public class Composite extends Glyph {
    private final Compositor compositor;
    private final DocumentState documentState;
    private final List<Glyph> children;

    /**
     * Constructor for composite
     *
     * @param compositor    the compositor
     * @param documentState the state of the document
     * @author John Gauci
     */
    public Composite(Compositor compositor, DocumentState documentState) {
        super(0, 0, 0, 0);
        this.compositor = compositor;
        this.documentState = documentState;
        children = new ArrayList<>();
    }

    /**
     * Calls draw on all children
     *
     * @param window the window to draw in
     * @author John Gauci
     */
    @Override
    public void draw(Window window) {
        children.forEach(row -> row.draw(window));
    }

    /**
     * Adds a new glyph to the composite at the specified index
     *
     * @param index the index to add the glyph
     * @param glyph the glyph to add
     * @author John Gauci
     */
    @Override
    public void add(int index, Glyph glyph) {
        children.add(index, glyph);
    }

    /**
     * Removes the given glyph from the glyph
     *
     * @param index the index of the glyph to be removed
     * @return the removed glyph
     * @author John Gauci
     */
    @Override
    public Glyph remove(int index) {
        return children.remove(index);
    }

    /**
     * Get the child glyph at the specified index
     *
     * @param index the index of the glyph to return
     * @return glyph
     * @author John Gauci
     */
    @Override
    public Glyph get(int index) {
        if (index > -1 && index < children.size()) {
            return children.get(index);
        } else {
            return null;
        }
    }

    /**
     * Returns the size of the composite
     *
     * @return size
     * @author John Gauci
     */
    @Override
    public int size() {
        return children.size();
    }

    /**
     * Returns an iterator for the composite
     *
     * @return iterator
     * @author John Gauci
     */
    @Override
    public Iterator<Glyph> iterator() {
        return new ListIterator<>(children);
    }

    /**
     * Calls visit on the composites visitor
     *
     * @param visitor the visitor to visit
     * @author John Gauci
     */
    @Override
    public void accept(GlyphVisitor visitor) {
        visitor.visitComposite(this);
    }

    /**
     * Returns the width of the composite
     *
     * @return width
     * @author John Gauci
     */
    @Override
    public int getWidth() {
        return children.stream().mapToInt(Glyph::getWidth).max().orElse(0);
    }

    /**
     * Returns the height of the composite
     *
     * @return height
     * @author John Gauci
     */
    @Override
    public int getHeight() {
        return children.stream().mapToInt(Glyph::getHeight).max().orElse(0);
    }

    /**
     * Sets the composite and all of its children invisible
     *
     * @author John Gauci
     */
    @Override
    public void setInvisible() {
        super.setInvisible();
        children.forEach(Glyph::setInvisible);
    }

    /**
     * Sets the composite and all of its children visible
     *
     * @author John Gauci
     */
    @Override
    public void setVisible() {
        super.setVisible();
        children.forEach(Glyph::setVisible);
    }

    /**
     * Gets the row of the composite
     *
     * @return row
     * @author John Gauci
     */
    @Override
    public int getRow() {
        if (children.size() != 0)
            return children.get(0).getRow();
        else
            return 0;
    }

    /**
     * Sets the row for the composite
     *
     * @param row the row to set for the composite
     * @author John Gauci
     */
    @Override
    public void setRow(int row) {
        children.forEach(child -> child.setRow(row));
    }

    /**
     * clears the composites children and then composes the composite
     *
     * @author John Gauci
     */
    public void repair() {
        children.clear();
        compositor.compose();
    }

    /**
     * Gets the document state
     *
     * @return document state
     * @author John Gauci
     */
    public DocumentState getDocumentState() {
        return documentState;
    }


}
