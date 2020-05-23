package it.polimi.ingsw.psp12.view.userinterface.GUI.screens.roomUtils;

import it.polimi.ingsw.psp12.view.userinterface.GUinterface;

import javax.swing.*;
import java.awt.*;

/**
 * This class display a dialog for set the playername
 * @author Mattia Malacarne
 */
public class SetPlayerNamePanel extends JPanel
{
    private JLabel setNameLabel;
    private JTextField nameIn;
    private JButton setBtn;

    private GUinterface gui;

    public SetPlayerNamePanel(GUinterface gui)
    {
        this.setLayout(new GridLayout(3,1));
        setNameLabel = new JLabel("Whats your name?");
        nameIn = new JTextField();
        setBtn = new JButton("Set name");

        this.add(setNameLabel);
        this.add(nameIn);
        this.add(setBtn);

        this.setVisible(true);
    }
}
