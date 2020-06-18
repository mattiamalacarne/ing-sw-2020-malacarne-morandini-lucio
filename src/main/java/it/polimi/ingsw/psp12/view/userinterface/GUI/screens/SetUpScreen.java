package it.polimi.ingsw.psp12.view.userinterface.GUI.screens;

import it.polimi.ingsw.psp12.exceptions.GUIStatusErrorException;
import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.view.userinterface.GUI.GUIStatus;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.SetUpUtils.*;
import it.polimi.ingsw.psp12.view.userinterface.GUinterface;

import javax.swing.*;
import java.awt.*;

/**
 * This screen display a box for connect to a server
 * @author Mattia Malacarne
 */

public class SetUpScreen extends Screen
{

    private ServerSetupPanel serverSetupPanel;
    private SetUpPanel actualPanel;

    private GUinterface gui;
    private SetUpScreen thisScreen;
    private GenericMessageScreen waitBox;

    public SetUpScreen(GUinterface gui) {
        super(gui);
        this.gui = gui;
        thisScreen = this;

        this.setPreferredSize(new Dimension(screenX, screenY));

        this.setLayout(null);

        // Load the bg
        Image bg = loadScreenBackground("firstscreen.png", new Dimension(screenX, screenY));
        screenBg = new JLabel(new ImageIcon(bg));
        screenBg.setBounds(0,0,screenX,screenY);


        serverSetupPanel = new ServerSetupPanel(new Point(490,104),gui, this, new Dimension(437,100));

        this.add(serverSetupPanel);

        this.add(screenBg, JLayeredPane.DEFAULT_LAYER);
        actualPanel = serverSetupPanel;
        actualPanel.setVisible(true);


        this.setVisible(true);
    }

    /**
     * Load a waiting message to the screen
     */
    public void dispayWaitBox()
    {
        try {
            gui.loadNewStatusScreen(GUIStatus.WAIT_CARD_SELECTION, null);
        } catch (GUIStatusErrorException e) {
            e.printStackTrace();
        }
    }

}
