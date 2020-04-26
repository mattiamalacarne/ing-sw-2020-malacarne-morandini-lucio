package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;

/**
 * Class for send to server a selected cell generally used for request a list of possible move after choosing a worker
 * @author Mattia Malacarne
 */
public class SelectCellMsg extends Message
{
    private Cell selectedCell;

    /**
     * Init the message with a cell
     * @param selectedCell the cell selected by the user
     */
    public SelectCellMsg(Cell selectedCell) {
        super(MsgCommand.SELECTED_CELL);
        this.selectedCell = selectedCell;
    }

    public Cell getSelectedCell()
    {
        return selectedCell;
    }
}
