package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.enumeration.Action;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;

import java.util.List;

/**
 * Class for a send to the client a list of cell generated by the server
 * @author Mattia Malacarne
 */
public class CellListMsg extends Message
{
    private final List<Cell> cellList;

    private final Action action;

    /**
     * Init the message with the list of cell created by the server
     * @param listOfCell the list of cell
     * @param action action associated to the list of cells
     */
    public CellListMsg(List<Cell> listOfCell, Action action) {
        super(MsgCommand.CELL_LIST);
        this.cellList = listOfCell;
        this.action = action;
    }

    public List<Cell> getCellList()
    {
        return cellList;
    }

    public Action getAction() {
        return action;
    }
}
