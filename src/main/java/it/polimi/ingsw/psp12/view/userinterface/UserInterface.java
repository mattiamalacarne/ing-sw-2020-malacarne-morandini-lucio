package it.polimi.ingsw.psp12.view.userinterface;

import it.polimi.ingsw.psp12.client.ServerInfo;

import java.io.IOException;
import java.net.UnknownHostException;

/**
 * <p>This <b>Interface</b> is the user interface manager</p>
 *
 * @author mattia
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
}
