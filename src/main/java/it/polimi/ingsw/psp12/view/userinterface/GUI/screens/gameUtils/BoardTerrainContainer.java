package it.polimi.ingsw.psp12.view.userinterface.GUI.screens.gameUtils;

import it.polimi.ingsw.psp12.utils.Color;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Class for draw the gameterrain, bg and workers on the screen
 * @author Mattia Malacarne
 */
public class BoardTerrainContainer extends JLayeredPane
{
    /** Game terrain image **/
    private JLabel gameBoard;

    /** Game grid **/
    private JPanel gameGrid;

    /** List of cell **/
    private CellDraw[] cells;

    public BoardTerrainContainer(Dimension size)
    {
        // Set dimension of the terrain size
        this.setPreferredSize(size);

        // Init the gameGrid
        gameGrid = new JPanel();
        gameGrid.setBounds(496,187, 430,438 );
        //gameGrid.setBackground(Color.RED);
        gameGrid.setOpaque(false);

        gameGrid.setLayout(new GridLayout(5,5,0,0));
        // Create the grid
        drawGameGrid(gameGrid);

        Image board = loadGameTerrain(size);
        gameBoard = new JLabel(new ImageIcon(board));
        gameBoard.setBounds(0,0, size.width, size.height);

        this.setLayout(null);

        this.add(gameBoard, JLayeredPane.DEFAULT_LAYER);
        this.add(gameGrid, JLayeredPane.DRAG_LAYER);


        this.setVisible(true);
    }

    /**
     * Load the image for the game terrain
     * @return a scaled image of the terrain
     */
    private Image loadGameTerrain(Dimension size)
    {
        ImageIcon icon = new ImageIcon(getClass().getResource("/gamescreenbg.png"));
        Image scaled = icon.getImage().getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
        return scaled;
    }

    /**
     * Redraw the gameGrid reading a BoardUpdateMsg
     */
    private void drawGameGrid(JPanel grid)
    {
        cells = new CellDraw[25];
        for (int i = 0; i < 25; i++) { cells[i] = new CellDraw(); }
        for (int i = 0; i < 25; i++) {grid.add(cells[i]);}

    }
}
