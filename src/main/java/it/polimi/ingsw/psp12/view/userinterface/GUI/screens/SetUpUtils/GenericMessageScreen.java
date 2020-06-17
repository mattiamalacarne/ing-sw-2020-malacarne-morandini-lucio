package it.polimi.ingsw.psp12.view.userinterface.GUI.screens.SetUpUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Display on screen a box while the other player join the room
 * @author Mattia Malacarne
 */

public class GenericMessageScreen extends JLayeredPane
{
    private JLabel waitText;
    private JLabel backg;

    public GenericMessageScreen(Dimension size, String text) {

        Image bg = loadScreenBackground("firstscreen.png", new Dimension(size.width, size.height));
        backg = new JLabel(new ImageIcon(bg));

        backg.setBounds(0,0, size.width, size.height);

        waitText = new JLabel(text, SwingConstants.CENTER);
        waitText.setFont(new Font("dalek", Font.PLAIN, 28));
        waitText.setBounds(490,50, 437, 300);

        this.setLayout(null);

        this.setBounds(0,0, size.width,size.height);
        this.add(waitText, JLayeredPane.DRAG_LAYER);
        this.add(backg, JLayeredPane.DEFAULT_LAYER);
        this.setVisible(true);
        this.setVisible(true);
    }

    public Image loadScreenBackground(String bg, Dimension size) {
        ImageIcon icon = new ImageIcon(getClass().getResource("/"+bg));
        Image scaled = icon.getImage().getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
        return scaled;
    }
}
