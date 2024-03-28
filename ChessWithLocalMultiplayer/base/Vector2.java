package base;

import java.io.Serializable;

/**
 * Implementation of a two-integer vector.
 *
 * @author John Gauci
 */

public class Vector2 implements Serializable {
    private int x, y;

    /**
     * Constructs a vector with the given x and y values.
     *
     * @param x x value
     * @param y y value
     * @author John Gauci
     */
    public Vector2(final int x, final int y) {
        super();
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the x value of the vector
     *
     * @return x value
     * @author John Gauci
     */
    public int x() {
        return this.x;
    }

    /**
     * Sets the x value of the vector to the given value
     *
     * @param x x value to be set to
     * @author John Gauci
     */
    public void x(final int x) {
        this.x = x;
    }

    /**
     * Gets the y value of the vector
     *
     * @return y value
     * @author John Gauci
     */
    public int y() {
        return this.y;
    }

    /**
     * Sets the y value of the vector to the given value
     *
     * @param y y value to be set to
     * @author John Gauci
     */
    public void y(final int y) {
        this.y = y;
    }
    
    /**
     * Converts a 2D vector representing a chess board position into a string representing the same position.
     * <p>
     * Example: vectorToPositionString(new Vector2(0, 0)) -> a1
     *
     * @return the position string
     * @author Alexander Cooper
     */
    public String vectorToPositionString() {
        final int row = this.y() + 1;
        final int colAsciiCode = 'a' + this.x();
        final char col = (char) colAsciiCode;

        return String.valueOf(col) + row;
    }
    
    /**
     * Converts a position string to a vector.
     * <p>
     * Example: positionStringToVector("b3") -> Vector2(1, 2)
     *
     * @return the vector
     * @author Alexander Cooper
     */
    public static Vector2 positionStringToVector(final String position) {
        final char col = position.charAt(0);
        final char row = position.charAt(1);

        final int colNum = col - 'a';
        final int rowNum = Character.getNumericValue(row) - 1;

        return new Vector2(colNum, rowNum);
    }

    /**
     * Returns whether two vectors are equal
     *
     * @param obj the vector to compare to
     * @return whether the two vectors are equal
     * @author John Gauci (IDE generated)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Vector2 vector2)) return false;

        if (this.x != vector2.x) return false;
        return this.y == vector2.y;
    }


    /**
     * Returns a hashcode for a vector
     *
     * @return hashcode for a vector
     * @author John Gauci (IDE generated)
     */
    @Override
    public int hashCode() {
        int result = this.x;
        result = 31 * result + this.y;
        return result;
    }
}
