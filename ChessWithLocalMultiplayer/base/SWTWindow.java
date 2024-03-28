package base;

import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the Window for the SWT UI toolkit.
 *
 * @author John Gauci
 */

public class SWTWindow extends Window {

    private static final List<Image> imageCache = new ArrayList<>(30);
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
    private SWTWindow(final PaintEvent event, final int rowOffset, final int columnOffset) {
        super();
        this.event = event;
        this.rowOffset = rowOffset;
        this.columnOffset = columnOffset;
    }

    public SWTWindow(final PaintEvent event) {
        this(event, 0, 0);
    }

    public static void addImage(final String fileName) {
        SWTWindow.imageCache.add(new Image(Display.getCurrent(), fileName));
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
    public void drawCharacter(final int row, final int column, final char character, final int width, final int height, final int textColor, final int backColor, final int italic, final int bold, final String fontName) {
        this.event.gc.setBackground(this.event.display.getSystemColor(backColor));
        this.event.gc.setForeground(this.event.display.getSystemColor(textColor));
        final Font font = new Font(this.event.display, fontName, width, italic | bold);
        this.event.gc.setFont(font);
        this.event.gc.drawString(String.valueOf(character), column + this.columnOffset, row + this.rowOffset);
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
    public void drawRectangle(final int row, final int column, final int width, final int height, final int borderColor, final int fillColor, final int alpha) {
        this.event.gc.setForeground(this.event.display.getSystemColor(fillColor));
        this.event.gc.setAlpha(alpha);
        this.event.gc.drawRectangle(column + this.columnOffset, row + this.rowOffset, width, height);
        this.event.gc.setBackground(this.event.display.getSystemColor(borderColor));
        this.event.gc.fillRectangle(column + this.columnOffset, row + this.rowOffset, width, height);
    }

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
    @Override
    public void drawText(final int row, final int column, final String text, final int width, final int height, final int textColor, final int backColor, final int italic, final int bold, final String fontName) {
        this.event.gc.setBackground(this.event.display.getSystemColor(backColor));
        this.event.gc.setForeground(this.event.display.getSystemColor(textColor));
        final Font font = new Font(this.event.display, fontName, width, italic | bold);
        this.event.gc.setFont(font);
        this.event.gc.drawString(text, column + this.columnOffset, row + this.rowOffset);
        font.dispose();
    }


    /**
     * Draws a picture to the window using SWT
     *
     * @param row        the row of the picture
     * @param column     the column of the picture
     * @param width      the width of the picture
     * @param height     the height of the picture
     * @param imageIndex the index of the file for the picture
     * @author Kareem Khalidi
     */
    @Override
    public void drawPicture(final int row, final int column, final int width, final int height, final int imageIndex) {
        this.event.gc.drawImage(SWTWindow.imageCache.get(imageIndex), column + width / 4, row + height / 4);
    }


}
