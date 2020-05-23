package it.polimi.ingsw.psp12.view.userinterface.GUI.screens;

import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.SetUpUtils.*;
import it.polimi.ingsw.psp12.view.userinterface.GUinterface;

import javax.swing.*;
import java.awt.*;

public class SetUpScreen extends Screen
{

    private ServerSetupPanel serverSetupPanel;
    private SetUpPanel actualPanel;

    private GUinterface gui;
    private SetUpScreen thisScreen;

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

    public void loadNewPanel(Setup setup)
    {
        actualPanel.setVisible(false);
        actualPanel.setVisible(true);
        thisScreen.revalidate();
        thisScreen.repaint();
        thisScreen.setVisible(true);
    }
}
