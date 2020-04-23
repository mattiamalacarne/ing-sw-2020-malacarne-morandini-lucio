package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.model.board.Cell;

/**
 * Class for send to server a selected cell generally used for request a list of possiblemove after choosing a worker
 * @author Mattia Malacarne
 */
public class SelectCellMsg extends Message
{
    private Cell selectedCell;

    /**
     * Init the message with a cell
     * @param cmd the command for server
     * @param selectedCell the cell selected by the user
     */
    public SelectCellMsg(MsgCommand cmd, Cell selectedCell) {
        super(MsgCommand.SELECTED_CELL);
        this.selectedCell = selectedCell;
    }

    public Cell getSelectedCell()
    {
        return selectedCell;
    }
}
