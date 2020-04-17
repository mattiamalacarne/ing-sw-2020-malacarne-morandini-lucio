package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.model.enumeration.Action;
import it.polimi.ingsw.psp12.model.enumeration.MsgType;

/**
 * Class representing a general cell request, used for requesting a cell list
 *
 * @author Mattia Malacarne
 */
public class CellRequestMsg extends Message
{

    // TODO: Controllare, si possono unire la richiesta di celle e move action poichè dipendono l'uno dall'altro

    private Action action;
    /**
     * Request a list of cell where build or move is possible
     * @param cmd command
     * @param action define if build cell list or move cell list (Da vedere potrebbe non serive)
     */
    public CellRequestMsg(String cmd, Action action) {
        super(cmd);
        this.type = MsgType.STATE;
        this.action = action;
    }
}
