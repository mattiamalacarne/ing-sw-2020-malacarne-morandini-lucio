package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;

/**
 * Class used to send to the server the selected worker to be used in the current turn
 * @author Luca Morandini
 */
public class SelectWorkerMsg extends Message {
    /**
     * Selected worker for the current turn
     */
    private final int worker;

    /**
     * Create the message with the worker selected by the user
     * @param worker selected worker
     */
    public SelectWorkerMsg(int worker) {
        super(MsgCommand.SELECTED_WORKER);

        this.worker = worker;
    }

    public int getWorker() {
        return worker;
    }
}
