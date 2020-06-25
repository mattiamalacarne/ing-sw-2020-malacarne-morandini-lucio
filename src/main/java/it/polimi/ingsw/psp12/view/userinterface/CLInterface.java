package it.polimi.ingsw.psp12.view.userinterface;

import it.polimi.ingsw.psp12.client.MessageHandler;
import it.polimi.ingsw.psp12.client.ServerInfo;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;
import it.polimi.ingsw.psp12.network.messages.*;
import it.polimi.ingsw.psp12.view.userinterface.CLI.CLIBoardGenerator;

import java.io.*;
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
     * MessageHandler that will manages the communication which the server
     */
    private MessageHandler messageHandler;

//    private Boolean isWaiting;

    /**
     * Thread dedicated to receive user input
     */
    private Thread scannerThread;

    private Thread timerThread;

    /**
     * CLI constructor
     */
    public CLInterface() {

        welcomeMessage();
        cmdLock = new Object();

    }

    @Override
    public void writeOnStream(String s)
    {
        System.out.println(s.toUpperCase());
    }

    public void welcomeMessage() {

        inputStreamReader = new InputStreamReader(System.in);
        exitGame = false;

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
            messageHandler = new MessageHandler(this);
        }else {
            //Exit
            System.out.println("Quitting the game");
            System.exit(0);
        }

    }


    @Override
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
    public void roomCreatedMessage(CreatedMsg createdMsg) throws IOException {

        //FIXME: uncomment after synchronization fix in ClientHandlerConnection
//        scannerThread = new Thread(() -> {

        System.out.printf("\nRoom for %d players created\n", createdMsg.getRoom().getMaxPlayersCount());

        //New port is updated
        try {
            messageHandler.setGamePort(createdMsg.getRoom().getAssignedPort());
        } catch (IOException e) {
            System.out.println("Error entering the room...");
        }

        System.out.println("What's your name: ");

        String playerName = cmdIn.nextLine();

        //Checks if it's still connected to the server
        if (exitGame){
            closeGameMessage();
            return;
        }

        messageHandler.sendToServer(new JoinMsg(playerName));

//        });
//        scannerThread.start();

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
//            //TODO: stampa messaggio di waiting ?
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

            System.out.println("Choose a color:");
            for (int c=0; c<requestInfoMsg.getAvailableColors().size();c++){
                System.out.printf("%d) %s\n", c, requestInfoMsg.getAvailableColors().get(c).toString());
            }
            int colorChoice;
            do {
                String input = cmdIn.nextLine();

                //Checks if it's still connected to the server
                if (exitGame){
                    closeGameMessage();
                    return;
                }

                try {
                    colorChoice = Integer.parseInt( input );
                } catch (NumberFormatException e) {
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
                String input = cmdIn.nextLine();

                //Checks if it's still connected to the server
                if (exitGame){
                    closeGameMessage();
                    return;
                }

                try {
                    worker1Position = Integer.parseInt( input );
                } catch (NumberFormatException e) {
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
                String input = cmdIn.nextLine();

                //Checks if it's still connected to the server
                if (exitGame){
                    closeGameMessage();
                    return;
                }

                try {
                    worker2Position = Integer.parseInt( input );
                } catch (NumberFormatException e) {
                    worker2Position=-1;
                }
                if (worker2Position<0 || worker2Position>=requestInfoMsg.getAvailablePositions().size() || worker1Position==worker2Position){
                    System.out.println("Choice not allowed, retry");
                }
            }while (worker2Position<0 || worker2Position>=requestInfoMsg.getAvailablePositions().size() || worker1Position==worker2Position);

            messageHandler.sendToServer( new PlayerInfoMsg( requestInfoMsg.getAvailableColors().get(colorChoice),
                    requestInfoMsg.getAvailablePositions().get(worker1Position),
                    requestInfoMsg.getAvailablePositions().get(worker2Position) ) );

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

            //Synchronization for an exclusive execution of chooseWorker and chooseUndo
            synchronized (cmdLock) {

                System.out.println("Choose the worker for this turn:");
                for (int i = 0; i < workersListMsg.getWorkers().size(); i++) {
                    System.out.printf("%d) Worker at position %s\n", i, workersListMsg.getWorkers().get(i).getPosition().toString());
                }

                int workerChoice;
                do {
                    String input = cmdIn.nextLine();

                    //Checks if it's still connected to the server
                    if (exitGame){
                        closeGameMessage();
                        return;
                    }

                    try {
                        workerChoice = Integer.parseInt( input );
                    } catch (NumberFormatException e) {
                        workerChoice = -1;
                    }
                    if (workerChoice<0 || workerChoice>=workersListMsg.getWorkers().size()){
                        System.out.println("Choice not allowed, retry");
                    }
                }while (workerChoice<0 || workerChoice>=workersListMsg.getWorkers().size());

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
            do {
                String input = cmdIn.nextLine();

                //Checks if it's still connected to the server
                if (exitGame){
                    closeGameMessage();
                    return;
                }

                try {
                    actionChoice = Integer.parseInt( input );
                } catch (NumberFormatException e) {
                    actionChoice = -1;
                }
                if (actionChoice<0 || actionChoice>=actionsListMsg.getActions().size()){
                    System.out.println("Choice not allowed, retry");
                }
            }while (actionChoice<0 || actionChoice>=actionsListMsg.getActions().size());

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
            do {
                String input = cmdIn.nextLine();

                //Checks if it's still connected to the server
                if (exitGame){
                    closeGameMessage();
                    return;
                }

                try {
                    choice = Integer.parseInt( input );
                } catch (NumberFormatException e) {
                    choice = -1;
                }
                if (choice<0 || choice>=cellListMsg.getCellList().size()){
                    System.out.println("Choice not allowed, retry");
                }
            }while (choice<0 || choice>=cellListMsg.getCellList().size());

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
            do {
                String input = cmdIn.nextLine();

                //Checks if it's still connected to the server
                if (exitGame){
                    closeGameMessage();
                    return;
                }

                try {
                    optionChoice = Integer.parseInt( input );
                } catch (NumberFormatException e) {
                    optionChoice = -1;
                }
                if (optionChoice<0 || optionChoice>=optionsListMsg.getOptions().size()){
                    System.out.println("Choice not allowed, retry");
                }
            }while (optionChoice<0 || optionChoice>=optionsListMsg.getOptions().size());

            messageHandler.sendToServer( new SelectOptionMsg(optionsListMsg.getOptions().get(optionChoice)) );

        });
        scannerThread.start();

    }

    @Override
    public void chooseUndo() {

        discardUndo = false;
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
        System.out.print("\nYour turn is ended!");

        //If the thread that ask the undo request is still waiting for an input
        if (scannerThread.isAlive()){
            discardUndo = true;
            System.out.println("\nPress a key to continue\n");
        }
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

    @Override
    public void notYourTurnMessage() {
        System.out.println("It's not your turn\n");
    }

    @Override
    public void closeGameMessage() {


        if (!exitGame) {
            System.out.println("\nDisconnected from server, the game is closing");
            System.out.println("\nPress a key to exit");

            exitGame = true;

        } else {
            welcomeMessage();
        }

//        welcomeMessage();
    }

}
