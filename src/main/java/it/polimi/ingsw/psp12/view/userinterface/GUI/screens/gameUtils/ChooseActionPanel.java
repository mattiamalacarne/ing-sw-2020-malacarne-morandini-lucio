package it.polimi.ingsw.psp12.view.userinterface.GUI.screens.gameUtils;

import it.polimi.ingsw.psp12.model.enumeration.Action;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.GameScreen;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.SetUpUtils.DialogContent;
import it.polimi.ingsw.psp12.view.userinterface.GUinterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * This class display a dialobox asking to user an action to complete
 * @author Mattia Malacarne
 */

public class ChooseActionPanel extends DialogContent
{

    private GameScreen game;
    private JLabel text;
    private JButton setButton;

    private JComboBox avAction;

    public ChooseActionPanel(GUinterface gui, GameScreen game, List<Action> possActions) {
        super(gui);
        this.game = game;
        System.out.println("Creami");
        this.setLayout(new GridLayout(3,1));

        Action[] actions = new Action[possActions.size()];
        for (int i = 0; i < possActions.size(); i++)
        {
            actions[i] = possActions.get(i);
        }

        avAction = new JComboBox(actions);
        text = new JLabel("Select your action");
        setButton = new JButton("Set");
        setButton.addActionListener(setAction);

        this.add(text);
        this.add(avAction);
        this.add(setButton);

        this.setVisible(true);
    }

    ActionListener setAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Invio al server");
        }
    };
}
