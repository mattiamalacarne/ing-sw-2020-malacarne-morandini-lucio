package it.polimi.ingsw.psp12.client;

import it.polimi.ingsw.psp12.exceptions.MessageTypeNotFoundException;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;
import it.polimi.ingsw.psp12.network.messages.*;
import it.polimi.ingsw.psp12.utils.Observer;
import it.polimi.ingsw.psp12.view.userinterface.UserInterface;

import java.io.IOException;

/** class for reading a generic message from the server and cast it toa specific one for interpretation
 * @author Mattia Malacarne
 * @author Michele Lucio
 */
public class MessageHandler implements Observer<Message>
{

    /**
     * UserInterface that will receive and manage the message
     */
    private UserInterface userInterface;

    public MessageHandler(UserInterface userInterface)
    {
        this.userInterface = userInterface;
    }

    /**
     * Returns the command of the message
     * @param message The message
     * @return Command of the message
     */
    private MsgCommand getMsgType(Message message)
    {
        return message.getCommand();
    }

    /**
     * Receive a message and cast it to its original type,
     * in order to use the appropriate method on the UserInterface
     * @param message The received message
     * @throws MessageTypeNotFoundException Message not identified exception
     * @throws IOException IO exception
     */
    private void genericMessageToSpec(Message message) throws MessageTypeNotFoundException, IOException {
        MsgCommand cmd = getMsgType(message);
        switch (cmd)
        {
            //System commands
            case CREATED:
                userInterface.roomCreatedMessage();
                break;
            case ROOMS:
                userInterface.selectGamePort( (RoomsMsg) message );
                break;
            case JOINED:
                userInterface.joinPlayerNameConfirmation();
                break;
            case INVALID_NICKNAME:
                userInterface.joinPlayerNameAlreadyUsed();
                break;

            //Game commands
            case MOVE:
                userInterface.move( (CellListMsg) message);
                break;
            case BUILD:
                userInterface.build( (CellListMsg) message );
                break;

            case CELL_REQUEST:  /*(CellRequestMsg) message;*/
            case CELL_LIST:  /*(CellListMsg) message;*/

            case SELECTED_CELL:  /*(SelectCellMsg) message;*/
            case BOARD_UPDATE:  /*(UpdateBoardMsg) message;*/
            default: throw new MessageTypeNotFoundException();
        }
    }

    @Override
    public void update(Object sender, Message message) {
        try {
            genericMessageToSpec(message);
        } catch (MessageTypeNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
