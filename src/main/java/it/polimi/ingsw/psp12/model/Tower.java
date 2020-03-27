package it.polimi.ingsw.psp12.model;

import java.awt.*;

public class Tower
{
    private Image GUISPrite;
    private char CLISprite;

    private int level;
    private Boolean dome;

    public Image getGUISPrite() {
        return GUISPrite;
    }

    public char getCLISprite() {
        return CLISprite;
    }

    public int getLevel() {
        return level;
    }

    public void incrementLevel() {}
    public Boolean hasDome()
    {
        return dome;
    }
}
