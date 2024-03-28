package base;

/**
 * Implements an abstract Window used for drawing things to the screen.
 *
 * @author John Gauci
 */
public abstract class Window {

    public final static int COLOR_BLACK = 2;
    public final static int COLOR_WHITE = 1;
    public final static int COLOR_GREEN = 5;

    public final static int COLOR_RED = 3;

    public final static int COLOR_BLUE = 9;


    /**
     * Draws a character to the screen with the given attributes.
     *
     * @param row       row of character
     * @param column    column of character
     * @param character character to be drawn
     * @param width     width of character
     * @param height    height of character
     * @param textColor text color of character
     * @param backColor background color of character
     * @param italic    the italics of the character
     * @param bold      the boldness of the character
     * @param fontName  the name of the font of the character
     * @author John Gauci
     */
    public abstract void drawCharacter(int row, int column, char character, int width, int height,
                                       int textColor, int backColor, int italic, int bold,
                                       String fontName);

    /**
     * Draws a rectangle to the screen with the given attributes.
     *
     * @param row         the row of the rectangle
     * @param column      the column of the rectangle
     * @param width       the width of the rectangle
     * @param height      the height of the rectangle
     * @param borderColor the border color of the rectangle
     * @param fillColor   the fill color of the rectangle
     * @author John Gauci
     */
    public abstract void drawRectangle(int row, int column, int width, int height,
                                       int borderColor, int fillColor);

    public abstract void drawPicture(int row, int column, int width, int height, String imageFile);


}
