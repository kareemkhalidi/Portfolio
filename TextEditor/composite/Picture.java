package composite;

import base.Window;

/**
 * Picture Glyph class
 *
 * @author John Gauci
 * @author Kareem Khalidi
 */
public class Picture extends Rectangle {

    private final String imageFile;

    /**
     * Constructor for a new picture object
     *
     * @param width          the width of the picture
     * @param height         the height of the picture
     * @param primaryColor   the primary color of the picture
     * @param secondaryColor the secondary color of the picture
     * @param imageFile      the name of the file for the picture
     * @author John Gauci
     * @author Kareem Khalidi
     */
    public Picture(int width, int height, int primaryColor, int secondaryColor, String imageFile) {
        super(width, height, primaryColor, secondaryColor);
        this.imageFile = imageFile;
    }

    /**
     * Draws the picture
     *
     * @param window the window to draw in
     * @author Kareem Khalidi
     */
    @Override
    public void draw(Window window) {

        window.drawPicture(getRow(), getColumn(), getWidth(), getHeight(), imageFile);

    }

}
