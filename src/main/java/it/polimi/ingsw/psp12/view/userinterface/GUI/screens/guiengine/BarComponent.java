package it.polimi.ingsw.psp12.view.userinterface.GUI.screens.guiengine;


import javax.swing.*;
import java.awt.*;

/**
 * Class defining a generic bar component like sidebar or topbar
 * @author Mattia Malacarne
 *
  */
public class BarComponent extends JPanel
{


    /**
     * Render a bar without background
     * @param width
     * @param height
     */
    public BarComponent(int width, int height)
    {

        //this.setPreferredSize(new Dimension(width, height));
        this.add(new JButton("test"));
        System.out.println("Ho aggiunto");
        //this.setVisible(true);
    }

    /**
     * Render a bar with a bg
     * @param width
     * @param height
     * @param img
     */
    public BarComponent(int width, int height, String img)
    {

        this.setPreferredSize(new Dimension(width, height));
        Image bg = loadBackground(new Dimension(width, height), img);
        JLabel backGound = new JLabel(new ImageIcon(bg));
        this.add(backGound);

        this.add(new JButton("testing"));
        this.setVisible(true);

    }

    private Image loadBackground(Dimension size, String img)
    {
        ImageIcon icon = new ImageIcon(getClass().getResource("/"+img));
        Image scaled = icon.getImage().getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
        return scaled;
    }
}
