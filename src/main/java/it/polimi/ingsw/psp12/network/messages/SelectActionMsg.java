package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.model.enumeration.Action;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;

/**
 * Class used to send to the server the selected action
 * @author Luca Morandini
 */
public class SelectActionMsg extends Message{
    /**
     * Selected action
     */
    private final Action action;

    /**
     * Create the message with the action selected by the user
     * @param action selected action
     */
    public SelectActionMsg(Action action) {
        super(MsgCommand.SELECTED_ACTION);

        this.action = action;
    }

    public Action getAction() {
        return action;
    }
}
