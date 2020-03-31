package it.polimi.ingsw.psp12.model.board;

import it.polimi.ingsw.psp12.model.Worker;

public class Cell
{
    private Tower tower;
    private Worker worker;
    private Point location;

    public boolean hasTower() {
        return false;
    }

    public boolean hasWorker() {
        return false;
    }

    public Tower getTower() {
        return tower;
    }

    public Worker getWorker() {
        return worker;
    }

    public Point getLocation() {
        return new Point(location.getX(), location.getY());
    }

    public Cell(int x, int y) {
        tower = new Tower();
        worker = null;
        location = new Point(x, y);
    }

    /**
     * check if worker can move on the current cell
     * @param currentLevel the level of the actual cell tower
     * @return true if tower.level - currentLevel <= 1
     */
    public boolean canMoveOn(int currentLevel) {
        return (tower.getLevel() - currentLevel) <= 1;
    }

    /**
     * check if worker can move on the current cell
     * @return true if worker can build on the current cell
     */
    public boolean canBuildOn() {
        return !tower.hasDome();
    }

    /**
     *  add the worker to the current cell
     * @param w worker to be placed in the current cell
     */
    public void addWorker(Worker w) {
        worker = w;
    }

    /**
     * remove the worker from the current cell
     */
    public void removeWorker() {
        worker = null;
    }
}
