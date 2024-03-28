package mvc;

import base.KeyInput;
import org.eclipse.swt.events.KeyEvent;

/**
 * Class for an SWT Key Input
 *
 * @author John Gauci
 */
public class SWTKeyInput extends KeyInput {

    private final KeyEvent keyEvent;

    /**
     * Constructor for an SWT Key Input
     *
     * @param keyEvent the key input event
     * @author John Gauci
     */
    public SWTKeyInput(KeyEvent keyEvent) {
        this.keyEvent = keyEvent;
        setCharacter(keyEvent.character);
        setKeycode(keyEvent.keyCode);
    }

}
