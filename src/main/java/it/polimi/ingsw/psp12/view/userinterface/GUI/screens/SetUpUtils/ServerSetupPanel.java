package it.polimi.ingsw.psp12.view.userinterface.GUI.screens.SetUpUtils;

import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.SetUpScreen;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.guiengine.MenuTextComponent;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.guiengine.TextFieldComponent;
import it.polimi.ingsw.psp12.view.userinterface.GUinterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ServerSetupPanel extends SetUpPanel
{

    private MenuTextComponent serverLabel;
    private TextFieldComponent serverIn;

    public ServerSetupPanel(Point p, GUinterface gui, SetUpScreen setup, Dimension size) {
        super(p, gui, setup, size);

        this.setLayout(new GridLayout(3,1));
        serverLabel = new MenuTextComponent("Server IP");
        serverIn = new TextFieldComponent();
        serverIn.setText("localhost");

        JButton btn = new JButton("Play");
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    gui.connectToServer(serverIn.getText());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        this.add(serverLabel);
        this.add(serverIn);
        this.add(btn);

        this.setVisible(false);
    }


}
