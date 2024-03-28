package strategy;

import composite.Composite;
import composite.Glyph;
import mvc.DocumentState;
import mvc.DocumentView;

import java.util.List;

/**
 * The standard compositor for the document
 *
 * @author John Gauci
 */
public class StandardCompositor extends Compositor {

    /**
     * Composes the compositor
     *
     * @author John Gauci
     */
    @Override
    public void compose() {
        DocumentState documentState = getComposite().getDocumentState();
        List<Glyph> allGlyphs = documentState.getAllGlyphs();
        Composite currentRow = new Composite(this, getComposite().getDocumentState());
        getComposite().add(getComposite().size(), currentRow);
        int column = 0;
        int row = 0;
        for (int i = 0; i < getComposite().getDocumentState().getAllGlyphs().size(); i++) {
            Glyph child = allGlyphs.get(i);
            if (child.isVisible()) {
                if (column >= DocumentView.CANVAS_WIDTH - 100) {
                    row += currentRow.getHeight();
                    currentRow = new Composite(this, getComposite().getDocumentState());
                    getComposite().add(getComposite().size(), currentRow);
                    column = 0;
                }
                currentRow.add(currentRow.size(), child);
                child.setRow(row);
                child.setColumn(column);
                column += child.getWidth();
            }
        }
    }


}
