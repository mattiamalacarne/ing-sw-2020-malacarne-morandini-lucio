package it.polimi.ingsw.psp12.view.userinterface.GUI.screens.gameUtils;

import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.utils.Color;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.GameScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CellDraw extends JButton
{

    private Point pos;
    private GameScreen game;
    private CellDraw me;

    public CellDraw(String text)
    {
        super(text);
        // set the button transparent
        this.setOpaque(false);
        this.setContentAreaFilled(false);

    }

    public CellDraw(Point cell, GameScreen game)
    {
        this.pos = cell;
        this.game = game;
        me = this;
        this.setOpaque(false);
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);

    }

    public void enablePress()
    {
        //System.out.println("Addes listener to cell");
        this.addActionListener(prssed);
        //this.setBackground(new java.awt.Color(213, 134, 145, 123));
        this.setBorderPainted(true);
        this.repaint();
    }

    public void flushMe()
    {
        this.setBorderPainted(false);
        this.removeActionListener(prssed);
    }

    public ImageIcon loadSelector()
    {
        ImageIcon icon = new ImageIcon(getClass().getResource("image"));
        Image scaled = icon.getImage().getScaledInstance(100,80,Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

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

    public ImageIcon loadTower(int level)
    {
        StringBuilder str = new StringBuilder();
        str.append("/towers/tower");
        str.append(level);
        str.append(".png");
        ImageIcon icon;
        icon = new ImageIcon(getClass().getResource(str.toString()));
        Image scaled = icon.getImage().getScaledInstance(100, 80, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    public void updateCell(CellIcon type,Color color, int level)
    {
        switch (type)
        {
            case WORKER: {this.setIcon(loadWorker(color)); break;}
            case TOWER: {this.setIcon(loadTower(level)); break; }
            case SELECTOR: {this.loadSelector(); break; }
        }
        this.setBorderPainted(false);
    }

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
