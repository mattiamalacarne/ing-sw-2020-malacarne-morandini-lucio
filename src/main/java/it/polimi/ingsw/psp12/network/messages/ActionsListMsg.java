package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.model.enumeration.Action;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;

import java.util.List;

/**
 * Class for sending to the client a list of actions that can be performed
 * @author Luca Morandini
 */
public class ActionsListMsg extends Message {
    /**
     * Actions that the player can select
     */
    private final List<Action> actions;

    /**
     * Create the message with the list of actions that can be performed
     * @param actions the list of actions
     */
    public ActionsListMsg(List<Action> actions) {
        super(MsgCommand.ACTIONS_LIST);

        this.actions = actions;
    }

    public List<Action> getActions() {
        return actions;
    }
}
