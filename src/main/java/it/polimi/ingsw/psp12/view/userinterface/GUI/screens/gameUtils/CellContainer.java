package it.polimi.ingsw.psp12.view.userinterface.GUI.screens.gameUtils;

import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.GameScreen;

import javax.swing.*;

/**
 * Used for render a tower under a worker in game
 * @author Mattia Malacarne
 */
public class CellContainer extends JLayeredPane
{
    /* The tower to render */
    private JLabel tower;

    /* Clickable cell */
    public CellDraw cell;

    public CellContainer(Point p, GameScreen game)
    {
        this.setLayout(null);
        cell = new CellDraw(p, game, this);
        tower = new JLabel();

        tower.setBounds(0,0,86,86);

        this.add(tower, JLayeredPane.DEFAULT_LAYER);
        this.add(cell, JLayeredPane.DRAG_LAYER);

        this.setOpaque(false);

        this.setVisible(true);
    }

    public void setTower(ImageIcon icon)
    {
        tower.setIcon(icon);
    }
}
