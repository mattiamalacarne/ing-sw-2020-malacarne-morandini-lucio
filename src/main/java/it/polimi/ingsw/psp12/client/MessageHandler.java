package it.polimi.ingsw.psp12.client;

import it.polimi.ingsw.psp12.network.messages.*;

/** class for reading a generic message from the server and cast it toa specific one for interpretation
 * @author Mattia Malacarne
 */
public class MessageHandler
{

    private Message message;

    //TODO: Should be a static singleton?
    //TODO: Add documentation

    public MessageHandler(Message gnMsg)
    {
        this.message = gnMsg;
    }

    private MsgCommand setMsgType(Message message)
    {
        return message.getCommand();
    }

    private Message gnericMessageToSpec(Message message)
    {
        MsgCommand cmd = setMsgType(message);
        switch (cmd)
        {
            case MOVE: return (MoveMsg) message;
            case BUILD: return (BuildMsg) message;
            case CELL_REQUEST: return (CellRequestMsg) message;
            case CELL_LIST: return (CellListMsg) message;
            case SELECTED_CELL: return (SelectCellMsg) message;
            case BOARD_UPDATE: return (UpdateBoardMsg) message;
        }
    }

}
