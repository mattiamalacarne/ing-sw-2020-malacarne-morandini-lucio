package it.polimi.ingsw.psp12.view.userinterface.GUI.screens.gameUtils;

import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.GameScreen;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.SetUpUtils.DialogContent;
import it.polimi.ingsw.psp12.view.userinterface.GUinterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This class display a panel used by the user for confirm the turn or restart
 * @author Mattia Malacarne
 */

public class ChooseUndoPanel extends DialogContent {

    private JLabel text;
    private JButton confirm, undo;

    private GameScreen game;

    private Timer disposeTimer;

    public ChooseUndoPanel(GUinterface gui, GameScreen game) {
        super(gui);

        this.game = game;
        this.setLayout(new GridLayout(3,1));

        disposeTimer = new Timer();

        text = new JLabel("Do you want to confirm your turn?");
        confirm = new JButton("Confirm");
        undo = new JButton("Undo");

        confirm.addActionListener(confirmEv);
        undo.addActionListener(undoEv);

        this.add(text);
        this.add(confirm);
        this.add(undo);

        // Start the timer
        disposeTimer.schedule(disposeAction, 5200);
    }

    ActionListener confirmEv = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            game.chooseUndo(MsgCommand.CONFIRM_TURN);
            parent.dispose();
        }
    };

    ActionListener undoEv = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            game.chooseUndo(MsgCommand.UNDO_TURN);
            parent.dispose();
        }
    };

    TimerTask disposeAction = new TimerTask() {
        @Override
        public void run() {
            //System.out.println("[DEBUG] Undo timer expired");
            parent.dispose();
        }
    };
}
