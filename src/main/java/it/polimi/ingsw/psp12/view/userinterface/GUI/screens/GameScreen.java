package it.polimi.ingsw.psp12.view.userinterface.GUI.screens;

import it.polimi.ingsw.psp12.view.userinterface.GUI.GUIStatus;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.gameUtils.BoardTerrainContainer;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.guiengine.BarComponent;
import it.polimi.ingsw.psp12.view.userinterface.GUinterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class containes the graphical elements for display the game terrain, worker and card
 * @author Mattia Malacarne
 */
public class GameScreen extends Screen
{

    private int terrainDimensionY;
    private int terrainDimensionX;

    // GUI Game terrain
    private JButton btnTest;

    /** Board image container**/
    private JPanel boardImageContainer;


    public GameScreen(GUinterface gui)
    {
        // Init screen size
       super(gui);


        this.add(new BoardTerrainContainer(new Dimension(screenX, screenY)),JLayeredPane.DEFAULT_LAYER);
        this.setVisible(true);
    }


}
