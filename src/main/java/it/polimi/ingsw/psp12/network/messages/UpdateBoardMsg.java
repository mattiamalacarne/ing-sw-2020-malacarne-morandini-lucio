package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.model.board.Board;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;

/**
 * Class for send a board to draw from server to client
 * @author Mattia Malacarne
 */
public class UpdateBoardMsg extends Message
{
    private Board board;

    /**
     * Init the message with the board obtained after a move/build
     * @param cmd the command for the client
     * @param newBoard the board for the client
     */
    public UpdateBoardMsg(MsgCommand cmd, Board newBoard) {
        super(MsgCommand.BOARD_UPDATE);
        this.board = newBoard;
    }
    // Getter
    public Board getBoard()
    {
        return this.board;
    }
}
