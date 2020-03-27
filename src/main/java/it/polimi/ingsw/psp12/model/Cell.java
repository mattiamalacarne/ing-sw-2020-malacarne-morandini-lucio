package it.polimi.ingsw.psp12.model;

public class Cell
{
    private Tower tower;
    private Worker worker;

    public Boolean hasTower() {}
    public Boolean hasWorker() {}

    public Tower getTower() {
        return tower;
    }

    public Worker getWorker() {
        return worker;
    }

    /**
     *
     * @param currentLevel the level of the actual cell tower
     * @return true if tower.level - currentlevel<= 1
     */
    public Boolean canMoveOn(int currentLevel)
    {
        return (tower.getLevel() - currentLevel <= 1) ? true : false;
    }


}
