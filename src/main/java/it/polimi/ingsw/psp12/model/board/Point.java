package it.polimi.ingsw.psp12.model.board;

import java.io.Serializable;

/**
 * Class that is used to hold information of a coordinate on the map
 * @author Luca Morandini
 */
public class Point implements Serializable
{
    /**
     * Column index
     */
    private final int x;

    /**
     * Row index
     */
    private final int y;


    /**
     * Constructor
     * @param x x position
     * @param y y position
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Getter of x value
     * @return x value
     */
    public int getX() {
        return x;
    }

    /**
     * Getter of y value
     * @return y value
     */
    public int getY() {
        return y;
    }

    /**
     * Check if the current point is valid
     * @return true if the point represents a valid point on the map
     */
    public boolean isOnBoard() {
        return (x >= 0 && x <= 4 && y >= 0 && y <= 4);
    }

    /**
     * Determines if the provided point is near to the current point
     * @param target point to be tested with the current point
     * @return true if the provided point is near to the current point
     */
    public boolean isNear(Point target) {
        int dx = Math.abs(this.x - target.getX());
        int dy = Math.abs(this.y - target.getY());

        return (dx <= 1 && dy <= 1);
    }

    /**
     * Determines if two points are equal
     * @param o point to compare
     * @return true if this point equals the parameter
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x &&
                y == point.y;
    }

    /**
     * Returns a string representation of the curren point
     * @return string representation
     */
    @Override
    public String toString() {
        return  "x = " + x +
                ", y = " + y ;
    }

    /**
     * Returns a clone of the point
     * @return point clone
     */
    @Override
    public Point clone() {
        return new Point(this.x, this.y);
    }
}
