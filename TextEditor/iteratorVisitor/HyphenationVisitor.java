package iteratorVisitor;

import composite.Character;
import composite.*;
import mvc.DocumentModel;

import java.util.ArrayList;

/**
 * Implements a visitor to add hyphens at the ends of wrapped words.
 *
 * @author John Gauci
 */

public class HyphenationVisitor extends GlyphVisitor {

    private final ArrayList<Character> currentCharacters;

    private final DocumentModel documentModel;

    /**
     * Initializes a hyphenation visitor with the given documentModel.
     *
     * @param documentModel the documentModel of the visited glyphs.
     */
    public HyphenationVisitor(DocumentModel documentModel) {
        currentCharacters = new ArrayList<>();
        this.documentModel = documentModel;
    }

    /**
     * Visits a glyph character
     *
     * @param character the character to be visited
     * @author John Gauci
     */
    @Override
    public void visitCharacter(Character character) {
        char currentChar = character.getCharacter();
        if (java.lang.Character.isLetterOrDigit(currentChar)) {
            currentCharacters.add(character);
        } else {
            if (!currentCharacters.isEmpty()) {
                int firstRow = currentCharacters.get(0).getRow();
                for (Character currentCharacter : currentCharacters) {
                    if (currentCharacter.getRow() != firstRow) {
                        Iterator<Glyph> columnIterator = documentModel.getState().getComposite().iterator();
                        while (columnIterator.hasNext()) {
                            Glyph currentRow = columnIterator.next();
                            Iterator<Glyph> rowIterator = currentRow.iterator();
                            Glyph glyph = null;
                            while (rowIterator.hasNext()) {
                                glyph = rowIterator.next();
                            }
                            if (glyph != null && glyph.getRow() == firstRow) {
                                Hyphen hyphen =
                                        new Hyphen(documentModel.getState().getFontWidth(),
                                                documentModel.getState().getFontHeight(), documentModel.getState().getFontColor(),
                                                documentModel.getState().getBackGroundColor(), '-',
                                                documentModel.getState().getBold(), documentModel.getState().getItalic(),
                                                documentModel.getState().getFontName());
                                hyphen.setRow(firstRow);
                                hyphen.setColumn(currentRow.get(currentRow.size() - 1).getColumn() + hyphen.getWidth());
                                currentRow.add(currentRow.size(), hyphen);
                            }
                        }
                        break;
                    }
                }
            }
            currentCharacters.clear();
        }
    }

    /**
     * Visits a glyph rectangle
     *
     * @param rectangle the rectangle to be visited
     * @author John Gauci
     */
    @Override
    public void visitRectangle(Rectangle rectangle) {

    }

    /**
     * Visits a glyph composite
     *
     * @param composite the composite to be visited
     * @author John Gauci
     */
    @Override
    public void visitComposite(Composite composite) {
        Iterator<Glyph> compositeIterator = composite.iterator();
        while (compositeIterator.hasNext()) {
            compositeIterator.next().accept(this);
        }
    }

    /**
     * Visits a glyph cursor
     *
     * @param cursor the cursor to be visited
     * @author John Gauci
     */
    @Override
    public void visitCursor(Cursor cursor) {

    }

    /**
     * Visits a glyph hyphen
     *
     * @param hyphen the hyphen to be visited
     * @author John Gauci
     */
    @Override
    public void visitHyphen(Hyphen hyphen) {

    }


}
