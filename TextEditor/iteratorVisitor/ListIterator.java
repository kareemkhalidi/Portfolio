package iteratorVisitor;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Implements an iterator to iterate over a Java List.
 *
 * @param <E> the iterator return type
 * @author John Gauci
 */
public class ListIterator<E> extends Iterator<E> {

    private final List<E> array;
    private int index;

    /**
     * Initializes an iterator from the given List.
     *
     * @param array the list to iterate over
     * @author John Gauci
     */
    public ListIterator(List<E> array) {
        this.array = array;
        index = 0;
    }

    /**
     * Checks to see if the iterator has a next element
     *
     * @return has next object
     * @author John Gauci
     * @author Kareem Khalidi
     */
    @Override
    public boolean hasNext() {
        return index < array.size();
    }

    /**
     * Returns the next element in the iterator
     *
     * @return next element
     * @author John Gauci
     * @author Kareem Khalidi
     */
    @Override
    public E next() throws NoSuchElementException {
        if (hasNext()) {
            E element = array.get(index);
            index++;
            return element;
        } else
            throw new NoSuchElementException();
    }
}
