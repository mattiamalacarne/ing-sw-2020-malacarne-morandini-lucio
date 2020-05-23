package it.polimi.ingsw.psp12.view.userinterface.GUI.screens.SetUpUtils;

import it.polimi.ingsw.psp12.view.userinterface.GUinterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * This class is the panel contained in a JDialog used for define the number of player in the room
 * @author Mattia Malacarne
 */
public class CreateRoomPanel extends DialogContent
{

    private JLabel text;
    private JButton setPlayerNumberbtn;

    private JRadioButton[] numberChoice;
    private ButtonGroup cGroup;

    private JPanel cContainer;



    public CreateRoomPanel(GUinterface gui)
    {
        super(gui);
        cContainer = new JPanel();
        cContainer.setLayout(new GridLayout(1,2));
        cGroup = new ButtonGroup();
        numberChoice = new JRadioButton[2];
        for (int i = 0; i < numberChoice.length; i++)
        {
            numberChoice[i] = new JRadioButton(String.valueOf(i+2));
            cGroup.add(numberChoice[i]);
            cContainer.add(numberChoice[i]);
        }
        numberChoice[0].setSelected(true);


        this.setLayout(new GridLayout(3,1));
        text = new JLabel("Define max player number");
        setPlayerNumberbtn = new JButton("Set");
        setPlayerNumberbtn.addActionListener(setPlayerNumber);

        this.add(text);
        this.add(cContainer);
        this.add(setPlayerNumberbtn);
        this.setVisible(true);
    }



    ActionListener setPlayerNumber = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            int maxPlayerNumber = 2;
            for (int i = 0; i < 2; i++)
            {
                if (numberChoice[i].isSelected())
                {
                    maxPlayerNumber = Integer.parseInt(numberChoice[i].getText());
                }
            }

            try {

                gui.createRoom(maxPlayerNumber);
                parent.dispose();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    };
}
