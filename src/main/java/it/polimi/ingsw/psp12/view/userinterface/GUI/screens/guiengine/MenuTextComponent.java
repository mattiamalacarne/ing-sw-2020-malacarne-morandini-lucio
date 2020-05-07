package it.polimi.ingsw.psp12.view.userinterface.GUI.screens.guiengine;

import it.polimi.ingsw.psp12.model.board.Point;

import javax.swing.*;
import java.awt.*;

public class MenuTextComponent extends JLabel
{
    public MenuTextComponent(Point p, String text)
    {
        super(text);
        this.setBounds(p.getX(),p.getY(), 500,500);
        this.setFont(new Font("dalek", Font.PLAIN, 25));
    }

    public MenuTextComponent(String text)
    {
        super(text);
        this.setFont(new Font("dalek", Font.PLAIN, 25));
    }
}
