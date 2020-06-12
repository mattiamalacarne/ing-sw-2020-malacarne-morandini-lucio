package it.polimi.ingsw.psp12.view.userinterface.GUI.screens.SetUpUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Display on screen a box while the other player join the room
 * @author Mattia Malacarne
 */

public class GenericMessageScreen extends JPanel
{
    private JLabel waitText;

    public GenericMessageScreen(Dimension size, String text) {

        waitText = new JLabel(text);

        this.setBounds(0,0, size.width,size.height);
        this.add(waitText);
        this.setVisible(true);
        this.setVisible(true);
    }
}
