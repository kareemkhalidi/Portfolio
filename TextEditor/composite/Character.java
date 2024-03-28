package composite;

import base.Window;
import iteratorVisitor.GlyphVisitor;
import iteratorVisitor.Iterator;
import iteratorVisitor.SingleIterator;

/**
 * Implements a character for viewing in a document.
 *
 * @author John Gauci
 */
public class Character extends Glyph {

    private final char character;
    private final String fontName;

    private final int bold;
    private final int italic;

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
    public Character(int width, int height, int textColor, int backColor, char character, int bold, int italic, String fontName) {
        super(width, height, textColor, backColor);
        this.character = character;
        this.fontName = fontName;
        this.bold = bold;
        this.italic = italic;
    }

    /**
     * Draws the character to the window.
     *
     * @param window the window to draw with
     * @author John Gauci
     */
    @Override
    public void draw(Window window) {
        window.drawCharacter(getRow(), getColumn(), getCharacter(), getWidth(), getHeight(),
                getPrimaryColor(), getSecondaryColor(), getBold(), getItalic(), getFontName());
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
     * Unused.
     *
     * @param child index of the glyph to be returned
     * @return glyph
     * @author John Gauci
     */
    @Override
    public Glyph get(int child) {
        return null;
    }

    /**
     * Returns the size of the Character
     *
     * @return size (1)
     * @author John Gauci
     */
    @Override
    public int size() {
        return 1;
    }

    /**
     * Returns a new iterator for the Character
     *
     * @return iterator
     * @author John Gauci
     */
    @Override
    public Iterator<Glyph> iterator() {
        return new SingleIterator<>(this);
    }

    /**
     * Visits the Characters visitor
     *
     * @param visitor the GlyphVisitor to visit
     * @author John Gauci
     */
    @Override
    public void accept(GlyphVisitor visitor) {
        visitor.visitCharacter(this);
    }

    /**
     * Gets the character.
     *
     * @return the character
     * @author John Gauci
     */
    public char getCharacter() {
        return character;
    }

    /**
     * Gets the font name of the character.
     *
     * @return the font name
     * @author John Gauci
     */
    public String getFontName() {
        return fontName;
    }

    /**
     * Gets the bold setting of the character.
     *
     * @return the bold setting
     * @author John Gauci
     */
    public int getBold() {
        return bold;
    }

    /**
     * Gets the italic setting of the character.
     *
     * @return the italic setting
     * @author John Gauci
     */
    public int getItalic() {
        return italic;
    }


}
