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
    void roomFullMessage() throws IOException;

    /**
     * Communicates to the user the correct entry in the room
     */
    void joinPlayerNameConfirmation();

    /**
     * The user insert the name again, because the previous one is already used
     */
    void joinPlayerNameAlreadyUsed() throws IOException;

    /**
     * The user chooses the card/s for the game
     * @param cardsListMsg The list of available card/s to choose
     */
    void chooseCard(CardsListMsg cardsListMsg);

    /**
     * The user chooses the color and the position of its workers
     * @param requestInfoMsg The list of available color and the available positions to choose
     */
    void requestStartInfo(RequestInfoMsg requestInfoMsg) throws IOException;

    /**
     * Communicates to the user the his god card
     * @param yourCardMsg The god card of the player
     */
    void yourCardMessage(YourCardMsg yourCardMsg);

    /**
     * The user chooses the worker that will perform the action
     * @param workersListMsg The list of possible worker to choose
     */
    void chooseWorker(WorkersListMsg workersListMsg);

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
     * The user chooses what to build in the build action
     */
    void chooseBuildOption(OptionsListMsg optionsListMsg);

    /**
     * The user chooses if to use the undo option to undo the turn
     */
    void chooseUndo();

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

    /**
     * Communicates to the user that it's not his turn
     */
    void notYourTurnMessage();

    /**
     * Communicates to the user that the game is ended
     * and the connection with the game server is closing,
     * return then to the Play-Exit menu
     */
    void closeGameMessage();


}
