package it.polimi.ingsw.psp12.view.userinterface.GUI.screens.gameUtils;

import it.polimi.ingsw.psp12.model.enumeration.BuildOption;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.GameScreen;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.SetUpUtils.DialogContent;
import it.polimi.ingsw.psp12.view.userinterface.GUinterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Dialog activate by cards power for build something in a cell
 * @author Mattia Malacarne
 */

public class ChooseBuildPanel extends DialogContent
{
    private GameScreen game;
    private JLabel text;
    private JButton setButton;

    private JComboBox avBuild;

    List<BuildOption> options;

    public ChooseBuildPanel(GUinterface gui, List<BuildOption> options, GameScreen game) {
        super(gui);

        this.game = game;
        this.options = options;

        this.setLayout(new GridLayout(3,1));

        BuildOption[] builds = new BuildOption[options.size()];
        for (int i = 0; i < options.size(); i++)
        {
            builds[i] = options.get(i);
        }

        avBuild = new JComboBox(builds);
        text = new JLabel("Select a build type");
        setButton = new JButton("Choose");
        setButton.addActionListener(setBuild);

        this.add(text);
        this.add(avBuild);
        this.add(setButton);

        this.setVisible(true);

    }

    ActionListener setBuild = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            game.chooseBuild(options.get(avBuild.getSelectedIndex()));
            parent.dispose();
        }
    };
}
