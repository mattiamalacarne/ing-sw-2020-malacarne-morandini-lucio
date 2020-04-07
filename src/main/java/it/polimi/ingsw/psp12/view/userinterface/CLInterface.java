package it.polimi.ingsw.psp12.view.userinterface;

import it.polimi.ingsw.psp12.client.ClientHandlerConnection;
import it.polimi.ingsw.psp12.client.ServerInfo;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Scanner;

public class CLInterface implements UserInterface
{

    private Scanner cmdIn;
    public String clientName;

    /**
     * Server information for build the socket
     */
    private ServerInfo serverInfo;

    /**
     * Connection to the server
     */
    private ClientHandlerConnection con;

    /**
     * The real game port, obtained from the server after the first connection
     */
    private int gamePort;

    /**
     * CLI constructor
     * @throws UnknownHostException
     */
    public CLInterface() throws IOException {
        System.out.print("Starting CLI, Setup server and client");
        this.clientName = this.setClientName();
        this.serverInfo = this.setUpServerInfo();

        this.gamePort = getGamePort(this.serverInfo);
        //TODO: Create new socket with the server now woth the correct port
    }


    @Override
    public void writeOnStream(String s)
    {
        System.out.println(s.toUpperCase());
    }

    @Override
    public ServerInfo setUpServerInfo() throws UnknownHostException {

        String hostname = "";
        System.out.println("Hostname: ");
        hostname = cmdIn.nextLine();

        return new ServerInfo((Inet4Address) Inet4Address.getByName(hostname));
    }

    @Override
    public String setClientName() {

        String clientName = "Player";

        System.out.println("Name: ");
        this.clientName = cmdIn.nextLine();

        return this.clientName;
    }

    @Override
    public int getGamePort(ServerInfo serverInfo) throws IOException {
        // Connect to the server, the server (If connection arrive in the default port and with a particular cmd) send back the gamePort (if new room) or a list of gameport (if join)

        System.out.println("Do you want to create or join a room?");
        System.out.println( "1) Create 2) Join");
        int op = cmdIn.nextInt();

        ClientHandlerConnection connection = new ClientHandlerConnection(serverInfo, "FirstQuestion");
        connection.run();

        connection.sendRequestToServer("getports");
        int gamePort = cmdIn.nextInt();
        // Close the socket
        return gamePort;

    }
}
