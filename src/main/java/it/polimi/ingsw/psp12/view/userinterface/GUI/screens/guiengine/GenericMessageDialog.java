package it.polimi.ingsw.psp12.view.userinterface.GUI.screens.guiengine;

import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.SetUpUtils.DialogContent;
import it.polimi.ingsw.psp12.view.userinterface.GUinterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * this class is a dialogbox for display generic message to the user
 * @author Mattia Malacarne
 */
public class GenericMessageDialog extends DialogContent
{

    private JLabel text;
    private JButton btn;
    public GenericMessageDialog(GUinterface gui, String text) {
        super(gui);
        this.text = new JLabel(text);

        this.btn = new JButton("OK");
        this.btn.addActionListener(close);

        this.setLayout(new GridLayout(2,1));

        this.add(this.text);
        this.add(btn);

        this.setVisible(true);
    }

    ActionListener close = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            parent.dispose();
        }
    };
}
