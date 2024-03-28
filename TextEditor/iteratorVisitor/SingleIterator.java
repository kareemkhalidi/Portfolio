package iteratorVisitor;

import java.util.NoSuchElementException;

/**
 * Implements an iterator to iterate over a single element.
 *
 * @param <E> the iterator return type
 * @author John Gauci
 */
public class SingleIterator<E> extends Iterator<E> {

    private final E element;
    private boolean empty;

    /**
     * Initializes an iterator from the given element.
     *
     * @param element the element to iterate over
     * @author John Gauci
     */
    public SingleIterator(E element) {
        this.element = element;
        empty = false;
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
        return empty;
    }

    /**
     * Returns the next element in the iterator
     *
     * @return next element
     * @author John Gauci
     * @author Kareem Khalidi
     */
    @Override
    public E next() {
        if (hasNext()) {
            empty = true;
            return element;
        } else
            throw new NoSuchElementException();
    }
}
