package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;

/**
 * Class representing the message related to a build intention
 *
 * @author Mattia Malacarne
 */
public class BuildMsg extends Message
{

    private Cell toBuild;

    /**
     * Build a message containing the cell where the player want to build
     * @param toBuild
     */
    public BuildMsg(Cell toBuild)
    {
        super(MsgCommand.BUILD);
        this.toBuild = toBuild;
    }
}
