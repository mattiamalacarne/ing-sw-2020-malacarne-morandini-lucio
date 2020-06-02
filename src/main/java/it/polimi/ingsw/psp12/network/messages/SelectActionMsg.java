package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.model.enumeration.Action;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;

/**
 * Class used to send to the server the selected action and the worker to be used in the current turn
 * @author Luca Morandini
 */
public class SelectActionMsg extends Message{
    /**
     * Selected action
     */
    private Action action;

    /**
     * Selected worker for the current turn
     * @deprecated
     */
    private int worker;

    /**
     * Create the message with the action and worker selected by the user
     * @param action selected action
     * @param worker selected worker
     * @deprecated
     */
    public SelectActionMsg(Action action, int worker) {
        super(MsgCommand.SELECTED_ACTION);

        this.action = action;
        this.worker = worker;
    }

    /**
     * Create the message with the action selected by the user
     * @param action selected action
     */
    public SelectActionMsg(Action action) {
        this(action, -1);
    }

    public Action getAction() {
        return action;
    }

    /**
     * @deprecated
     */
    public int getWorker() {
        return worker;
    }
}
