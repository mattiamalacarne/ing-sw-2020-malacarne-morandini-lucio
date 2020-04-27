package it.polimi.ingsw.psp12.view.userinterface;

import it.polimi.ingsw.psp12.client.ClientHandlerConnection;
import it.polimi.ingsw.psp12.client.MessageHandler;
import it.polimi.ingsw.psp12.client.ServerInfo;
import it.polimi.ingsw.psp12.model.board.Board;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;
import it.polimi.ingsw.psp12.network.messages.*;
import it.polimi.ingsw.psp12.view.userinterface.CLI.CLIBoardGenerator;
import it.polimi.ingsw.psp12.view.userinterface.UserInterface;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * CLI interface, manages input and output communication with the user
 * @author Michele Lucio
 */
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
    private ClientHandlerConnection clientHandlerConnection;

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
        cmdIn = new Scanner(System.in);

        this.clientName = setClientName();
        this.serverInfo = setUpServerInfo();

        clientHandlerConnection = new ClientHandlerConnection(serverInfo, "FirstQuestion");
        clientHandlerConnection.run();

        MessageHandler messageHandler = new MessageHandler(this);
        clientHandlerConnection.addObserver(messageHandler);

        this.gamePort = getGamePort(this.serverInfo);
        //TODO: Create new socket with the server now with the correct port
    }


    @Override
    public void writeOnStream(String s)
    {
        System.out.println(s.toUpperCase());
    }

    @Override
    public String setClientName() {

        //TODO: si può fare a meno?
        System.out.println("Name: ");
        String clientName = cmdIn.nextLine();

        return clientName;
    }

    public void setServerInfo(ServerInfo serverInfo) {
        this.serverInfo = serverInfo;
    }

    public void setClientHandlerConnection(ClientHandlerConnection clientHandlerConnection) {
        this.clientHandlerConnection = clientHandlerConnection;
    }

    public void setGamePort(int gamePort) {
        this.gamePort = gamePort;
    }

    @Override
    public ServerInfo setUpServerInfo() throws UnknownHostException {

        System.out.println("Hostname: ");
        String hostname = cmdIn.nextLine();
        //TODO: check hostname
        return new ServerInfo((Inet4Address) Inet4Address.getByName(hostname));

    }

    @Override
    public int getGamePort(ServerInfo serverInfo) throws IOException {
        // Connect to the server, the server (If connection arrive in the default port and with a particular cmd) send back the gamePort (if new room) or a list of gameport (if join)

        System.out.println("Do you want to create or join a room?");
        System.out.println( "1) Create\n2) Join");

        int choice;
        do {
            choice = cmdIn.nextInt();
            switch (choice){

                //Creates a new Room
                case 1:
                    //Room Name
                    System.out.println("Room name: ");
                    String roomName = cmdIn.next();

                    //Room player number
                    System.out.println("Player number [2-4]: ");
                    int playerNumber;
                    do {
                        playerNumber = cmdIn.nextInt();
                        if (playerNumber<2 || playerNumber>4){
                            System.out.println("Choice not allowed, retry");
                        }
                    }while (playerNumber<2 || playerNumber>4);

                    clientHandlerConnection.sendRequestToServer( new CreateMsg(roomName, playerNumber)  );
                    break;

                //Requests list of available room/s
                case 2:
                    clientHandlerConnection.sendRequestToServer( new Message(MsgCommand.LIST) );
                    break;

                default:
                    System.out.println("Choice not allowed, retry");
                    break;
            }

        }while (choice<1 || choice>2);


        // Close the socket
        return serverInfo.serverPort;

    }

    @Override
    public void roomCreatedMessage() throws IOException {
        System.out.println("Room created!");
        clientHandlerConnection.sendRequestToServer( new Message(MsgCommand.LIST) );
    }

    @Override
    public void selectGamePort(RoomsMsg roomList) throws IOException {

        System.out.println("Choose a room where to enter:");
        for (int c=0; c< roomList.getRooms().size(); c++){
            System.out.println(c + "] " + roomList.getRooms().get(c).getName());
        }

        int choice;
        do {
            choice=cmdIn.nextInt();
            if (choice >= roomList.getRooms().size() || choice < 0){
                System.out.println("Choice not allowed, retry");
            }else {
                System.out.println("What's you name: ");
                String playerName = cmdIn.nextLine();
                clientHandlerConnection.sendRequestToServer(new JoinMsg(playerName));

                //New port is updated
                //TODO: controllare correttazza aggiornamento porta
                setGamePort(roomList.getRooms().get(choice).getAssignedPort());
                setClientHandlerConnection( new  ClientHandlerConnection(serverInfo,clientName) );
            }
        }while (choice >= roomList.getRooms().size() || choice < 0);

    }

    @Override
    public void joinPlayerNameConfirmation() throws IOException {
        System.out.println("You have joined the room!");
        String playerName = cmdIn.nextLine();
        //TODO: aspetta o inizia la partita?
//        clientHandlerConnection.sendRequestToServer(new Message() );
    }

    @Override
    public void joinPlayerNameAlreadyUsed() throws IOException {
        System.out.println("That name is already used! Choose another name: ");
        String playerName = cmdIn.nextLine();
        clientHandlerConnection.sendRequestToServer(new JoinMsg(playerName));
    }

    @Override
    public void move(CellListMsg cellListMsg) throws IOException {

        System.out.println("Where do you want to move?");
        for (int c=0; c< cellListMsg.getCellList().size(); c++){
            System.out.println(c + "] " + cellListMsg.getCellList().get(c).getLocation().toString());
        }

        int choice;
        do {
            choice=cmdIn.nextInt();
            if (choice<0 || choice>=cellListMsg.getCellList().size() ){
                System.out.println("Choice not allowed, retry");
            }else{
                //FIXME: dovrebe essere MoveMsg, ma il client sa dove si trova?
                clientHandlerConnection.sendRequestToServer(new SelectCellMsg(cellListMsg.getCellList().get(choice)));
            }
        }while (choice<0 || choice>=cellListMsg.getCellList().size() );
    }

    @Override
    public void build(CellListMsg cellListMsg) throws IOException {

        System.out.println("Where do you want to build?");
        for (int c=0; c< cellListMsg.getCellList().size(); c++){
            System.out.println(c + "] " + cellListMsg.getCellList().get(c).getLocation().toString());
        }

        int choice;
        do {
            choice=cmdIn.nextInt();
            if (choice<0 || choice>=cellListMsg.getCellList().size() ){
                System.out.println("Choice not allowed, retry");
            }else{
                clientHandlerConnection.sendRequestToServer(new BuildMsg(cellListMsg.getCellList().get(choice)));
            }
        }while (choice<0 || choice>=cellListMsg.getCellList().size() );

    }

    @Override
    public void drawBoard(UpdateBoardMsg boardMsg) {
        //TODO: Add code for draw a board on screen.. [così dovrebbe essere ok]
        CLIBoardGenerator boardGenerator = new CLIBoardGenerator(boardMsg.getBoard());
        System.out.println(boardGenerator.toString());
    }
}
