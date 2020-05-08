package it.polimi.ingsw.psp12.view.userinterface;

import it.polimi.ingsw.psp12.client.ServerInfo;
import it.polimi.ingsw.psp12.network.messages.*;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * <p>This <b>Interface</b> is the user interface manager</p>
 *
 * @author Mattia Malacarne
 * @author Michele Lucio
 */

public interface UserInterface
{
    /**
     * Write a string on the standard output
     * @param s the string to write
     */
    void writeOnStream(String s);

    /**
     * The user chooses the IP of the to server to which it wants to connect
     * @return Data for create the socket
     * @throws UnknownHostException Unknown Host Exception
     */
    ServerInfo getServerByIp() throws UnknownHostException;

    /**
     * The user chooses to create or to join an available room
     * @throws IOException IO Exception
     */
    void getGamePort() throws IOException;

    /**
     * Communicates to the user the correct creation of the room
     * @throws IOException IO Exception
     */
    void roomCreatedMessage() throws IOException;

    /**
     * The user chooses the port of game room where to enter
     * @param roomList The list of available room/s
     * @throws IOException IO Exception
     */
    void selectGamePort(RoomsMsg roomList) throws IOException;

    /**
     * Communicates to the user that the chosen room is already full
     * @throws IOException IO Exception
     */
    void roomFull() throws IOException;

    /**
     * Communicates to the user the correct entry in the room
     */
    void joinPlayerNameConfirmation();

    /**
     * The user insert the name again, because the previous one is already used
     * @throws IOException IO Exception
     */
    void joinPlayerNameAlreadyUsed() throws IOException;

    /**
     * The user chooses the color and the position of its workers
     * @param requestInfoMsg The list of available color and the available positions to choose
     * @throws IOException IO Exception
     */
    void requestStartInfo(RequestInfoMsg requestInfoMsg) throws IOException;

    /**
     * The user chooses the action to perform
     * @param actionsListMsg The list of possible action to perform
     * @throws IOException IO Exception
     */
    void chooseAction(ActionsListMsg actionsListMsg) throws IOException;

    /**
     * The user chooses the cell where to perform the action
     * @param cellListMsg The list of cell where to perform the action, and the action to perform
     * @throws IOException IO Exception
     */
    void chooseCell(CellListMsg cellListMsg) throws  IOException;

    /**
     * Communicates to the user the end of the own turn
     */
    void endTurnMessage();


    /**
     * The user chooses the cell where to move
     * @param cellListMsg The list of available cell/s where it's possible to move
     * @throws IOException IO Exception
     */
    @Deprecated
    void move(CellListMsg cellListMsg) throws IOException;

    /**
     * The user chooses the cell where to build
     * @param cellListMsg The list of available cell/s where it's possible to build
     * @throws IOException IO Exception
     */
    @Deprecated
    void build(CellListMsg cellListMsg) throws IOException;

    /**
     * Update the shown information of the board
     */
    void updateBoard(UpdateBoardMsg updateBoardMsg);

}
