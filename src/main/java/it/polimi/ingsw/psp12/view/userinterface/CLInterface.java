package it.polimi.ingsw.psp12.view.userinterface;

import it.polimi.ingsw.psp12.client.MessageHandler;
import it.polimi.ingsw.psp12.client.ServerInfo;
import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;
import it.polimi.ingsw.psp12.network.messages.*;
import it.polimi.ingsw.psp12.view.userinterface.CLI.CLIBoardGenerator;

import java.io.*;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
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
     * The input stream reader
     */
    private InputStreamReader inputStreamReader;

    /**
     * Used to know when to terminate the game
     */
    private Boolean exitGame;

    /**
     * Used to synchronize undo request and chooseWorker request
     */
    private final Object cmdLock;

    /**
     * Used to distinguish if to ask or not an input to the player after undo request
     */
    private Boolean discardUndo;

    /**
     * Used to discard close message when closing acceptance server to connect to game server
     */
    private Boolean serverClosedRedirection;

    /**
     * Used to know if the undo scanner is still waiting for an input
     */
    private Boolean undoScannerWaiting;

    /**
     * MessageHandler that will manages the communication which the server
     */
    private MessageHandler messageHandler;

//    private Boolean isWaiting;

    /**
     * Thread dedicated to receive user input
     */
    private Thread scannerThread;

