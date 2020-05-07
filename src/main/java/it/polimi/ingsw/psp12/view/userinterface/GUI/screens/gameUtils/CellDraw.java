package it.polimi.ingsw.psp12.view.userinterface.GUI.screens.gameUtils;

import it.polimi.ingsw.psp12.utils.Color;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CellDraw extends JButton
{

    private Image worker;


    public CellDraw(String text)
    {
        super(text);
        // set the button transparent
        this.setOpaque(false);
        this.setContentAreaFilled(false);

    }

    public CellDraw()
    {
        //this.setIcon(loadWorker(Color.RED));
        this.setOpaque(false);
        this.setContentAreaFilled(false);


    }

    private ImageIcon loadWorker(Color color)
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

    public void updateCell(Color color)
    {
        this.setIcon(loadWorker(Color.RED));
    }


}
