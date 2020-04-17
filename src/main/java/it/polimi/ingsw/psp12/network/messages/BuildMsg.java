package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.enumeration.MsgType;

/**
 * Class epresenting the message related to a build intention
 *
 * @author Mattia Malacarne
 */
public class BuildMsg extends Message
{

    private Cell toBuild;

    /**
     * Build a message containing the cell where the player want to build
     * @param cmd
     * @param toBuild
     */
    public BuildMsg(String cmd, Cell toBuild)
    {
        super(cmd);
        this.type = MsgType.STATE;
        this.toBuild = toBuild;
    }
}
