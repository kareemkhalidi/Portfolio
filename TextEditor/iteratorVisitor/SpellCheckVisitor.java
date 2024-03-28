package iteratorVisitor;

import composite.Character;
import composite.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

/**
 * Spell Check Visitor class
 *
 * @author John Gauci
 * @author Kareem Khalidi
 */
public class SpellCheckVisitor extends GlyphVisitor {

    private final List<String> misSpelledWords;
    private final HashSet<String> dictionary;
    private StringBuilder currentWord;

    /**
     * Constructor for the SpellCheckVisitor class
     *
     * @author John Gauci
     */
    public SpellCheckVisitor() {
        currentWord = new StringBuilder();
        misSpelledWords = new ArrayList<>();
        dictionary = new HashSet<>();
        try (Scanner scanner = new Scanner(new File("dictionary.txt"))) {
            while (scanner.hasNext()) {
                dictionary.add(scanner.next().toLowerCase());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Calls terminateWord to ensure that all words in the document were spell-checked and then
     * clears and returns the list of all misspelled words in the document
     *
     * @return list of all misspelled words in the document
     * @author Kareem Khalidi
     */
    public List<String> getMisSpelledWords() {

        terminateWord();
        List<String> temp = new ArrayList<>(misSpelledWords);
        misSpelledWords.clear();
        return temp;

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
            currentWord.append(currentChar);
        } else {
            String word = currentWord.toString().toLowerCase();
            currentWord = new StringBuilder();
            if (!dictionary.contains(word) && word.length() > 0)
                misSpelledWords.add(word);
        }
    }

    /**
     * Runs the spell checker one last time on the final captured word
     * in case the visitCharacter method did not reach it
     *
     * @author Kareem Khalidi
     */
    private void terminateWord() {

        String word = currentWord.toString().toLowerCase();
        currentWord = new StringBuilder();
        if (!dictionary.contains(word) && word.length() > 0) // error
            misSpelledWords.add(word);

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
        Iterator<Glyph> iterator = composite.iterator();
        while (iterator.hasNext()) {
            iterator.next().accept(this);
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

