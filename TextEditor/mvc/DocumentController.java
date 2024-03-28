package mvc;

import base.KeyInput;
import base.Mediator;
import command.CommandList;
import command.DeleteCommand;
import command.InsertCommand;
import composite.Character;
import composite.Glyph;
import composite.Return;
import composite.Tab;
import iteratorVisitor.HyphenationVisitor;
import iteratorVisitor.Iterator;
import iteratorVisitor.SpellCheckVisitor;

import java.util.List;

/**
 * Implements a DocumentController for communication from the DocumentView to the DocumentModel.
 *
 * @author John Gauci
 */

public class DocumentController extends Mediator {

    private final CommandList commandList;
    private final SpellCheckVisitor spellCheckVisitor;


    /**
     * Initializes the controller with the given attribute.
     *
     * @param documentModel the DocumentModel to communicate to
     * @author John Gauci
     */
    public DocumentController(DocumentModel documentModel) {
        super(documentModel);
        commandList = new CommandList();
        spellCheckVisitor = new SpellCheckVisitor();
    }

    /**
     * Gets the subject of the controller
     *
     * @return the subject of the controller
     * @author John Gauci
     */
    @Override
    public DocumentModel getSubject() {
        return (DocumentModel) super.getSubject();
    }

    /**
     * Processes a given KeyInput and communicates to the DocumentModel accordingly.
     *
     * @param keyInput the keyInput sent to the controller
     * @author John Gauci
     */
    public void processKeyInput(KeyInput keyInput) {
        List<java.lang.Character> restrictedKeys = getSubject().getKeyRestriction();
        switch (keyInput.getKeycode()) {
            case KeyInput.ARROW_LEFT -> getSubject().moveCursorHorizontal(-1);
            case KeyInput.ARROW_RIGHT -> getSubject().moveCursorHorizontal(1);
            case KeyInput.HOME -> getSubject().moveCursorHome();
            case KeyInput.END -> getSubject().moveCursorEnd();
        }
        switch (keyInput.getCharacter()) {
            case (char) 8 ->
                    commandList.addAndExecuteCommand(new DeleteCommand(getSubject().leftOfCursor(), getSubject().getState().getCursorIndex(), getSubject()));
            case '\r' -> {
                Glyph glyph = new Return(getSubject().getState().getFontHeight(),
                        getSubject().getState().getFontColor(),
                        getSubject().getState().getBackGroundColor(), keyInput.getCharacter(),
                        getSubject().getState().getBold(),
                        getSubject().getState().getItalic(), getSubject().getState().getFontName());

                commandList.addAndExecuteCommand(new InsertCommand(glyph,
                        getSubject().getState().getCursorIndex(), getSubject()));
            }

            case (char) 9 -> {
                Glyph glyph = new Tab(getSubject().getState().getFontWidth(),
                        getSubject().getState().getFontHeight(), getSubject().getState().getFontColor(),
                        getSubject().getState().getBackGroundColor(), (char) 9,
                        getSubject().getState().getBold(), getSubject().getState().getItalic(),
                        getSubject().getState().getFontName());

                commandList.addAndExecuteCommand(new InsertCommand(glyph,
                        getSubject().getState().getCursorIndex(), getSubject()));
            }
            default -> {
                if (!restrictedKeys.contains(keyInput.getCharacter())) {
                    Glyph character = new Character(getSubject().getState().getFontWidth(),
                            getSubject().getState().getFontHeight(), getSubject().getState().getFontColor(),
                            getSubject().getState().getBackGroundColor(), keyInput.getCharacter(),
                            getSubject().getState().getBold(), getSubject().getState().getItalic(),
                            getSubject().getState().getFontName());
                    commandList.addAndExecuteCommand(new InsertCommand(character,
                            getSubject().getState().getCursorIndex(), getSubject()));

                }
            }
        }


    }


    /**
     * Process the scroll input and communicates to the controller accordingly.
     *
     * @param position the position of the scroll bar
     * @author John Gauci
     */
    public void processScrollInput(int position) {
        getSubject().addScroll(position);
    }

    public String runSpellCheck() {
        Iterator<Glyph> iterator = getSubject().getState().getComposite().iterator();
        while (iterator.hasNext()) {
            iterator.next().accept(spellCheckVisitor);
        }
        List<String> misspelledWords = spellCheckVisitor.getMisSpelledWords();
        StringBuilder result = new StringBuilder(misspelledWords.size() + " misspelled words found: ");
        misspelledWords.forEach(word -> result.append(word).append(", "));
        result.delete(result.length() - 2, result.length() - 1);
        return (result.toString());
    }

    public void runHyphenation() {
        HyphenationVisitor hyphenationVisitor = new HyphenationVisitor(getSubject());
        Iterator<Glyph> compositeIterator = getSubject().getState().getComposite().iterator();
        while (compositeIterator.hasNext()) {
            compositeIterator.next().accept(hyphenationVisitor);
        }
    }

    /**
     * Undoes the most recently performed command
     *
     * @author John Gauci
     */
    public void undo() {
        commandList.undo();
    }

    /**
     * Redoes the most recently undone command
     *
     * @author John Gauci
     */
    public void redo() {
        commandList.redo();
    }

    /**
     * Inserts a rectangle to the document
     *
     * @param width  width of the rectangle
     * @param height height of the rectangle
     * @param color  the color for the rectangle
     * @author Kareem Khalidi
     */
    public void insertRectangle(int width, int height, int color) {

        Glyph rectangle = new composite.Rectangle(width, height, color, color);
        commandList.addAndExecuteCommand(new InsertCommand(rectangle, getSubject().getState().getCursorIndex(), getSubject()));

    }

    /**
     * Inserts an image to the document
     *
     * @param img the name of the image file
     * @author Kareem Khalidi
     */
    public void insertImage(String img) {

        Glyph picture = new composite.Picture(100, 100, getSubject().getState().getFontColor(),
                getSubject().getState().getBackGroundColor(), img);
        commandList.addAndExecuteCommand(new InsertCommand(picture, getSubject().getState().getCursorIndex(), getSubject()));

    }

}
