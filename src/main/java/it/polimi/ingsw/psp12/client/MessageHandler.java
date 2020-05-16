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

    /**
     * Server information for build the socket
     */
    private ServerInfo serverInfo;

    /**
     * Connection to the server
     */
    private ClientHandlerConnection clientHandlerConnection;

    /**
     * The real game port, obtained from the server after the first connection
     */
    private int gamePort;

    public MessageHandler(UserInterface userInterface) throws IOException {

        this.userInterface = userInterface;

        this.serverInfo = userInterface.getServerByIp();

        clientHandlerConnection = new ClientHandlerConnection(serverInfo);
        clientHandlerConnection.addObserver(this);
        Thread thread = new Thread(clientHandlerConnection);
        thread.start();

    }

//    /**
//     * Start the game communication with the user interface
//     * @throws IOException IO Exception
//     */
//    public void startGame() throws IOException {
//        userInterface.getGamePort();
//    }

    /**
     * Update the current game port (the port by which the client communicates with the server)
     * @param gamePort the port of the game server
     * @throws IOException IO Exception
     */
    public void setGamePort(int gamePort) throws IOException {

        //ServerInfo of the selected room
        ServerInfo updatedServerInfo = new ServerInfo(this.serverInfo.serverIp, gamePort);

        //Closes the socket of the lobby (Acceptance room)
        clientHandlerConnection.close();

        //New ClientHandlerConnection that opens a new socket on the port of the new room
        clientHandlerConnection = new ClientHandlerConnection(updatedServerInfo);
        clientHandlerConnection.addObserver(this);

        Thread thread = new Thread(clientHandlerConnection);
        thread.start();

        serverInfo = updatedServerInfo;

        this.gamePort = gamePort;
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
            case REQUEST_CREATE:
                userInterface.createsNewRoom();
                break;
            case WAIT:
                userInterface.waitMessage();
                break;
            case CREATED:
                userInterface.roomCreatedMessage((CreatedMsg) message );
                break;
            case CREATE_FAILED:
                //should never be here
                userInterface.roomCreatedFailedMessage();
                break;
            case INVALID_MAX_PLAYERS:
                //should never be here
                userInterface.invalidMaxPlayerMessage();
                break;
            case ROOM_FULL:
                userInterface.roomFull();
                break;
            case INVALID_NICKNAME:
                userInterface.joinPlayerNameAlreadyUsed();
                break;
            case JOINED:
                userInterface.joinPlayerNameConfirmation();
                break;
            case REQUEST_INFO:
                userInterface.requestStartInfo( (RequestInfoMsg) message );
                break;
            case PING:
                break;

            //Game commands
            case BOARD_UPDATE:
                userInterface.updateBoard( (UpdateBoardMsg) message );
                break;
            case ACTIONS_LIST:
                userInterface.chooseAction( (ActionsListMsg) message );
                break;
            case CELL_LIST:
                userInterface.chooseCell( (CellListMsg) message );
                break;
            case TURN_ENDED:
                userInterface.endTurnMessage();
                break;
            case YOU_WON:
                userInterface.youWonMessage();
                break;
            case YOU_LOST:
                userInterface.youLostMessage();
                break;
            case OTHER_LOST:
                userInterface.otherPlayerLostMessage( (OtherLostMsg) message );
                break;



            //FIXME: Deprecati
            case ROOMS:
                userInterface.selectGamePort( (RoomsMsg) message );
                break;
            case MOVE:
                userInterface.move( (CellListMsg) message );
                break;
            case BUILD:
                userInterface.build( (CellListMsg) message );
                break;
            case CELL_REQUEST:  /*(CellRequestMsg) message;*/
                break;
            case SELECTED_CELL:  /*(SelectCellMsg) message;*/
                break;

            default: throw new MessageTypeNotFoundException();
        }
    }

    /**
     * Send a message to clientHandlerConnection that will send it to the server
     * @param message the message to send to the server
     */
    public void sendToServer(Message message)  {
        clientHandlerConnection.sendRequestToServer(message);
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
