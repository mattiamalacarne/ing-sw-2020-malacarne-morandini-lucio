package it.polimi.ingsw.psp12.view.userinterface;

import it.polimi.ingsw.psp12.client.MessageHandler;
import it.polimi.ingsw.psp12.client.ServerInfo;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;
import it.polimi.ingsw.psp12.network.messages.*;
import it.polimi.ingsw.psp12.view.userinterface.CLI.CLIBoardGenerator;

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

    /**
     * The input scanner
     */
    private Scanner cmdIn;

    /**
     * MessageHandler that will manages the communication which the server
     */
    private MessageHandler messageHandler;

    /**
     * CLI constructor
     * @throws UnknownHostException Unknown Host Exception
     */
    public CLInterface() throws IOException {

        System.out.println("Starting CLI, Setup server and client");
        cmdIn = new Scanner(System.in);
        messageHandler = new MessageHandler(this);
        messageHandler.startGame();

    }

    @Override
    public void writeOnStream(String s)
    {
        System.out.println(s.toUpperCase());
    }

    @Override
    public ServerInfo getServerByIp() throws UnknownHostException {

        System.out.println("(IP) Hostname: ");
        String hostname = cmdIn.nextLine();
        //TODO: check hostname
        return new ServerInfo((Inet4Address) Inet4Address.getByName(hostname));

    }

    @Override
    public void getGamePort() throws IOException {

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
                    cmdIn=new Scanner(System.in);
                    String roomName = cmdIn.nextLine();

                    //Room player number
                    System.out.println("Player number [2-3]: ");
                    int playerNumber;
                    do {
                        cmdIn=new Scanner(System.in);
                        playerNumber = cmdIn.nextInt();
                        if (playerNumber<2 || playerNumber>3){
                            System.out.println("Choice not allowed, retry");
                        }
                    }while (playerNumber<2 || playerNumber>3);

                    messageHandler.sendToServer( new CreateMsg(roomName, playerNumber)  );
                    break;

                //Requests list of available room/s
                case 2:
                    messageHandler.sendToServer( new Message(MsgCommand.LIST) );
                    break;

                default:
                    System.out.println("Choice not allowed, retry");
                    break;
            }

        }while (choice<1 || choice>2);

    }

    @Override
    public void roomCreatedMessage() throws IOException {
        System.out.println("Room created!\n");
        getGamePort();
    }

    @Override
    public void selectGamePort(RoomsMsg roomList) throws IOException {

        if (!roomList.getRooms().isEmpty()) {
            //TODO: inserire possibilità di ritornare alla creazione stanze
            System.out.println("Choose a room where to enter:");
            for (int c=0; c< roomList.getRooms().size(); c++){
                System.out.println(c + "] " + roomList.getRooms().get(c).getName() +
                        " [" + roomList.getRooms().get(c).getPlayersCount() + "/" +
                               roomList.getRooms().get(c).getMaxPlayersCount() + "]");
            }

            int choice;
            do {
                choice=cmdIn.nextInt();
                if (choice >= roomList.getRooms().size() || choice < 0){
                    System.out.println("Choice not allowed, retry");
                }else {
                    //New port is updated
                    messageHandler.setGamePort(roomList.getRooms().get(choice).getAssignedPort());

                    System.out.println("What's you name: ");
                    cmdIn= new Scanner(System.in);
                    String playerName = cmdIn.nextLine();
                    messageHandler.sendToServer(new JoinMsg(playerName));
                }
            }while (choice >= roomList.getRooms().size() || choice < 0);

        } else {
            System.out.println("There are no available room to join");
            getGamePort();
        }

    }

    @Override
    public void roomFull() throws IOException {
        System.out.println("This room is already full! Choose another room");
        messageHandler.sendToServer( new Message(MsgCommand.LIST) );
    }

    @Override
    public void joinPlayerNameConfirmation() {
        System.out.println("You have joined the room!");
    }

    @Override
    public void joinPlayerNameAlreadyUsed() throws IOException {
        System.out.println("That name is already used! Choose another name: ");
        String playerName = cmdIn.nextLine();
        messageHandler.sendToServer(new JoinMsg(playerName));
    }

    @Override
    public void requestStartInfo(RequestInfoMsg requestInfoMsg) throws IOException {

        System.out.println("Choose a color:");
        for (int c=0; c<requestInfoMsg.getAvailableColors().size();c++){
            System.out.println(c + "] " + requestInfoMsg.getAvailableColors().get(c).toString());
        }
        int colorChoice;
        do {
            colorChoice=cmdIn.nextInt();
            if ( colorChoice<0 || colorChoice>=requestInfoMsg.getAvailableColors().size() ){
                System.out.println("Choice not allowed, retry");
            }
        }while ( colorChoice<0 || colorChoice>=requestInfoMsg.getAvailableColors().size() );

        System.out.println("Choose the position of the first worker:");
        for (int c=0; c<requestInfoMsg.getAvailablePositions().size(); c++){
            System.out.printf("%2d] %s\n", c, requestInfoMsg.getAvailablePositions().get(c).toString());
        }
        int worker1Position;
        do {
            worker1Position = cmdIn.nextInt();
            if (worker1Position<0 || worker1Position>requestInfoMsg.getAvailablePositions().size()){
                System.out.println("Choice not allowed, retry");
            }
        }while (worker1Position<0 || worker1Position>requestInfoMsg.getAvailablePositions().size());

        //FIXME: va bene fare qui il controllo sulla scelta della stessa cella da parte dell'utente? (worker1Position==worker2Position)
        System.out.println("Choose the position of the second worker:");
        for (int c=0; c<requestInfoMsg.getAvailablePositions().size(); c++){
            System.out.printf("%2d] %s\n", c, requestInfoMsg.getAvailablePositions().get(c).toString());
        }
        int worker2Position;
        do {
            worker2Position = cmdIn.nextInt();
            if (worker2Position<0 || worker2Position>requestInfoMsg.getAvailablePositions().size() || worker1Position==worker2Position){
                System.out.println("Choice not allowed, retry");
            }
        }while (worker2Position<0 || worker2Position>requestInfoMsg.getAvailablePositions().size() || worker1Position==worker2Position);

        messageHandler.sendToServer( new PlayerInfoMsg( requestInfoMsg.getAvailableColors().get(colorChoice),
                                                        requestInfoMsg.getAvailablePositions().get(worker1Position),
                                                        requestInfoMsg.getAvailablePositions().get(worker2Position) ) );
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
                messageHandler.sendToServer(new SelectCellMsg(cellListMsg.getCellList().get(choice)));
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
                messageHandler.sendToServer(new BuildMsg(cellListMsg.getCellList().get(choice)));
            }
        }while (choice<0 || choice>=cellListMsg.getCellList().size() );

    }

    @Override
    public void updateBoard(UpdateBoardMsg updateBoardMsg) {
        System.out.println( ( new CLIBoardGenerator(updateBoardMsg.getBoard()) ).toString() );
    }

}
