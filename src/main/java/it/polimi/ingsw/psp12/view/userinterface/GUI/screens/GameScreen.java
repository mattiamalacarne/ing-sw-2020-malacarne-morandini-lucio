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

    /** left and right bars **/
    private BarComponent sidebarsx;
    private JPanel sidebardx;

    /** top and bottom **/
    private BarComponent topbar;
    private JPanel bottombar;



    public GameScreen(GUinterface gui)
    {
        // Init screen size
        terrainDimensionY = (int) (gui.getWindowDimY() - 200);
        terrainDimensionX = (int) (terrainDimensionY*gui.getAspectRatio());

        // Render the Bars


        this.setLayout(new BorderLayout(-10,0));
        btnTest = new JButton("Test");
        btnTest.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Ciao da Game");
                changeScreen(GUIStatus.SETUP, gui);
            }
        });
        //this.add(btnTest, BorderLayout.LINE_START);

        this.add(new BarComponent(1422,100, "topbar.png"), BorderLayout.NORTH);
        this.add(new BarComponent(1422,100, "bottom.png"), BorderLayout.SOUTH);

        this.add(new BarComponent(178,600, "sx.png"), BorderLayout.LINE_START);
        this.add(new BarComponent(178,600, "dx.png"), BorderLayout.LINE_END);

        this.add(new BoardTerrainContainer(new Dimension((int) terrainDimensionX, (int) terrainDimensionY)), BorderLayout.CENTER);
        this.setVisible(true);
    }


}
