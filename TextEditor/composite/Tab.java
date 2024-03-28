package composite;

/**
 * Implements a tab for use in a document.
 *
 * @author John Gauci
 */
public class Tab extends Character {

    /**
     * Initializes a tab with the given attributes.
     *
     * @param width     the width of the tab
     * @param height    the height of the tab
     * @param textColor the text color of the tab
     * @param backColor the background color of the tab
     * @param character the character of the tab
     * @param bold      the bold setting of the tab
     * @param italic    the italic setting of the tab
     * @param fontName  the font name of the tab
     * @author John Gauci
     */
    public Tab(int width, int height, int textColor, int backColor, char character, int bold, int italic, String fontName) {
        super(width * 4, height, textColor, backColor, character, bold, italic, fontName);
    }

}
