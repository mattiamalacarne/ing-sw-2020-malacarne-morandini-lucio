package it.polimi.ingsw.psp12.model.board;

import it.polimi.ingsw.psp12.model.Worker;

import java.io.Serializable;

/**
 * Class that represents a cell of the board
 * Holds references to the tower and worker positioned on it
 * @author Luca Morandini
 */
public class Cell implements Serializable
{
    /**
     * Reference to the tower on the cell
     */
    private Tower tower;

    /**
     * Reference to the worker positioned on the cell
     */
    private Worker worker;

    /**
     * Coordinates of the current cell on the board
     */
    private final Point location;


    /**
     * Constructor of the cell
     * @param x x position
     * @param y y position
     */
    public Cell(int x, int y) {
        location = new Point(x, y);
        tower = new Tower();
        worker = null;
    }

    /**
     * Getter for the tower
     * @return the tower
     */
    public Tower getTower() {
        return tower;
    }

    /**
     * Determines if there is a worker placed on the cell
     * @return true if there is a worker on the cell
     */
    public boolean hasWorker() {
        return worker != null;
    }

    /**
     * Getter for the worker
     * @return the worker
     */
    public Worker getWorker() {
        return worker;
    }

    /**
     * Add the worker to the current cell
     * @param w worker to be placed in the current cell
     */
    public void addWorker(Worker w) {
        worker = w;
    }

    /**
     * Remove the worker from the current cell
     * @return the worker that has been removed
     */
    public Worker removeWorker() {
        Worker w = worker;
        worker = null;

        return w;
    }

    /**
     * Getter for the location
     * @return the location
     */
    public Point getLocation() {
        return location.clone();
    }

    /**
     * Check if worker can move on the current cell
     * @param currentLevel the level of the actual cell tower
     * @param maxClimbLevel max number of level/s which is possible to climb
     * @return true if (tower.level - currentLevel) is low_equal to maxClimbLevel
     */
    public boolean canMoveOn(int currentLevel, int maxClimbLevel) {
        return (tower.getLevel() - currentLevel) <= maxClimbLevel && !tower.hasDome();
    }

    /**
     * Check if worker can build on the current cell
     * @return true if worker can build on the current cell
     */
    public boolean canBuildOn() {
        return !tower.hasDome();
    }

    /**
     * Determines if two cells are equal
     * @param o cell to compare
     * @return true if this cell equals the parameter
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return location.equals(cell.location);
    }

    /**
     * Returns a clone of the cell
     * @return cell clone
     */
    @Override
    public Cell clone() {
        Cell c = new Cell(this.location.getX(), this.location.getY());
        c.tower = this.tower.clone();
        if (this.worker != null) {
            c.worker = this.worker.clone();
        }
        return c;
    }
}
