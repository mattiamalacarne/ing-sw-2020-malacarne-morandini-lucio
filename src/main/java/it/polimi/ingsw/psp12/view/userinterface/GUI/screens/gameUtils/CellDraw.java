package it.polimi.ingsw.psp12.view.userinterface.GUI.screens.gameUtils;

import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.utils.Color;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.GameScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class represent a cell on the GUI gameboard
 * @author Mattia Malacarne
 */
public class CellDraw extends JButton
{

    private Point pos;
    private GameScreen game;
    private CellDraw me;
    private CellContainer parent;

    public CellDraw(String text)
    {
        super(text);
        // set the button transparent
        this.setOpaque(false);
        this.setContentAreaFilled(false);

    }

    public CellDraw(Point cell, GameScreen game, CellContainer parent)
    {
        this.parent = parent;
        this.pos = cell;
        this.game = game;
        me = this;

        this.setBounds(0,0,100,80);
        this.setOpaque(false);
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);

    }

    /**
     * Make the cell clickable
     */
    public void enablePress()
    {
        this.addActionListener(prssed);
        this.setBorderPainted(true);
        this.repaint();
    }

    /**
     * Reset the cell status
     */
    public void flushMe()
    {
        this.setBorderPainted(false);
        this.removeActionListener(prssed);
    }

    // TODO: Remove me
    public ImageIcon loadSelector()
    {
        ImageIcon icon = new ImageIcon(getClass().getResource("image"));
        Image scaled = icon.getImage().getScaledInstance(100,80,Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    /**
     * Draw a worker in this cell
     * @param color the worker color
     * @return the image to render
     */
    public ImageIcon loadWorker(Color color)
    {
        ImageIcon icon;
        switch (color)
        {
            case BLUE: { icon = new ImageIcon(getClass().getResource("/workers/blue-worker.png")); break;}
            case RED: { icon = new ImageIcon(getClass().getResource("/workers/red-worker.png")); break;}
            case GREEN: { icon = new ImageIcon(getClass().getResource("/workers/green-worker.png")); break;}
            case YELLOW: { icon = new ImageIcon(getClass().getResource("/workers/yellow-worker.png")); break;}
            default: return null;
        }
        Image scaled = icon.getImage().getScaledInstance(100, 80, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    /**
     * Draw a tower in this cell
     * @param level the tower level
     * @return the image to render
     */
    public ImageIcon loadTower(int level)
    {
        if (level == 0) return null;
        StringBuilder str = new StringBuilder();
        str.append("/towers/tower");
        str.append(level);
        str.append(".png");
        ImageIcon icon;
        icon = new ImageIcon(getClass().getResource(str.toString()));
        Image scaled = icon.getImage().getScaledInstance(100, 80, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    /**
     * Load the dome icon
     * @return
     */
    private ImageIcon loadDome()
    {
        ImageIcon icon = new ImageIcon(getClass().getResource("/dome.png"));
        Image scaled = icon.getImage().getScaledInstance(100, 80, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    /**
     * Draw something in this cell
     * @param type the cell type
     * @param color the color
     * @param level the level
     */
    public void updateCell(CellIcon type,Color color, int level)
    {
        switch (type)
        {
            case WORKER: {this.setIcon(loadWorker(color)); break;}
            case DOME: {this.setIcon(loadDome()); break;}
            case TOWER: {this.parent.setTower(loadTower(level)); break; }
            case SELECTOR: {this.loadSelector(); break; }
        }
        this.setBorderPainted(false);
    }

    /**
     * This listener make selection on the board
     */
    ActionListener prssed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (game.getPhase() != GamePhase.NOT_MY_TURN)
            {
                //System.out.println("Clicked on (" +pos.getX()+","+pos.getY()+")");
                game.selectCell(pos);
            } else
            {
                System.out.println("Not your turn!!");
            }
        }
    };


}
