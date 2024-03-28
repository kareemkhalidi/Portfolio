package decorator;

import base.Window;
import composite.Composite;
import composite.Glyph;
import iteratorVisitor.GlyphVisitor;
import iteratorVisitor.Iterator;
import mvc.DocumentState;

import java.util.stream.IntStream;

/**
 * Scroll Decorator class
 *
 * @author John Gauci
 */
public class ScrollDecorator extends Decorator {

    private final Composite composite;

    private final DocumentState documentState;

    /**
     * Constructor for a new ScrollDecorator
     *
     * @param composite     the composite for the scroll decorator
     * @param documentState the state of the document
     * @author John Gauci
     */
    public ScrollDecorator(Composite composite, DocumentState documentState) {
        this.composite = composite;
        this.documentState = documentState;
    }

    /**
     * Draws the scroll decorator
     *
     * @param window the window to draw the scroll decorator in
     * @author John Gauci
     */
    @Override
    public void draw(Window window) {
        int startingRow = (int) ((Math.round(documentState.getScrollPosition()) / 100.0) * 100);
        for (int i = startingRow; i < composite.size(); i++) {
            Glyph currentRow = composite.get(i);
            currentRow.setRow(IntStream.range(startingRow, i).map(j -> composite.get(j).getHeight()).sum());
            currentRow.draw(window);
        }
    }

    @Override
    public void add(int index, Glyph glyph) {

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
        return null;
    }

    /**
     * Gets the specified child
     *
     * @param child the child to get
     * @return the specified child glyph
     * @author John Gauci
     */
    @Override
    public Glyph get(int child) {
        return composite.get(child);
    }

    /**
     * Gets the size of the scroll decorator
     *
     * @return size
     * @author John Gauci
     */
    @Override
    public int size() {
        return composite.size();
    }

    /**
     * Gets the width of the scroll decorator
     *
     * @return width (0)
     * @author John Gauci
     */
    @Override
    public int getWidth() {
        return 0;
    }

    /**
     * Gets the height of the scroll decorator
     *
     * @return height (0)
     * @author John Gauci
     */
    @Override
    public int getHeight() {
        return 0;
    }

    /**
     * Returns an iterator for the scroll decorator
     *
     * @return null
     * @author John Gauci
     */
    @Override
    public Iterator<Glyph> iterator() {
        return null;
    }

    /**
     * Visits the scroll decorators visitor
     *
     * @param visitor the visitor to visit
     * @author John Gauci
     */
    @Override
    public void accept(GlyphVisitor visitor) {
        visitor.visitComposite(composite);
    }
}
