package it.polimi.ingsw.psp12.view.userinterface.GUI.screens;

import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.SetUpUtils.SetUpPanel;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.guiengine.ButtonComponent;
import it.polimi.ingsw.psp12.view.userinterface.GUinterface;

import javax.swing.*;
import java.awt.*;

public class SetUpScreen extends Screen
{

    private SetUpPanel setupPanel;

    public SetUpScreen(GUinterface gui) {
        super(gui);
        this.setPreferredSize(new Dimension(screenX, screenY));

        this.setLayout(null);
        // Load the bg
        Image bg = loadScreenBackground("firstscreen.png", new Dimension(screenX, screenY));
        screenBg = new JLabel(new ImageIcon(bg));
        screenBg.setBounds(0,0,screenX,screenY);

        ButtonComponent createBtn = new ButtonComponent(Color.BLUE, "Create", 525, 600);
        ButtonComponent joinBtn = new ButtonComponent(Color.BLUE, "Join", 725, 600);

        setupPanel = new SetUpPanel(new Point(490,104),gui);
        this.add(setupPanel);

        this.add(joinBtn);
        this.add(createBtn,JLayeredPane.DRAG_LAYER);
        this.add(screenBg, JLayeredPane.DEFAULT_LAYER);

        this.setVisible(true);
    }
}
