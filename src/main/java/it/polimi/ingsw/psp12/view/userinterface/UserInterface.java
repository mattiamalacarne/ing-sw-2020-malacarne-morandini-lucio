package it.polimi.ingsw.psp12.view.userinterface;

import it.polimi.ingsw.psp12.client.ServerInfo;
import it.polimi.ingsw.psp12.model.board.Board;
import it.polimi.ingsw.psp12.network.messages.CellListMsg;
import it.polimi.ingsw.psp12.network.messages.RoomsMsg;
import it.polimi.ingsw.psp12.network.messages.UpdateBoardMsg;

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
    public void writeOnStream(String s);

    /**
     * Setup the information for connect the client to the server
     * @return Data for create the socket
     * @throws UnknownHostException
     */
    public ServerInfo setUpServerInfo() throws UnknownHostException;

    /**
     * Setup the client information, for identify the client in the server
     * @return the client name
     */
    public String setClientName();

    public int getGamePort(ServerInfo serverInfo) throws IOException;

    /**
     * Communicates to the user the correct creation of the room
     */
    public void roomCreatedMessage() throws IOException;

    /**
     * The user chooses the port of game room where to enter
     * @param roomList
     */
    public void selectGamePort(RoomsMsg roomList) throws IOException;

    /**
     * Communicates to the user the correct entry in the room
     */
    public void joinPlayerNameConfirmation() throws IOException;

    /**
     * The user insert the name again, because the previous one is already used
     */
    public void joinPlayerNameAlreadyUsed() throws IOException;

    /**
     * The user chooses the cell where to move
     * @param cellListMsg
     */
    public void move(CellListMsg cellListMsg) throws IOException;

    /**
     * The user chooses the cell where to build
     * @param cellListMsg
     */
    public void build(CellListMsg cellListMsg) throws IOException;

    /**
     * Draw the gameBoard on the screen
     * @param boardMsg
     */
    public void drawBoard(UpdateBoardMsg boardMsg);
}
