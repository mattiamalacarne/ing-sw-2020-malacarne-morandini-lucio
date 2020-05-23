package it.polimi.ingsw.psp12.view.userinterface.GUI.screens.SetUpUtils;

import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.SetUpScreen;
import it.polimi.ingsw.psp12.view.userinterface.GUinterface;

import javax.swing.*;
import java.awt.*;

/**
 * This class contains all the label and elements used for setup the player (server name ecc.)
 * @author Mattia Malacarne
 */

public class SetUpPanel extends JPanel
{


    // Utils
    private GUinterface gui;

    public SetUpPanel(Point p, GUinterface gui, SetUpScreen setup, Dimension size)
    {

        this.setOpaque(false);
        this.setBounds(p.getX(),p.getY(),size.width, size.height);

        this.gui = gui;

    }



}
