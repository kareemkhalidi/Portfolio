package composite;

import mvc.DocumentView;

/**
 * Implements a line return to be used in a document.
 *
 * @author John Gauci
 */

public class Return extends Character {

    /**
     * Initializes a return with the given attributes.
     *
     * @param height    the height of the cursor
     * @param textColor the text color of the return
     * @param backColor the background color of the return
     * @param character the character of the return
     * @param bold      the bold setting of the return
     * @param italic    the italic setting of the return
     * @param fontName  the font name of the return
     * @author John Gauci
     */
    public Return(int height, int textColor, int backColor, char character, int bold, int italic, String fontName) {
        super(DocumentView.CANVAS_WIDTH, height, textColor, backColor, character, bold, italic, fontName);
    }

    /**
     * Returns the width of the return to the document canvas width.
     *
     * @return the width of the return
     * @author John Gauci
     */
    @Override
    public int getWidth() {
        return DocumentView.CANVAS_WIDTH;
    }


}