//    private Thread timerThread;

    /**
     * Used to distinguish the board representation on the cli,
     * because the ansi code for text color aren't recognize on some terminal
     */
    private final Boolean coloredBoard;

    /**
     * CLI constructor
     */
    public CLInterface( Boolean coloredBoard ) {

        cmdLock = new Object();
        messageHandler = new MessageHandler(this);

        this.coloredBoard = coloredBoard;
        welcomeMessage();

    }

    /**
     * Gets the input from the user and checks:
     * -if it's a valid input
     * -if it't in a valid range
     *
     * @param list The list used to determine the range size of possible selection
     * @return The selection of the user
     * @throws Throwable If a server disconnection it's detected while waiting for the user input
     * that exception is launched to stop the scanner from waiting for a user input
     */
    private int getUserInput(List<?> list) throws Throwable {

        int returnInput;
        do {
            String input = cmdIn.nextLine();

            //Checks if it's still connected to the server
            if (exitGame){
                closeGameMessage();
                throw new Throwable();
            }

            try {
                returnInput = Integer.parseInt( input );
            } catch (NumberFormatException e) {
                returnInput=-1;
            }
            if (returnInput<0 || returnInput>=list.size()){
                System.out.println("Choice not allowed, retry");
            }
        }while (returnInput<0 || returnInput>=list.size());

        return returnInput;

    }

    /**
     * Welcome game menu
     */
    public void welcomeMessage() {

        inputStreamReader = new InputStreamReader(System.in);
        serverClosedRedirection = false;
        exitGame = false;
        discardUndo = false;

        System.out.print("\n***********************************");
        System.out.print("\n**** Welcome to Santorini game ****");
        System.out.print("\n***********************************");
        System.out.println("\n0) Play\n1) Exit");

        int choice;
        do {
            cmdIn = new Scanner(inputStreamReader);
            try {
                choice = cmdIn.nextInt();
            }catch (InputMismatchException e){
                choice = -1;
            }
            if (choice<0 || choice>1){
                System.out.println("Choice not allowed, retry");
            }
        }while (choice<0 || choice>1);

        if (choice==0){
            //Play
            connect();
        }else {
            //Exit
            System.out.println("Quitting the game");
            System.exit(0);
        }

    }

    /**
     * Tries to connect to the server until a valid ip is entered
     */
    public void connect() {
        // Continue to asks to the user the ip of the server to connect to,
        // until a valid ip is entered
        boolean connected = false;
        do {
            connected = messageHandler.connect(getServerByIp());
            if (!connected) {
                System.out.println("This server doesn't exists, retry...");
            }
        } while (!connected);

        System.out.println("Successfully connected to the server!");
    }

    /**
     * Requests server ip to the user
     * @return server information
     */
    public ServerInfo getServerByIp() {

        System.out.println("(IP) Hostname: ");
        ServerInfo serverInfo;

        do {
            cmdIn = new Scanner(inputStreamReader);
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

        scannerThread = new Thread(() -> {

            System.out.println("\nThere are no available room to join!\nCreate a new room to start playing");

            //Room player number
            System.out.println("Player number [2-3]:");
            int playerNumber;
            do {
                String input = cmdIn.nextLine();

                //Checks if it's still connected to the server
                if (exitGame){
                    closeGameMessage();
                    return;
                }

                try {
                    playerNumber = Integer.parseInt( input );
                } catch (NumberFormatException e) {
                    playerNumber=0;
                }
                if (playerNumber<2 || playerNumber>3){
                    System.out.println("Choice not allowed, retry");
                }
            }while (playerNumber<2 || playerNumber>3);

            messageHandler.sendToServer( new CreateMsg(playerNumber)  );

        });
        scannerThread.start();

    }

    @Override
    public void waitMessage() {
        System.out.println("\nWait while all the players joined the room");
    }

    @Override
    public void roomCreatedMessage(CreatedMsg createdMsg) {

        serverClosedRedirection = true;
        exitGame = false;

        scannerThread = new Thread(() -> {

        System.out.printf("\nRoom for %d players created\n", createdMsg.getRoom().getMaxPlayersCount());

        //New port is updated
        try {
            messageHandler.setGamePort(createdMsg.getRoom().getAssignedPort());
        } catch (IOException e) {
            System.out.println("Error entering the room...");
            serverClosedRedirection = false;
            closeGameMessage();
        }

        System.out.println("What's your name: ");

        String playerName = cmdIn.nextLine();

        //Checks if it's still connected to the server
        if (exitGame){
            closeGameMessage();
            return;
        }

        messageHandler.sendToServer(new JoinMsg(playerName));

        });
        scannerThread.start();

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
    public void roomFullMessage() {
        System.out.println("This room is already full! Choose another room");
    }

    @Override
    public void joinPlayerNameConfirmation() {
        System.out.println("You have joined the room!\n");
//        isWaiting = true;
//        while (isWaiting){
//            //TODO: print waiting message ?
//        }
    }

//    public void setIsWaiting(Boolean bool){
//        this.isWaiting = bool;
//    }

    @Override
    public void joinPlayerNameAlreadyUsed() {

        scannerThread = new Thread(() -> {

            System.out.println("That name is already used! Choose another name: ");
            String playerName = cmdIn.nextLine();

            //Checks if it's still connected to the server
            if (exitGame){
                closeGameMessage();
                return;
            }

            messageHandler.sendToServer(new JoinMsg(playerName));

        });
        scannerThread.start();

    }

    @Override
    public void chooseCard(CardsListMsg cardsListMsg) {

        scannerThread = new Thread(()->{

            //Card choice
            int cardChoice;
            do {
                System.out.println("Choose a card:");
                System.out.println(" 0) Read cards descriptions");
                for (int c=1; c<=cardsListMsg.getCards().size(); c++){
                    System.out.printf("%2d) %s\n", c, cardsListMsg.getCards().get(c-1).getName());
                }

                String input = cmdIn.nextLine();

                //Checks if it's still connected to the server
                if (exitGame){
                    closeGameMessage();
                    return;
                }

                try {
                    cardChoice = Integer.parseInt( input );
                } catch (NumberFormatException e) {
                    cardChoice = -1;
                }
                if (cardChoice<0 || cardChoice>cardsListMsg.getCards().size()){
                    System.out.println("Choice not allowed, retry\n");
                }
                if (cardChoice == 0){
                    for (int card = 0; card < cardsListMsg.getCards().size(); card++) {
                        //Prints cards descriptions
                        System.out.println(cardsListMsg.getCards().get(card).getName());
                        System.out.println(cardsListMsg.getCards().get(card).getShortDescription());
                        System.out.println(cardsListMsg.getCards().get(card).getDescription() + "\n");
                    }

                }
            }while (cardChoice<=0 || cardChoice>cardsListMsg.getCards().size());

            System.out.printf("You choose %s\n\n", cardsListMsg.getCards().get(cardChoice-1).getName());

            messageHandler.sendToServer(new SelectCardMsg(cardsListMsg.getCards().get(cardChoice-1)));

        });
        scannerThread.start();

    }

    @Override
    public void requestStartInfo(RequestInfoMsg requestInfoMsg) {

        scannerThread = new Thread(() -> {

            //Request for color selection
            System.out.println("Choose a color:");
            for (int c=0; c<requestInfoMsg.getAvailableColors().size();c++){
                System.out.printf("%d) %s\n", c, requestInfoMsg.getAvailableColors().get(c).toString());
            }
            int colorChoice;
            try {
                colorChoice = getUserInput(requestInfoMsg.getAvailableColors());
            } catch (Throwable throwable) {
                //server disconnected
                return;
            }

            //List used as support, where user choice for worker position will be added
            List<Point> selectedPoint = new ArrayList<>();

            //Two request for worker position
            for (int i = 0; i < 2; i++) {

                if (i==0){
                    System.out.println("Choose the position of the first worker:");
                }else {
                    System.out.println("Choose the position of the second worker:");
                }

                //Prints all the available position
                for (int c=0; c<requestInfoMsg.getAvailablePositions().size(); c++){
                    System.out.printf("%2d) %s\n", c, requestInfoMsg.getAvailablePositions().get(c).toString());
                }

                //Selection of the position of the first worker
                if (i==0){
                    int worker1Position;
                    try {
                        worker1Position = getUserInput(requestInfoMsg.getAvailablePositions());
                    } catch (Throwable throwable) {
                        //server disconnected
                        return;
                    }
                    //added user selection to the support list
                    selectedPoint.add(requestInfoMsg.getAvailablePositions().get(worker1Position));
                    //remove the selected point from the original list
                    requestInfoMsg.getAvailablePositions().remove(worker1Position);
                }

                //Selection of the position of the second worker
                else {
                    int worker2Position;
                    try {
                        worker2Position = getUserInput(requestInfoMsg.getAvailablePositions());
                    } catch (Throwable throwable) {
                        //server disconnected
                        return;
                    }
                    //added user selection to the support list
                    selectedPoint.add(requestInfoMsg.getAvailablePositions().get(worker2Position));
                }

            }

            messageHandler.sendToServer( new PlayerInfoMsg( requestInfoMsg.getAvailableColors().get(colorChoice),
                    selectedPoint.get(0),
                    selectedPoint.get(1) ) );

        });
        scannerThread.start();

    }

    @Override
    public void yourCardMessage(YourCardMsg yourCardMsg) {
        System.out.println("Your card:");

        if (yourCardMsg.getCard()!=null) {
            System.out.println(yourCardMsg.getCard().getName());
            System.out.println(yourCardMsg.getCard().getShortDescription());
            System.out.println(yourCardMsg.getCard().getDescription() + "\n");
        } else {
            System.out.println("No card\n");
        }

    }

    @Override
    public void chooseWorker(WorkersListMsg workersListMsg) {

        scannerThread = new Thread(() -> {

            if (discardUndo){
                System.out.println("\nPress a key to continue\n");
            }

            //Synchronization for an exclusive execution of chooseWorker and chooseUndo
            synchronized (cmdLock) {

                System.out.println("Choose the worker for this turn:");
                for (int i = 0; i < workersListMsg.getWorkers().size(); i++) {
                    System.out.printf("%d) Worker at position %s\n", i, workersListMsg.getWorkers().get(i).getPosition().toString());
                }

                int workerChoice;
                try {
                    workerChoice = getUserInput(workersListMsg.getWorkers());
                } catch (Throwable throwable) {
                    //server disconnected
                    return;
                }

                messageHandler.sendToServer( new SelectWorkerMsg(workerChoice) );
            }

        });
        scannerThread.start();

    }

    @Override
    public void chooseAction(ActionsListMsg actionsListMsg) {

        scannerThread = new Thread(() -> {

            System.out.println("Choose the next action: ");
            for (int c=0; c<actionsListMsg.getActions().size(); c++){
                System.out.printf("%d) %s\n", c, actionsListMsg.getActions().get(c));
            }

            int actionChoice;
            try {
                actionChoice = getUserInput(actionsListMsg.getActions());
            } catch (Throwable throwable) {
                //server disconnected
                return;
            }

            messageHandler.sendToServer( new SelectActionMsg(actionsListMsg.getActions().get(actionChoice)) );

        });
        scannerThread.start();

    }

    @Override
    public void chooseCell(CellListMsg cellListMsg) {

        scannerThread = new Thread(() -> {

            System.out.printf("Choose the cell where to do the %s action\n", cellListMsg.getAction());
            for (int c=0; c<cellListMsg.getCellList().size(); c++){
                System.out.printf("%2d) %s\n", c, cellListMsg.getCellList().get(c).getLocation().toString());
            }

            int choice;
            try {
                choice = getUserInput(cellListMsg.getCellList());
            } catch (Throwable throwable) {
                //server disconnected
                return;
            }

            messageHandler.sendToServer( new SelectCellMsg(cellListMsg.getCellList().get(choice)) );

        });
        scannerThread.start();

    }


    @Override
    public void chooseBuildOption(OptionsListMsg optionsListMsg) {

        scannerThread = new Thread(() -> {

            System.out.printf("Choose what to build in the %s cell\n", optionsListMsg.getCell().getLocation().toString());
            for (int i = 0; i < optionsListMsg.getOptions().size(); i++) {
                System.out.printf("%2d) %s\n", i, optionsListMsg.getOptions().get(i));
            }

            int optionChoice;
            try {
                optionChoice = getUserInput(optionsListMsg.getOptions());
            } catch (Throwable throwable) {
                //server disconnected
                return;
            }

            messageHandler.sendToServer( new SelectOptionMsg(optionsListMsg.getOptions().get(optionChoice)) );

        });
        scannerThread.start();

    }

    @Override
    public void chooseUndo() {

        discardUndo = false;
        undoScannerWaiting = true;
        scannerThread = new Thread( () -> {

            //Synchronization for an exclusive execution of chooseWorker and chooseUndo
            synchronized (cmdLock) {

                System.out.println("Do you want to confirm your turn?");
                System.out.println("You have 5 seconds to chose");
                System.out.println("0) Confirm\n1) Undo");

                int choice;
                do {

                    String input = cmdIn.nextLine();

                    //Check if player let the undo timer expire, ignore the player input
                    if (discardUndo){
                        return;
                    }

                    //Checks if it's still connected to the server
                    if (exitGame){
                        closeGameMessage();
                        return;
                    }

                    try {
                        choice = Integer.parseInt( input );
                    }catch (NumberFormatException e){
                        choice = -1;
                    }
                    if (choice<0 || choice>=2){
                        System.out.println("Choice not allowed, retry");
                    }
                }while (choice<0 || choice>=2);

                undoScannerWaiting = false;

                if (choice == 0){
                    messageHandler.sendToServer( new Message(MsgCommand.CONFIRM_TURN) );
                }else {
                    messageHandler.sendToServer( new Message(MsgCommand.UNDO_TURN) );
                }
            }

        } );

        scannerThread.start();

    }

    @Override
    public void endTurnMessage() {
        scannerThread.interrupt();
        System.out.println("\nYour turn is ended!");

        //If the thread that ask the undo request is still waiting for an input
        if (undoScannerWaiting){
            discardUndo = true;
            System.out.println("\nPress a key to continue\n");
        }
    }

    @Override
    public void updateBoard(UpdateBoardMsg updateBoardMsg) {
        System.out.println( ( new CLIBoardGenerator(updateBoardMsg.getBoard(), coloredBoard) ).toString() );
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

    @Override
    public void notYourTurnMessage() {
        System.out.println("It's not your turn\n");
    }

    @Override
    public void closeGameMessage() {

        // Close message caused by disconnection of the acceptance server,
        // need to be discarded because this disconnection it' not an error,
        // it's a disconnection followed by connection to the game server
        if (serverClosedRedirection){
            serverClosedRedirection = false;
            return;
        }

        // If there is an active thread ask to the user to press a key
        // in that way active scanner will be closed
        if (( scannerThread!=null && scannerThread.isAlive() ) && !exitGame) {
            System.out.println("\nDisconnected from server");
            System.out.println("\nPress a key to exit");

            exitGame = true;

        } else {
            System.out.println("\nThe game is closing");
            welcomeMessage();
        }

    }

}
