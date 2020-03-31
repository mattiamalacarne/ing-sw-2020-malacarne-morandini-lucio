package it.polimi.ingsw.psp12.model.board;

import java.awt.*;

public class Tower
{
    private int level;
    private boolean dome;

    public int getLevel() {
        return level;
    }

    public void incrementLevel() {
        if (level < 3)
            level++;
        else
            dome = true;
    }

    public void buildDome() {
        dome = true;
    }

    public boolean hasDome() {
        return dome;
    }
}
