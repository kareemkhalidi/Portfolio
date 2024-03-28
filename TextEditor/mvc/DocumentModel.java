package mvc;

import base.Subject;
import base.Window;
import composite.Composite;
import composite.Cursor;
import composite.Glyph;
import composite.Return;
import decorator.ScrollDecorator;
import iteratorVisitor.Iterator;
import strategy.StandardCompositor;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements a DocumentModel as a subject for a document.
 *
 * @author John Gauci
 */

public class DocumentModel extends Subject {

    /**
     * Initializes a DocumentModel with default settings.
     *
     * @author John Gauci
     */
    public DocumentModel() {
        DocumentState state = new DocumentState();
        StandardCompositor standardCompositor = new StandardCompositor();
        Composite composite = new Composite(standardCompositor, state);
        standardCompositor.setComposite(composite);
        setState(state);
        state.setComposite(composite);
        getState().setCursorCharacter('|');
        getState().setFontWidth(24);
        getState().setFontHeight(40);
        getState().setFontColor(Window.COLOR_BLACK);
        getState().setFontName("Courier");
        getState().setBold(0);
        getState().setItalic(0);
        getState().setBackGroundColor(Window.COLOR_WHITE);
        getState().setAllGlyphs(new ArrayList<>(1000));
        addCursor(new Cursor(getState().getFontWidth(), getState().getFontHeight(),
                getState().getFontColor(), getState().getBackGroundColor(),
                getState().getCursorCharacter(),
                getState().getBold(), getState().getItalic(), getState().getFontName()));
        ScrollDecorator scrollDecorator = new ScrollDecorator(composite, state);
        state.setScrollDecorator(scrollDecorator);

    }

    /**
     * Moves the cursor horizontally by the given delta.
     *
     * @param delta the delta to move the cursor by
     * @author John Gauci
     */
    public void moveCursorHorizontal(int delta) {
        if (getState().getCursorIndex() + delta > -1 && getState().getCursorIndex() + delta < getState().getAllGlyphs().size()) {
            Glyph cursor = getState().getAllGlyphs().remove(getState().getCursorIndex());
            getState().getAllGlyphs().add(getState().getCursorIndex() + delta, cursor);
            getState().setCursorIndex(getState().getCursorIndex() + delta);
        }
        notifyObservers();
    }

    /**
     * Adds a cursor to the model
     *
     * @param cursor to add
     * @author John Gauci
     */
    public void addCursor(Cursor cursor) {
        getState().getAllGlyphs().add(0, cursor);
    }


    /**
     * Adds a backspace to the document.
     *
     * @author John Gauci
     */
    public void removeGlyph() {
        if (getState().getCursorIndex() > 0) {
            if (getState().getAllGlyphs().size() > 1) {
                getState().getAllGlyphs().remove(getState().getCursorIndex() - 1);
            }
            getState().setCursorIndex(getState().getCursorIndex() - 1);
        }
        notifyObservers();
    }


    /**
     * Returns the state of the document.
     *
     * @return the state of the document
     * @author John Gauci
     */
    @Override
    public DocumentState getState() {
        return (DocumentState) super.getState();
    }

    /**
     * Returns a list of restricted document characters.
     *
     * @return a list of restricted document characters
     * @author John Gauci
     */
    public List<java.lang.Character> getKeyRestriction() {
        List<java.lang.Character> keys = new ArrayList<>();
        keys.add((char) 0);
        keys.add((char) 1);
        return keys;
    }

    /**
     * Moves the cursor to the beginning of the line it is on.
     *
     * @author John Gauci
     */
    public void moveCursorHome() {
        Iterator<Glyph> iterator = getState().getComposite().iterator();
        loop:
        while (iterator.hasNext()) {
            Glyph current = iterator.next();
            for (int j = 0; j < current.size(); j++) {
                if (current.get(j) instanceof Cursor) {
                    moveCursorHorizontal(-j);
                    break loop;
                }
            }
        }
        notifyObservers();
    }

    /**
     * Moves the cursor to the end of the line it is on.
     *
     * @author John Gauci
     */
    public void moveCursorEnd() {
        Iterator<Glyph> iterator = getState().getComposite().iterator();
        loop:
        while (iterator.hasNext()) {
            Glyph current = iterator.next();
            for (int j = 0; j < current.size(); j++) {
                if (current.get(j) instanceof Cursor) {
                    int delta = 0;
                    if (current.get(current.size() - 1) instanceof Return)
                        delta = 1;
                    moveCursorHorizontal((current.size() - 1) - j - delta);
                    break loop;
                }
            }
        }

        notifyObservers();
    }

    /**
     * Gets the glyph to the left of the cursor
     *
     * @return glyph to the left of cursor
     * @author John Gauci
     */
    public Glyph leftOfCursor() {
        if (getState().getCursorIndex() > 0)
            return getState().getAllGlyphs().get(getState().getCursorIndex() - 1);
        else
            return null;
    }

    /**
     * Adds a glyph
     *
     * @param glyph the glyph to add
     * @author John Gauci
     */
    public void addGlyph(Glyph glyph) {
        getState().getAllGlyphs().add(getState().getCursorIndex(), glyph);
        getState().setCursorIndex(getState().getCursorIndex() + 1);
        notifyObservers();
    }

    /**
     * Moves the cursor to the specified index
     *
     * @author John Gauci
     * @author Kareem Khalidi
     */
    public void moveCursor(int index) {
        Glyph cursor = getState().getAllGlyphs().remove(getState().getCursorIndex());
        getState().getAllGlyphs().add(index, cursor);
        getState().setCursorIndex(index);
    }

    /**
     * Adds the scroll position to the document.
     *
     * @param position the given scroll position
     * @author John Gauci
     */
    public void addScroll(int position) {
        getState().setScrollPosition(position);
        notifyObservers();
    }


}

