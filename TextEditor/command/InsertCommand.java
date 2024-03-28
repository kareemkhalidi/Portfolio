package command;

import composite.Glyph;
import mvc.DocumentModel;

/**
 * A command that inserts a glyph when executed and
 * deletes the glyph when unExecuted
 *
 * @author John Gauci
 * @author Kareem Khalidi
 */
public class InsertCommand extends Command {

    private final Glyph glyph;
    private final int index;
    private final DocumentModel documentModel;

    /**
     * Constructor for a new InsertCommand object
     *
     * @param glyph         the glyph to insert
     * @param index         the index to insert the glyph at
     * @param documentModel the document model to insert the glyph into
     * @author John Gauci
     * @author Kareem Khalidi
     */
    public InsertCommand(Glyph glyph, int index, DocumentModel documentModel) {
        this.glyph = glyph;
        this.index = index;
        this.documentModel = documentModel;

    }

    /**
     * Inserts the glyph into the document model
     *
     * @author John Gauci
     * @author Kareem Khalidi
     */
    public void execute() {
        documentModel.moveCursor(index);
        documentModel.addGlyph(glyph);
    }

    /**
     * Deletes the glyph from the document model
     *
     * @author John Gauci
     * @author Kareem Khalidi
     */
    public void unExecute() {
        documentModel.moveCursor(index + 1);
        documentModel.removeGlyph();
    }

}
