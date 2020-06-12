package it.polimi.ingsw.psp12.view.userinterface.GUI.screens.SetUpUtils;

import it.polimi.ingsw.psp12.utils.Color;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.GameScreen;
import it.polimi.ingsw.psp12.view.userinterface.GUinterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ChooseColorPanel extends DialogContent
{

    private JLabel text;
    private JButton setButton;
    private JComboBox avColors;

    private GameScreen game;

    public ChooseColorPanel(GameScreen game, GUinterface gui, List<Color> colors)
    {
        super(gui);
        this.game = game;
        this.setLayout(new GridLayout(3,1));

        Color[] arrayColor = new Color[colors.size()];
        for (int i = 0; i < colors.size(); i++)
        {
            arrayColor[i] = colors.get(i);
        }

        avColors = new JComboBox(arrayColor);
        text = new JLabel("Select your color");
        setButton = new JButton("Set");
        setButton.addActionListener(setColorPressed);

        this.add(text);
        this.add(avColors);
        this.add(setButton);

        this.setVisible(true);
    }

    /**
     * Pass to the GUI the color for create the message
     */
    ActionListener setColorPressed = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            game.chooseColor(avColors.getSelectedIndex());
            parent.dispose();
        }
    };
}
