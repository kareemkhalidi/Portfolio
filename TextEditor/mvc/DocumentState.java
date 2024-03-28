package mvc;

import base.State;
import composite.Composite;
import composite.Glyph;
import decorator.ScrollDecorator;

import java.util.List;

/**
 * Implements a DocumentState containing attributes of a DocumentModel.
 *
 * @author John Gauci
 */

public class DocumentState extends State {

    private Composite composite;
    private ScrollDecorator scrollDecorator;
    private int bold;
    private int italic;
    private char cursorCharacter;
    private String fontName;
    private int fontColor;
    private int fontWidth;
    private int fontHeight;
    private int backGroundColor;
    private int scrollPosition;
    private int cursorIndex;
    private List<Glyph> allGlyphs;

    public List<Glyph> getAllGlyphs() {
        return allGlyphs;
    }

    public void setAllGlyphs(List<Glyph> allGlyphs) {
        this.allGlyphs = allGlyphs;
    }

    /**
     * Gets the index of the cursor.
     *
     * @return cursor index
     * @author John Gauci
     */
    public int getCursorIndex() {
        return cursorIndex;
    }

    /**
     * Sets the index for the cursor.
     *
     * @param cursorIndex the index to set to
     * @author John Gauci
     */
    public void setCursorIndex(int cursorIndex) {
        this.cursorIndex = cursorIndex;
    }

    /**
     * Gets the bold setting of the document.
     *
     * @return the bold setting of the document
     * @author John Gauci
     */
    public int getBold() {
        return bold;
    }

    /**
     * Sets the bold setting of the document.
     *
     * @param bold the bold setting to set to
     * @author John Gauci
     */
    public void setBold(int bold) {
        this.bold = bold;
    }

    /**
     * Gets the italic setting of the document
     *
     * @return the italic setting of the document
     * @author John Gauci
     */
    public int getItalic() {
        return italic;
    }

    /**
     * Sets the italic setting of the document.
     *
     * @param italic the italic setting to set to
     * @author John Gauci
     */
    public void setItalic(int italic) {
        this.italic = italic;
    }

    /**
     * Gets the cursor character of this document.
     *
     * @return the cursor character
     * @author John Gauci
     */
    public char getCursorCharacter() {
        return cursorCharacter;
    }

    /**
     * Sets the cursor character for this document.
     *
     * @param cursorCharacter the cursor character to set to
     * @author John Gauci
     */
    public void setCursorCharacter(char cursorCharacter) {
        this.cursorCharacter = cursorCharacter;
    }

    /**
     * Gets the font height for this document.
     *
     * @return the font height
     * @author John Gauci
     */
    public int getFontHeight() {
        return fontHeight;
    }

    /**
     * Sets the font height for this document.
     *
     * @param fontHeight the font height to be set to
     * @author John Gauci
     */
    public void setFontHeight(int fontHeight) {
        this.fontHeight = fontHeight;
    }

    /**
     * Gets the background color of this document.
     *
     * @return background color
     * @author John Gauci
     */
    public int getBackGroundColor() {
        return backGroundColor;
    }

    /**
     * Sets the background color for this document.
     *
     * @param backGroundColor the background color to set to
     * @author John Gauci
     */
    public void setBackGroundColor(int backGroundColor) {
        this.backGroundColor = backGroundColor;
    }

    /**
     * Gets the font width of this document.
     *
     * @return the font width
     * @author John Gauci
     */
    public int getFontWidth() {
        return fontWidth;
    }

    /**
     * Sets the font width for this document.
     *
     * @param fontWidth the font width to set to
     * @author John Gauci
     */
    public void setFontWidth(int fontWidth) {
        this.fontWidth = fontWidth;
    }


    /**
     * Gets the font name of this document.
     *
     * @return the font name
     * @author John Gauci
     */
    public String getFontName() {
        return fontName;
    }

    /**
     * Sets the font name for this document.
     *
     * @param fontName the font name to set to
     * @author John Gauci
     */
    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    /**
     * Gets the font color of this document.
     *
     * @return the font color
     * @author John Gauci
     */
    public int getFontColor() {
        return fontColor;
    }

    /**
     * Sets the font color for this document.
     *
     * @param fontColor the font color to set to
     * @author John Gauci
     */
    public void setFontColor(int fontColor) {
        this.fontColor = fontColor;
    }

    /**
     * Returns the composite of the document.
     *
     * @return the composite
     * @author John Gauci
     */
    public Composite getComposite() {
        return composite;
    }

    /**
     * Sets the composite for the document.
     *
     * @param composite the composite to set to
     * @author John Gauci
     */
    public void setComposite(Composite composite) {
        this.composite = composite;
    }

    /**
     * Gets the scroll position of the document.
     *
     * @return the scroll position
     * @author John Gauci
     */
    public int getScrollPosition() {
        return scrollPosition;
    }

    /**
     * Sets the scroll position for the document.
     *
     * @param scrollPosition the scroll position to set to
     * @author John Gauci
     */
    public void setScrollPosition(int scrollPosition) {
        this.scrollPosition = scrollPosition;
    }

    /**
     * Gets the scroll decorator for the document
     *
     * @return the scroll decorator
     * @author John Gauci
     */
    public ScrollDecorator getScrollDecorator() {
        return scrollDecorator;
    }

    /**
     * Sets the scroll decorator for the document.
     *
     * @param scrollDecorator the scroll decorator to set to
     * @author John Gauci
     */
    public void setScrollDecorator(ScrollDecorator scrollDecorator) {
        this.scrollDecorator = scrollDecorator;
    }

}
