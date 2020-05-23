package it.polimi.ingsw.psp12.view.userinterface.GUI.screens.SetUpUtils;

import it.polimi.ingsw.psp12.view.userinterface.GUinterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class is the panel contained in a JDialog used for define the the playerName in the room
 * @author Mattia Malacarne
 */

public class PlayerNamePanel extends DialogContent
{
    private JTextField name;
    private JButton setName;
    private JLabel text;

    private String[] possibleText = {"Choose a name", "Name already choosed, set another one"};

    public PlayerNamePanel(GUinterface gui, int nameUsed)
    {
        super(gui);
        this.setLayout(new GridLayout(3,1));
        text = new JLabel(possibleText[nameUsed]);
        setName = new JButton("Set");
        name = new JTextField();

        setName.addActionListener(setPlayerNamePressed);

        this.add(text);
        this.add(name);
        this.add(setName);

        this.setVisible(true);
    }

    ActionListener setPlayerNamePressed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            gui.setPlayerName(name.getText());
            parent.dispose();
        }
    };
}
