package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.model.Worker;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;

import java.util.List;

/**
 * Class for sending to the client a list of workers that can be selected
 * @author Luca Morandini
 */
public class WorkersListMsg extends Message {
    /**
     * Workers that the player can select
     */
    private List<Worker> workers;

    /**
     * Create the message with the list of workers that can be selected
     * @param workers workers to be selected
     */
    public WorkersListMsg(List<Worker> workers) {
        super(MsgCommand.WORKERS_LIST);

        this.workers = workers;
    }

    public List<Worker> getWorkers() {
        return workers;
    }
}
