package it.polimi.ingsw.psp12.model.board;

import java.awt.*;

public class Tower
{
    private Image GUISPrite;
    private char CLISprite;

    private int level;
    private boolean dome;

    public Image getGUISPrite() {
        return GUISPrite;
    }

    public char getCLISprite() {
        return CLISprite;
    }

    public int getLevel() {
        return level;
    }

    public void incrementLevel() {
        if (level < 3)
            level++;
    }

    public void buildDome() {
        dome = true;
    }

    public boolean hasDome() {
        return dome;
    }
}
