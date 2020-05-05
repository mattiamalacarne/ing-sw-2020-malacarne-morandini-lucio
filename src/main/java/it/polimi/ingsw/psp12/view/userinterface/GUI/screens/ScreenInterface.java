package it.polimi.ingsw.psp12.view.userinterface.GUI.screens;

import it.polimi.ingsw.psp12.view.userinterface.GUI.GUIStatus;
import it.polimi.ingsw.psp12.view.userinterface.GUinterface;

/**
 * Interface for create Screens in the game
 * @author Mattia Malacarne
 */

public interface ScreenInterface
{
    /**
     * load another screen in the window
     * @param status identify the new screen to load
     * @param gui where the screen will loaded
     */
    public void changeScreen(GUIStatus status, GUinterface gui);
}
