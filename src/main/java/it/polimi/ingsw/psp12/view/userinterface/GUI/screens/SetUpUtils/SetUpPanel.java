package it.polimi.ingsw.psp12.view.userinterface.GUI.screens.SetUpUtils;

import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.view.userinterface.GUI.SetupHelper;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.guiengine.ButtonComponent;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.guiengine.MenuTextComponent;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.guiengine.TextFieldComponent;
import it.polimi.ingsw.psp12.view.userinterface.GUinterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * This class contains all the label and elements used for setup the player (server name ecc.)
 * @author Mattia Malacarne
 */

public class SetUpPanel extends JPanel
{
    // Label displayed
    private MenuTextComponent serverIP;
    private MenuTextComponent clientName;
    private MenuTextComponent createRoom;

    // Text input
    private TextFieldComponent serverIp_IN;
    private TextFieldComponent clientName_IN;
    private TextFieldComponent createRoomName_IN;

    // Utils
    Dimension panelSize;

    public SetUpPanel(Point p, GUinterface gui)
    {
        panelSize = new Dimension(437,400);
        this.setOpaque(false);
        this.setBounds(p.getX(),p.getY(),panelSize.width, panelSize.height);

        this.setLayout(new GridLayout(8,1));

        // Setup the label
        serverIP = new MenuTextComponent("Server IP:");
        clientName = new MenuTextComponent("Name:");
        createRoom = new MenuTextComponent("Set a name for the room");

        // Setup the input
        serverIp_IN = new TextFieldComponent();
        clientName_IN = new TextFieldComponent();
        createRoomName_IN = new TextFieldComponent();


        //Test
        ButtonComponent createBtn = new ButtonComponent(Color.BLUE, "Create", 525, 600);
        createBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.createRoom();
            }
        });

        this.add(serverIP);
        this.add(serverIp_IN);
        this.add(clientName);
        this.add(clientName_IN);
        this.add(createRoom);
        this.add(createRoomName_IN);
        this.add(createBtn);

        this.setVisible(true);
    }

}
