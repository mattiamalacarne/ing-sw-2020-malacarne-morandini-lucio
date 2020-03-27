package it.polimi.ingsw.psp12.model;

import java.awt.*;

public class Worker
{
    private Image GUIsprite;
    private char CLIsprite;

    public Image getGUIsprite() {
        return GUIsprite;
    }

    public char getCLIsprite() {
        return CLIsprite;
    }

    public void setGUIsprite(Image img) {
        this.GUIsprite = img;
    }

    public void setCLIsprite(char c) {
        this.CLIsprite = c;
    }
}
