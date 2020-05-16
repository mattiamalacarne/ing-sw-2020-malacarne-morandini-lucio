package it.polimi.ingsw.psp12.view.userinterface;

import it.polimi.ingsw.psp12.client.MessageHandler;
import it.polimi.ingsw.psp12.client.ServerInfo;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;
import it.polimi.ingsw.psp12.network.messages.*;
import it.polimi.ingsw.psp12.view.userinterface.CLI.CLIBoardGenerator;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
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

//    private Boolean isWaiting;

    /**
     * CLI constructor
     */
    public CLInterface() throws IOException {

        System.out.println("Starting CLI, Setup server and client");
        cmdIn = new Scanner(System.in);
        messageHandler = new MessageHandler(this);

//        messageHandler.startGame();

    }

    @Override
    public void writeOnStream(String s)
    {
        System.out.println(s.toUpperCase());
    }

    @Override
    public ServerInfo getServerByIp() {

        System.out.println("(IP) Hostname: ");
        ServerInfo serverInfo;

        //FIXME: gestire caso in cui l'iserimento errato è numerico
        do {
            cmdIn = new Scanner(System.in);
            try {
                String hostname = cmdIn.nextLine();
                serverInfo = new ServerInfo((Inet4Address) Inet4Address.getByName(hostname));
            } catch (UnknownHostException e) {
                System.out.println("Invalid host address, retry...");
                serverInfo = null;
            }
        } while (serverInfo==null);

        return serverInfo;
    }

    @Override
    public void createsNewRoom() {

        System.out.println("\nThere are no available room to join!\nCreate a new room to start playing");

        //Room player number
        System.out.println("Player number [2-3]:");
        int playerNumber;
        do {
            cmdIn=new Scanner(System.in);
            try {
                playerNumber = cmdIn.nextInt();
            } catch (InputMismatchException e) {
                playerNumber=0;
            }
            if (playerNumber<2 || playerNumber>3){
                System.out.println("Choice not allowed, retry");
            }
        }while (playerNumber<2 || playerNumber>3);

        messageHandler.sendToServer( new CreateMsg(playerNumber)  );


//        System.out.println("\nDo you want to create or join a room?");
//        System.out.println( "1) Create\n2) Join");

//        int choice;
//        do {
//
//            cmdIn = new Scanner(System.in);
//            try {
//                choice = cmdIn.nextInt();
//            } catch (InputMismatchException e) {
//                choice = 0;
//            }
//
//            switch (choice){
//
//                //Creates a new Room
//                case 1:
//                    //Room Name
//                    System.out.println("Room name: ");
//                    cmdIn=new Scanner(System.in);
//                    String roomName = cmdIn.nextLine();
//
//                    //Room player number
//                    System.out.println("Player number [2-3]: ");
//                    int playerNumber;
//                    do {
//                        cmdIn=new Scanner(System.in);
//                        try {
//                            playerNumber = cmdIn.nextInt();
//                        } catch (InputMismatchException e) {
//                            playerNumber=0;
//                        }
//                        if (playerNumber<2 || playerNumber>3){
//                            System.out.println("Choice not allowed, retry");
//                        }
//                    }while (playerNumber<2 || playerNumber>3);
//
//                    messageHandler.sendToServer( new CreateMsg(roomName, playerNumber)  );
//                    break;
//
//                //Requests list of available room/s
//                case 2:
//                    messageHandler.sendToServer( new Message(MsgCommand.LIST) );
//                    break;
//
//                default:
//                    System.out.println("Choice not allowed, retry");
//                    break;
//            }
//
//        }while (choice<1 || choice>2);

    }

    @Override
    public void waitMessage() {
        System.out.println("Wait while all the players joined the room");
    }

    @Override
    public void roomCreatedMessage(CreatedMsg createdMsg) throws IOException {
//        System.out.printf("Room \"%s\" for %d players created!\n",
//                createdMsg.getRoom().getName(), createdMsg.getRoom().getMaxPlayersCount());
//        getGamePort();
        System.out.printf("Room for %d players created\n", createdMsg.getRoom().getMaxPlayersCount());

        //New port is updated
        messageHandler.setGamePort(createdMsg.getRoom().getAssignedPort());

        System.out.println("What's you name: ");
        cmdIn= new Scanner(System.in);
        String playerName = cmdIn.nextLine();
        messageHandler.sendToServer(new JoinMsg(playerName));
    }

    @Override
    public void roomCreatedFailedMessage() {
        System.out.println("Room creation failed!");
    }

    @Override
    public void invalidMaxPlayerMessage() {
        System.out.println("Invalid player number!");
    }

    @Override
    public void selectGamePort(RoomsMsg roomList) throws IOException {

        if (!roomList.getRooms().isEmpty()) {
            System.out.println("\nChoose a room where to enter:");
            System.out.println("0) Return to create/join room");
            for (int c=1; c<= roomList.getRooms().size(); c++){
                System.out.printf("%d) %s [%d/%d] \n" , c , roomList.getRooms().get(c-1).getName() ,
                                                            roomList.getRooms().get(c-1).getPlayersCount() ,
                                                            roomList.getRooms().get(c-1).getMaxPlayersCount()
                );
            }

            int choice;
            do {
                cmdIn = new Scanner(System.in);
                try {
                    choice=cmdIn.nextInt();
                } catch (InputMismatchException e) {
                    choice=-1;
                }
                if (choice > roomList.getRooms().size() || choice < 0){
                    System.out.println("Choice not allowed, retry");
                }else {

                    if (choice == 0) {
                        createsNewRoom();
                    } else {
                        //New port is updated
                        messageHandler.setGamePort(roomList.getRooms().get(choice-1).getAssignedPort());

                        System.out.println("What's you name: ");
                        cmdIn= new Scanner(System.in);
                        String playerName = cmdIn.nextLine();
                        messageHandler.sendToServer(new JoinMsg(playerName));
                    }
                }
            }while (choice > roomList.getRooms().size() || choice < 0);

        } else {
            System.out.println("There are no available room to join");
            createsNewRoom();
        }

    }

    @Override
    public void roomFull() throws IOException {
        System.out.println("This room is already full! Choose another room");
        messageHandler.sendToServer( new Message(MsgCommand.LIST) );
    }

    @Override
    public void joinPlayerNameConfirmation() {
        System.out.println("You have joined the room!\n");
//        isWaiting = true;
//        while (isWaiting){
//            //TODO: stampa messaggio di waiting ?
//        }
    }

