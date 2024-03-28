package base;

/**
 * Implements an abstract KeyInput class used to provide information about a key input.
 *
 * @author John Gauci
 */

public abstract class KeyInput {


    public static final int ESCAPE = 0x1B;
    private static final int KEYCODE_BIT = 1 << 24;
    public static final int ARROW_UP = KeyInput.KEYCODE_BIT + 1;
    public static final int ARROW_DOWN = KeyInput.KEYCODE_BIT + 2;
    public static final int ARROW_LEFT = KeyInput.KEYCODE_BIT + 3;
    public static final int ARROW_RIGHT = KeyInput.KEYCODE_BIT + 4;
    public static final int HOME = KeyInput.KEYCODE_BIT + 7;
    public static final int END = KeyInput.KEYCODE_BIT + 8;

    private final int keycode;
    private final char character;

    protected KeyInput(final int keycode, final char character) {
        super();
        this.keycode = keycode;
        this.character = character;
    }

    /**
     * Returns the keycode of this KeyInput.
     *
     * @return keycode
     * @author John Gauci
     */
    public int getKeycode() {
        return this.keycode;
    }


    /**
     * Returns the character of this KeyInput.
     *
     * @return character
     * @author John Gauci
     */
    public char getCharacter() {
        return this.character;
    }


}
