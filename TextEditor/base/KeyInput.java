package base;

/**
 * Implements an abstract KeyInput class used to provide information about a key input.
 *
 * @author John Gauci
 */

public abstract class KeyInput {


    public static final int KEYCODE_BIT = 1 << 24;
    public static final int ARROW_LEFT = KEYCODE_BIT + 3;
    public static final int ARROW_RIGHT = KEYCODE_BIT + 4;

    public static final int HOME = KEYCODE_BIT + 7;

    public static final int END = KEYCODE_BIT + 8;

    private int keycode;
    private char character;

    /**
     * Returns the keycode of this KeyInput.
     *
     * @return keycode
     * @author John Gauci
     */
    public int getKeycode() {
        return keycode;
    }

    /**
     * Sets the keycode to the given keycode.
     *
     * @param keycode the keycode to be set to.
     * @author John Gauci
     */
    public void setKeycode(int keycode) {
        this.keycode = keycode;
    }

    /**
     * Returns the character of this KeyInput.
     *
     * @return character
     * @author John Gauci
     */
    public char getCharacter() {
        return character;
    }

    /**
     * Sets the character to the given character.
     *
     * @param character the character to be set to.
     * @author John Gauci
     */
    public void setCharacter(char character) {
        this.character = character;
    }


}
