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
     */
    ServerInfo getServerByIp() throws UnknownHostException;

    /**
     * The user creates a new room
     */
    void createsNewRoom() throws IOException;

    /**
     * Communicates to the user to wait while the room is full
     */
    void waitMessage();

    /**
     * Communicates to the user the correct creation of the room
     * @param createdMsg The created room
     * @throws IOException IO Exception
     */
    void roomCreatedMessage(CreatedMsg createdMsg) throws IOException;

    /**
     * Communicates to the user the incorrect creation of the room
     */
    void roomCreatedFailedMessage();

    /**
     * Communicates to the user the incorrect selection of the player number for the room
     */
    void invalidMaxPlayerMessage();

    /**
     * Communicates to the user that the chosen room is already full
     */
    void roomFull() throws IOException;

    /**
     * Communicates to the user the correct entry in the room
     */
    void joinPlayerNameConfirmation();

    /**
     * The user insert the name again, because the previous one is already used
     */
    void joinPlayerNameAlreadyUsed() throws IOException;

    /**
     * The user chooses the color and the position of its workers
     * @param requestInfoMsg The list of available color and the available positions to choose
     */
    void requestStartInfo(RequestInfoMsg requestInfoMsg) throws IOException;

    /**
     * The user chooses the action to perform
     * @param actionsListMsg The list of possible action to perform
     */
    void chooseAction(ActionsListMsg actionsListMsg) throws IOException;

    /**
     * The user chooses the cell where to perform the action
     * @param cellListMsg The list of cell where to perform the action, and the action to perform
     */
    void chooseCell(CellListMsg cellListMsg) throws IOException;

    /**
     * Communicates to the user the end of the own turn
     */
    void endTurnMessage();

    /**
     * Update the shown information of the board
     */
    void updateBoard(UpdateBoardMsg updateBoardMsg);

    /**
     * Communicates to the user the that has won the game
     */
    void youWonMessage();

    /**
     * Communicates to the user the that has lost the game
     */
    void youLostMessage();

    /**
     * Communicates to the user that a player in the room has lost
     * @param otherLostMsg The player that has lost
     */
    void otherPlayerLostMessage(OtherLostMsg otherLostMsg);

}
