package base;

import org.eclipse.swt.events.MouseEvent;

/**
 * Class for an SWT Mouse Input
 *
 * @author John Gauci
 */
public class SWTMouseInput extends MouseInput {


    /**
     * Constructor for an SWT Mouse Input
     *
     * @param mouseEvent the mouse input event
     * @author John Gauci
     */
    public SWTMouseInput(final MouseEvent mouseEvent) {
        super(mouseEvent.button, mouseEvent.y, mouseEvent.x);
    }

}