package it.polimi.ingsw.psp12.model;

public class Cell
{
    private Tower tower;
    private Worker worker;

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

    public Cell() {
        tower = new Tower();
    }

    /**
     *
     * @param currentLevel the level of the actual cell tower
     * @return true if tower.level - currentLevel <= 1
     */
    public boolean canMoveOn(int currentLevel) {
        return (tower.getLevel() - currentLevel) <= 1;
    }


}
