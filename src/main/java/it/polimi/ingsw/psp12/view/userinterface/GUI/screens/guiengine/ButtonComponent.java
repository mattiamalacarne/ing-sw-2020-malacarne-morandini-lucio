package it.polimi.ingsw.psp12.view.userinterface.GUI.screens.guiengine;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class ButtonComponent extends JButton
{

    public ButtonComponent(Color color, String text, int posX, int posY)
    {
        super(text);
        String image = "btn_blue.png";
        Dimension size = new Dimension(180,60);

        this.setHorizontalTextPosition(JButton.CENTER);
        this.setVerticalTextPosition(JButton.CENTER);

        // Set transparent
        this.setOpaque(false);
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);

        // Load the font
        this.setFont(new Font("dalek", Font.PLAIN, 25));

        this.setBounds(posX,posY, size.width,size.height);
        this.setIcon(loadButtonIcon(image, size));
        this.setVisible(true);

    }

    public ButtonComponent(Color color, String text)
    {
        super(text);
        String image = "btn_blue.png";
        Dimension size = new Dimension(180,60);

        this.setHorizontalTextPosition(JButton.CENTER);
        this.setVerticalTextPosition(JButton.CENTER);

        // Set transparent
        this.setOpaque(false);
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);

        // Load the font
        this.setFont(new Font("dalek", Font.PLAIN, 25));

        this.setIcon(loadButtonIcon(image, size));
        this.setVisible(true);

    }

    private ImageIcon loadButtonIcon(String image, Dimension size)
    {
        ImageIcon icon = new ImageIcon(getClass().getResource("/"+image));
        Image scaled = icon.getImage().getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    private Font loadGameFont()
    {

        File font = new File(getClass().getClassLoader().getResource("dalek.ttf").getFile());
        try {

            return Font.createFont(Font.TRUETYPE_FONT, font);

        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
