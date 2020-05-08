package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.model.Worker;
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
    private List<Action> actions;

    /**
     * Workers that the player can select
     */
    private List<Worker> workers;

    /**
     * Create the message with the list of actions that can be performed, and the worker that will perform the action
     * @param actions the list of actions
     */
    public ActionsListMsg(List<Action> actions, List<Worker> workers) {
        super(MsgCommand.ACTIONS_LIST);

        this.actions = actions;
        this.workers = workers;
    }

    public List<Action> getActions() {
        return actions;
    }

    public List<Worker> getWorkers() {
        return workers;
    }
}
