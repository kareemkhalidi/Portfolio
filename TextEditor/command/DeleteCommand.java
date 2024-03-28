package command;

import composite.Glyph;
import mvc.DocumentModel;

/**
 * A command that deletes a glyph when executed and reinserts
 * it when unExecuted
 *
 * @author John Gauci
 * @author Kareem Khalidi
 */
public class DeleteCommand extends Command {

    private final Glyph glyph;
    private final int index;
    private final DocumentModel documentModel;

    /**
     * Constructor for a new delete command
     *
     * @param glyph         the glyph to delete
     * @param index         the index that the glyph is deleted from
     * @param documentModel the document model that the glyph is deleted from
     * @author John Gauci
     * @author Kareem Khalidi
     */
    public DeleteCommand(Glyph glyph, int index, DocumentModel documentModel) {
        this.glyph = glyph;
        this.index = index;
        this.documentModel = documentModel;
    }

    /**
     * Deletes the glyph from the document model
     *
     * @author John Gauci
     * @author Kareem Khalidi
     */
    public void execute() {
        documentModel.moveCursor(index);
        documentModel.removeGlyph();
    }

    /**
     * Reinserts the glyph to the document model
     *
     * @author John Gauci
     * @author Kareem Khalidi
     */
    public void unExecute() {
        documentModel.moveCursor(index - 1);
        documentModel.addGlyph(glyph);
    }

}
