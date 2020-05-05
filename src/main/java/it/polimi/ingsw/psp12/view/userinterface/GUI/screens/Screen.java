package it.polimi.ingsw.psp12.view.userinterface.GUI.screens;

import it.polimi.ingsw.psp12.exceptions.GUIStatusErrorException;
import it.polimi.ingsw.psp12.view.userinterface.GUI.GUIStatus;
import it.polimi.ingsw.psp12.view.userinterface.GUinterface;

import javax.swing.*;

/**
 * Class for contain the generic screen settings
 * @author Mattia Malacarne
 */
public class Screen extends JPanel implements ScreenInterface {
    @Override
    public void changeScreen(GUIStatus status, GUinterface gui) {
        try {
            gui.loadNewStatusScreen(status);
        } catch (GUIStatusErrorException e) {
            e.printStackTrace();
        }
    }
}
