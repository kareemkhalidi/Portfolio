package composite;

import iteratorVisitor.GlyphVisitor;

/**
 * Implements a hyphen character for use of adding hyphens to end of lines.
 *
 * @author John Gauci
 */
public class Hyphen extends Character {
    /**
     * Initializes a character with the given attributes.
     *
     * @param width     the width of the character
     * @param height    the height of the character
     * @param textColor the text color of the character
     * @param backColor the background color of the character
     * @param character the character
     * @param bold      the bold setting of the character
     * @param italic    the italic setting of the character
     * @param fontName  the font name of the character
     * @author John Gauci
     */
    public Hyphen(int width, int height, int textColor, int backColor, char character, int bold, int italic, String fontName) {
        super(width, height, textColor, backColor, character, bold, italic, fontName);
    }


    /**
     * Visits the glyph visitor of this glyph
     *
     * @param visitor the GlyphVisitor to visit
     * @author John Gauci
     */
    @Override
    public void accept(GlyphVisitor visitor) {
        visitor.visitHyphen(this);
    }
}
