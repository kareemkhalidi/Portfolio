package composite;

import iteratorVisitor.GlyphVisitor;

/**
 * Implements a cursor class for a document.
 *
 * @author John Gauci
 */
public class Cursor extends Character {

    /**
     * Initializes a cursor with the given attributes.
     *
     * @param width     the width of the cursor
     * @param height    the height of the cursor
     * @param textColor the text color of the cursor
     * @param backColor the background color of the cursor
     * @param character the character of the cursor
     * @param bold      the bold setting of the cursor
     * @param italic    the italic setting of the cursor
     * @param fontName  the font name of the cursor
     * @author John Gauci
     */
    public Cursor(int width, int height, int textColor, int backColor, char character, int bold, int italic, String fontName) {
        super(width, height, textColor, backColor, character, bold, italic, fontName);
    }

    /**
     * Visits the cursors visitor
     *
     * @param visitor the visitor to visit
     * @author John Gauci
     */
    @Override
    public void accept(GlyphVisitor visitor) {
        visitor.visitCursor(this);
    }

}
