package base;

/**
 * Implements an abstract Window used for drawing things to the screen.
 *
 * @author John Gauci
 */
public abstract class Window {

    public static final int COLOR_BLACK = 2;
    public static final int COLOR_WHITE = 1;
    public static final int COLOR_GREEN = 5;

    public static final int COLOR_RED = 3;

    public static final int COLOR_BLUE = 9;
    public static final int COLOR_YELLOW = 7;
    public static final int COLOR_GRAY = 15;


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
     * @param alpha
     * @author John Gauci
     */
    public abstract void drawRectangle(int row, int column, int width, int height,
                                       int borderColor, int fillColor, int alpha);

    /**
     * Draws a character to the screen with the given attributes.
     *
     * @param row       row of character
     * @param column    column of character
     * @param text      text to be drawn
     * @param width     width of character
     * @param height    height of character
     * @param textColor text color of character
     * @param backColor background color of character
     * @param italic    the italics of the character
     * @param bold      the boldness of the character
     * @param fontName  the name of the font of the character
     * @author John Gauci
     */
    public abstract void drawText(int row, int column, String text, int width, int height,
                                  int textColor, int backColor, int italic, int bold,
                                  String fontName);

    /**
     * Draws a picture to the window
     *
     * @param row        the row of the picture
     * @param column     the column of the picture
     * @param width      the width of the picture
     * @param height     the height of the picture
     * @param imageIndex the index of the file for the picture
     * @author Kareem Khalidi
     */
    public abstract void drawPicture(int row, int column, int width, int height, int imageIndex);


}
