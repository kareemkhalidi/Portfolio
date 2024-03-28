package game;

import base.Vector2;
import base.Window;

import java.io.Serializable;
import java.util.Objects;

/**
 * Abstract entity class that all entity's (tanks, walls, bullets, etc) are based on.
 *
 * @author John Gauci
 */
public abstract class Entity implements Serializable {

    private int row;
    private int column;
    private int width;
    private int height;
    private int health;
    private int color;

    /**
     * Entity object constructor.
     *
     * @param row    the entities row
     * @param column the entities column
     * @param width  the entities width
     * @param height the entities height
     * @param health the entities health
     * @param color  the entities color
     * @author John Gauci
     */
    protected Entity(final int row, final int column, final int width, final int height, final int health, final int color) {
        super();
        this.row = row;
        this.column = column;
        this.width = width;
        this.height = height;
        this.health = health;
        this.color = color;
    }

    /**
     * Returns the entity's row to the user.
     *
     * @return the entity's row.
     * @author John Gauci
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Set the entity's row to the specified value.
     *
     * @param row the row to set the entity's row to.
     * @author John Gauci
     */
    public void setRow(final int row) {
        this.row = row;
    }

    /**
     * Returns the entity's column to the user.
     *
     * @return the entity's column.
     * @author John Gauci
     */
    public int getColumn() {
        return this.column;
    }

    /**
     * Set the entity's column to the specified value.
     *
     * @param column the column to set the entity's column to.
     * @author John Gauci
     */
    public void setColumn(final int column) {
        this.column = column;
    }

    /**
     * Returns the entity's width to the user.
     *
     * @return the entity's width.
     * @author John Gauci
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Set the entity's width to the specified value.
     *
     * @param width the width to set the entity's width to.
     * @author John Gauci
     */
    public void setWidth(final int width) {
        this.width = width;
    }

    /**
     * Returns the entity's height to the user.
     *
     * @return the entity's height.
     * @author John Gauci
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Set the entity's height to the specified value.
     *
     * @param height the height to set the entity's height to.
     * @author John Gauci
     */
    public void setHeight(final int height) {
        this.height = height;
    }

    /**
     * Returns the entity's health to the user.
     *
     * @return the entity's health.
     * @author John Gauci
     */
    public int getHealth() {
        return this.health;
    }

    /**
     * Set the entity's health to the specified value.
     *
     * @param health the health to set the entity's health to.
     * @author John Gauci
     */
    public void setHealth(final int health) {
        this.health = health;
    }

    /**
     * Returns the entity's color to the user.
     *
     * @return the entity's color.
     * @author John Gauci
     */
    public int getColor() {
        return this.color;
    }

    /**
     * Set the entity's color to the specified value.
     *
     * @param color the color to set the entity's color to.
     * @author John Gauci
     */
    public void setColor(final int color) {
        this.color = color;
    }


    /**
     * Abstract draw method that all entities must implement.
     *
     * @param window the window to draw the entity in
     * @author John Gauci
     */
    public abstract void draw(Window window);

    /**
     * Returns a boolean whether the given entity has isContacting this entity.
     *
     * @param other the entity that may have isContacting
     * @return boolean whether isContacting
     * @author Kareem Khalidi
     */
    public boolean isContacting(final Entity other) {
        final var upperLeft = new Vector2(this.column, this.row);
        final var lowerRight = new Vector2(this.column + this.width, this.row + this.height);

        final var otherUpperLeft = new Vector2(other.column, other.row);
        final var otherLowerRight = new Vector2(other.column + other.width, other.row + other.height);

        return (upperLeft.x() < otherLowerRight.x() && lowerRight.x() > otherUpperLeft.x() + 4 &&
                upperLeft.y() < otherLowerRight.y() && lowerRight.y() > otherUpperLeft.y() + 2);

    }

    /**
     * Equals method for an entity (checks if entity == Object o)
     *
     * @param o the object to compare this entity to.
     * @author John Gauci
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity entity)) return false;
        return this.row == entity.row && this.column == entity.column && this.width == entity.width && this.height == entity.height && this.health == entity.health && this.color == entity.color;
    }

    /**
     * Gets a hash for the entity
     *
     * @return integer hash for this entity
     * @author John Gauci
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.row, this.column, this.width, this.height,
                this.health, this.color);
    }

}
