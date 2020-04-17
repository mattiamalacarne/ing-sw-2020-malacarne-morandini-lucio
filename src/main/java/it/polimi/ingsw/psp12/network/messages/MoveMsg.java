package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.enumeration.MsgType;

/**
 * Class for representing a move request (client->server)
 *
 * @author Mattia Malacarne
 */
public class MoveMsg extends Message
{

    private Cell from;
    private Cell to;

    /**
     * Construct the message
     * @param cmd the command for the server
     * @param from the cell where the worker is
     * @param to the new cell for the worker
     */
    public MoveMsg(String cmd, Cell from, Cell to) {
        super(cmd);
        this.type = MsgType.STATE;
        this.from = from;
        this.to = to;
    }
}
