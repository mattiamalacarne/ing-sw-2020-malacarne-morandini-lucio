package it.polimi.ingsw.psp12.view.userinterface.GUI;

import javax.swing.*;
import java.awt.*;

/**
 * Class for draw on screen the gameBoard with tower and worker
 */
public class GameTerrain extends JPanel
{
    Image boardBg;

    public GameTerrain()
    {
        // Loading resources
        this.loadGraphicsResources();
    }

    /**
     * Init the game terrain loading all the required image
     */
    private void loadGraphicsResources()
    {
        boardBg = Toolkit.getDefaultToolkit().createImage("Board.png");
    }

    /**
     * Draw on screen the background (The board terrain)
     * @param terrain
     */
    private void drawGameTerrainBg(Image terrain)
    {

    }
}
