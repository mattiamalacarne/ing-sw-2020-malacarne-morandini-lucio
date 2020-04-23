package it.polimi.ingsw.psp12.view.userinterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Class for define all the graphocal elements relative to the game setup
 * @author Mattia Malacarne
 */

public class GUISetup extends JPanel
{

    GUinterface mainInt;
    // Textfield for user
    JTextField playerName;

    // Textfield for network
    JTextField gameIp;

    // Btn for choose server action
    JButton createRoom;
    JButton joinRoom;

    public GUISetup(GUinterface mainInterface)
    {

        this.mainInt = mainInterface;

        playerName = new JTextField(20);
        this.add(playerName);

        gameIp = new JTextField(15);
        this.add(gameIp);

        createRoom = new JButton("New room");
        createRoom.addActionListener(createBtn);
        this.add(createRoom);

        joinRoom = new JButton("Join room");
        joinRoom.addActionListener(joinBtn);
        this.add(joinRoom);
    }

    // Actionlistener waiting for button pressed
    ActionListener createBtn = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("test");
        }
    };

    ActionListener joinBtn = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            mainInt.setClientName();
        }
    };
}
