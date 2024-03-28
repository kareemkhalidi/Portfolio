package base;

import org.eclipse.swt.events.KeyEvent;


/**
 * Class for an SWT Key Input
 *
 * @author John Gauci
 */
public class SWTKeyInput extends KeyInput {

    /**
     * Constructor for an SWT Key Input
     *
     * @param keyEvent the key input event
     * @author John Gauci
     */
    public SWTKeyInput(final KeyEvent keyEvent) {
        super(keyEvent.keyCode, keyEvent.character);
    }

}
