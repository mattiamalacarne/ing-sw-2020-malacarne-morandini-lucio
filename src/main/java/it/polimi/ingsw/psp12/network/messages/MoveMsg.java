package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.model.board.Cell;

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
     * @param from the cell where the worker is
     * @param to the new cell for the worker
     */
    public MoveMsg(Cell from, Cell to) {
        super(MsgType.GAME);
        this.from = from;
        this.to = to;
    }
}
