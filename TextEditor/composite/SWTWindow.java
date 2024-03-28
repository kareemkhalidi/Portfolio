package composite;

import base.Window;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

/**
 * Implements the Window for the SWT UI toolkit.
 *
 * @author John Gauci
 */

public class SWTWindow extends Window {

    private final PaintEvent event;
    private final int rowOffset;
    private final int columnOffset;

    /**
     * Initializes the window with the given attributes.
     *
     * @param event        the PaintEvent for the window
     * @param rowOffset    the row drawing offset
     * @param columnOffset the column drawing offset
     */
    public SWTWindow(PaintEvent event, int rowOffset, int columnOffset) {
        this.event = event;
        this.rowOffset = rowOffset;
        this.columnOffset = columnOffset;
    }

    /**
     * Draws a character to the window using SWT.
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
    @Override
    public void drawCharacter(int row, int column, char character, int width, int height, int textColor, int backColor, int italic, int bold, String fontName) {
        event.gc.setBackground(event.display.getSystemColor(backColor));
        event.gc.setForeground(event.display.getSystemColor(textColor));
        Font font = new Font(event.display, fontName, width, italic | bold);
        event.gc.setFont(font);
        event.gc.drawString("" + character, column + columnOffset, row + rowOffset);
        font.dispose();
    }

    /**
     * Draws a rectangle to the window using SWT.
     *
     * @param row         the row of the rectangle
     * @param column      the column of the rectangle
     * @param width       the width of the rectangle
     * @param height      the height of the rectangle
     * @param borderColor the border color of the rectangle
     * @param fillColor   the fill color of the rectangle
     * @author John Gauci
     */
    @Override
    public void drawRectangle(int row, int column, int width, int height, int borderColor, int fillColor) {
        event.gc.setForeground(event.display.getSystemColor(fillColor));
        event.gc.drawRectangle(column + columnOffset, row + rowOffset, width, height);
        event.gc.setBackground(event.display.getSystemColor(borderColor));
        event.gc.fillRectangle(column + columnOffset, row + rowOffset, width, height);
    }

    /**
     * Draws a picture to the window using SWT
     *
     * @param row       the row of the picture
     * @param column    the column of the picture
     * @param width     the width of the picture
     * @param height    the height of the picture
     * @param imageFile the name of the file for the picture
     * @author Kareem Khalidi
     */
    @Override
    public void drawPicture(int row, int column, int width, int height, String imageFile) {

        event.gc.drawImage(new Image(event.display, imageFile), column + width / 4,
                row + height / 4);

    }


}
