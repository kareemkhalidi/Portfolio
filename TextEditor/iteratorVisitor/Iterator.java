package iteratorVisitor;

import java.util.NoSuchElementException;

/**
 * Abstract generic iterator
 *
 * @author John Gauci
 * @author Kareem Khalidi
 */
public abstract class Iterator<E> {

    /**
     * Checks to see if the iterator has a next element
     *
     * @return has next object
     * @author John Gauci
     * @author Kareem Khalidi
     */
    public abstract boolean hasNext();

    /**
     * Returns the next element in the iterator
     *
     * @return next element
     * @author John Gauci
     * @author Kareem Khalidi
     */
    public abstract E next() throws NoSuchElementException;

}
