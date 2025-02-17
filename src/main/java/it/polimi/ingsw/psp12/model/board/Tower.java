package it.polimi.ingsw.psp12.model.board;

import java.io.Serializable;

/**
 * <p><b>Class</b> that represent a tower</p>
 *
 * @author Mattia Malacarne
 */

public class Tower implements Serializable
{
    private int level;
    private boolean dome;

    /**
     * get the selected tower level
     * @return the tower level
     */
    public int getLevel() {
        return level;
    }

    /**
     * increment the tower level, if the level is max (3) than build a dome on the top
     */
    public void incrementLevel() {
        if (level < 3)
            level++;
        else
            dome = true;
    }

    /**
     * build a dome on any level (if is possible)
     */
    public void buildDome() {
        dome = true;
    }

    /**
     * check if the selected tower has a dome on top
     * @return true if there is a dome, false if not
     */
    public boolean hasDome() {
        return dome;
    }

    /**
     * Returns a clone of the tower
     * @return tower clone
     */
    @Override
    public Tower clone() {
        Tower t = new Tower();
        t.level = this.level;
        t.dome = this.dome;
        return t;
    }

    /**
     * Set the level of the tower
     * @param level new level
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Set if the tower has the dome
     * @param dome true if has dome
     */
    public void setDome(boolean dome) {
        this.dome = dome;
    }
}
