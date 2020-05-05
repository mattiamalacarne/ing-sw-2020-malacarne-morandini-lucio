package it.polimi.ingsw.psp12.view.userinterface.GUI.screens;

import it.polimi.ingsw.psp12.view.userinterface.GUI.GUIStatus;
import it.polimi.ingsw.psp12.view.userinterface.GUinterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class containes the graphical elements for setup the user while starting the game
 * @author Mattia Malacarne
 */
public class SetupScreen extends Screen
{
    private JButton btnTest;
    public SetupScreen(GUinterface gui)
    {
        btnTest = new JButton("Mango");
        btnTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Ciao da Setup");
                changeScreen(GUIStatus.GAME, gui);
            }
        });
        this.add(btnTest);
        this.setVisible(true);
    }



}
