package it.polimi.ingsw.psp12.view.userinterface.GUI.screens;

import it.polimi.ingsw.psp12.exceptions.GUIStatusErrorException;
import it.polimi.ingsw.psp12.view.userinterface.GUI.GUIStatus;
import it.polimi.ingsw.psp12.view.userinterface.GUinterface;

import javax.swing.*;
import java.awt.*;

/**
 * Class for contain the generic screen settings
 * @author Mattia Malacarne
 */
public class Screen extends JPanel implements ScreenInterface {
    /** Screen height **/
    protected int screenY;

    /** Screen width **/
    protected int screenX;

    /** Label for screen bg **/
    protected JLabel screenBg;

    protected GUinterface gui;


    public Screen(GUinterface gui)
    {
        this.gui = gui;
        screenY = (int) (gui.getWindowDimY());
        screenX = (int) (screenY*gui.getAspectRatio());
    }

    @Override
    public void changeScreen(GUIStatus status, GUinterface gui) {
        try {
            gui.loadNewStatusScreen(status, null);
        } catch (GUIStatusErrorException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Image loadScreenBackground(String bg, Dimension size) {
        ImageIcon icon = new ImageIcon(getClass().getResource("/"+bg));
        Image scaled = icon.getImage().getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
        return scaled;
    }
}