//    public void setIsWaiting(Boolean bool){
//        this.isWaiting = bool;
//    }

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
            System.out.printf("%d) %s\n", c, requestInfoMsg.getAvailableColors().get(c).toString());
        }
        int colorChoice;
        do {
            cmdIn = new Scanner(System.in);
            try {
                colorChoice=cmdIn.nextInt();
            } catch (InputMismatchException e) {
                colorChoice=-1;
            }
            if ( colorChoice<0 || colorChoice>=requestInfoMsg.getAvailableColors().size() ){
                System.out.println("Choice not allowed, retry");
            }
        }while ( colorChoice<0 || colorChoice>=requestInfoMsg.getAvailableColors().size() );

        System.out.println("Choose the position of the first worker:");
        for (int c=0; c<requestInfoMsg.getAvailablePositions().size(); c++){
            System.out.printf("%2d) %s\n", c, requestInfoMsg.getAvailablePositions().get(c).toString());
        }
        int worker1Position;
        do {
            cmdIn = new Scanner(System.in);
            try {
                worker1Position = cmdIn.nextInt();
            } catch (InputMismatchException e) {
                worker1Position=-1;
            }
            if (worker1Position<0 || worker1Position>=requestInfoMsg.getAvailablePositions().size()){
                System.out.println("Choice not allowed, retry");
            }
        }while (worker1Position<0 || worker1Position>=requestInfoMsg.getAvailablePositions().size());

        //TODO: rimuovere la cella scelta direttamente dalla lista

        //FIXME: va bene fare qui il controllo sulla scelta della stessa cella da parte dell'utente? (worker1Position==worker2Position)
        System.out.println("Choose the position of the second worker:");
        for (int c=0; c<requestInfoMsg.getAvailablePositions().size(); c++){
            System.out.printf("%2d) %s\n", c, requestInfoMsg.getAvailablePositions().get(c).toString());
        }
        int worker2Position;
        do {
            cmdIn = new Scanner(System.in);
            try {
                worker2Position = cmdIn.nextInt();
            } catch (InputMismatchException e) {
                worker2Position=-1;
            }
            if (worker2Position<0 || worker2Position>=requestInfoMsg.getAvailablePositions().size() || worker1Position==worker2Position){
                System.out.println("Choice not allowed, retry");
            }
        }while (worker2Position<0 || worker2Position>=requestInfoMsg.getAvailablePositions().size() || worker1Position==worker2Position);

        messageHandler.sendToServer( new PlayerInfoMsg( requestInfoMsg.getAvailableColors().get(colorChoice),
                                                        requestInfoMsg.getAvailablePositions().get(worker1Position),
                                                        requestInfoMsg.getAvailablePositions().get(worker2Position) ) );
    }

    @Override
    public void chooseAction(ActionsListMsg actionsListMsg) throws IOException {

        System.out.println("Choose the next action: ");
        for (int c=0; c<actionsListMsg.getActions().size(); c++){
            System.out.printf("%d) %s\n", c, actionsListMsg.getActions().get(c));
        }

        int actionChoice;
        do {
            cmdIn = new Scanner(System.in);
            try {
                actionChoice=cmdIn.nextInt();
            } catch (InputMismatchException e) {
                actionChoice = -1;
            }
            if (actionChoice<0 || actionChoice>=actionsListMsg.getActions().size()){
                System.out.println("Choice not allowed, retry");
            }
        }while (actionChoice<0 || actionChoice>=actionsListMsg.getActions().size());

        if (actionsListMsg.mustSelectWorker()) {

            System.out.println("Choose the worker you want to move:");
            for (int c=0; c<actionsListMsg.getWorkers().size(); c++){
                System.out.printf("%d) Worker at position %s\n", c, actionsListMsg.getWorkers().get(c).getPosition().toString());
            }

            int workerChoice;
            do {
                cmdIn = new Scanner(System.in);
                try {
                    workerChoice = cmdIn.nextInt();
                } catch (InputMismatchException e) {
                    workerChoice = -1;
                }
                if (workerChoice<0 || workerChoice>=actionsListMsg.getWorkers().size()){
                    System.out.println("Choice not allowed, retry");
                }
            }while (workerChoice<0 || workerChoice>=actionsListMsg.getWorkers().size());

            messageHandler.sendToServer( new SelectActionMsg(actionsListMsg.getActions().get(actionChoice), workerChoice) );

        } else {
            messageHandler.sendToServer( new SelectActionMsg(actionsListMsg.getActions().get(actionChoice)) );
        }

    }

    @Override
    public void chooseCell(CellListMsg cellListMsg) throws IOException {

        System.out.printf("Choose the cell where to do the %s action\n", cellListMsg.getAction());
        for (int c=0; c<cellListMsg.getCellList().size(); c++){
            System.out.printf("%2d) %s\n", c, cellListMsg.getCellList().get(c).getLocation().toString());
        }
        int choice;
        do {
            cmdIn = new Scanner(System.in);
            try {
                choice = cmdIn.nextInt();
            } catch (InputMismatchException e) {
                choice = -1;
            }
            if (choice<0 || choice>=cellListMsg.getCellList().size()){
                System.out.println("Choice not allowed, retry");
            }
        }while (choice<0 || choice>=cellListMsg.getCellList().size());

        messageHandler.sendToServer( new SelectCellMsg(cellListMsg.getCellList().get(choice)) );

    }

    @Override
    public void endTurnMessage() {
        System.out.print("Your turn is ended!\n");
    }


    @Deprecated
    @Override
    public void move(CellListMsg cellListMsg) throws IOException {

        System.out.println("Where do you want to move?");
        for (int c=0; c< cellListMsg.getCellList().size(); c++){
            System.out.printf("%d) %s\n", c, cellListMsg.getCellList().get(c).getLocation().toString());
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

    @Deprecated
    @Override
    public void build(CellListMsg cellListMsg) throws IOException {

        System.out.println("Where do you want to build?");
        for (int c=0; c< cellListMsg.getCellList().size(); c++){
            System.out.printf("%d) %s\n", c, cellListMsg.getCellList().get(c).getLocation().toString());
        }

        int choice;
        do {
            choice=cmdIn.nextInt();
            if (choice<0 || choice>=cellListMsg.getCellList().size() ){
                System.out.println("Choice not allowed, retry");
            }else{
                //messageHandler.sendToServer(new BuildMsg(cellListMsg.getCellList().get(choice)));
            }
        }while (choice<0 || choice>=cellListMsg.getCellList().size() );

    }

    @Override
    public void updateBoard(UpdateBoardMsg updateBoardMsg) {
        System.out.println( ( new CLIBoardGenerator(updateBoardMsg.getBoard()) ).toString() );
    }

    @Override
    public void youWonMessage() {
        System.out.println("********** You are the winner! **********");
    }

    @Override
    public void youLostMessage() {
        System.out.println("You lost :(");
    }

    @Override
    public void otherPlayerLostMessage(OtherLostMsg otherLostMsg) {
        System.out.printf("The player %s has lost\n", otherLostMsg.getPlayer());
    }

}
