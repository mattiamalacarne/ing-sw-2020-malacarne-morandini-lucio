package it.polimi.ingsw.psp12.view.userinterface.GUI.screens.gameUtils;

import javax.swing.*;

public class CellDraw extends JButton
{
    public CellDraw(String text)
    {
        super(text);
        // set the button transparent
        this.setOpaque(false);
        this.setContentAreaFilled(false);
    }
}
