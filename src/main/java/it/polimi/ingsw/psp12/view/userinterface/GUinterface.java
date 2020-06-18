package it.polimi.ingsw.psp12.view.userinterface;

import it.polimi.ingsw.psp12.client.MessageHandler;
import it.polimi.ingsw.psp12.client.ServerInfo;
import it.polimi.ingsw.psp12.exceptions.GUIStatusErrorException;
import it.polimi.ingsw.psp12.model.Worker;
import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.model.cards.Card;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;
import it.polimi.ingsw.psp12.network.messages.*;
import it.polimi.ingsw.psp12.utils.Color;
import it.polimi.ingsw.psp12.view.userinterface.GUI.GUIStatus;
import it.polimi.ingsw.psp12.view.userinterface.GUI.SetupHelper;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.*;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.SetUpUtils.*;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.gameUtils.ChooseUndoPanel;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.gameUtils.GamePhase;

import javax.swing.*;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Class for the GUI interface of the game
 * @author Mattia Malacarne
 */
public class GUinterface extends JFrame implements UserInterface
{

    /** The dim of the GUI **/
    private double aspectRatio;
    private int windowDimY;
    private int windowDimX;

    private Screen actualScreen;

    // Setup helper
    private SetupHelper roomHelper;
    private String hostname;


    private MessageHandler messageHandler;

    private GUinterface gui;
    private SetupDialog dialog;
    private GameScreen game;

    private SetupDialog waiting;
    private SetUpScreen setup;

    /** if true, recreate gamescreen with card displayed **/
    private boolean needCard;

    /**
     * Init the UI starting the mainWindow and populate it with the necessary panel
     */
    public GUinterface() throws IOException {

        super("Santorini");
        gui = this;
        // Init the dimensions
        windowDimY = 800;
        aspectRatio = 16.0/9.0;
        windowDimX = (int) (windowDimY*aspectRatio);

        needCard = true;

        this.setSize((int) windowDimX, (int) windowDimY + 40);
        this.setResizable(false);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Load the default screen
        try {
            loadNewStatusScreen(GUIStatus.SETUP, null);
            setup = (SetUpScreen) actualScreen;
        } catch (GUIStatusErrorException e) {
            e.printStackTrace();
        }


        this.setVisible(true);

    }

    public double getWindowDimY() { return windowDimY; }
    public double getWindoeDImX() { return windowDimX; }
    public double getAspectRatio() { return aspectRatio; }


    /**
     * Load a new screen depending by the status of the GUI
     * @param status the new screen to load
     */
    public void loadNewStatusScreen(GUIStatus status, Message msg) throws GUIStatusErrorException
    {
        Screen newScreen;
        switch (status)
        {
            case GAME: actualScreen = new GameScreen(this, (YourCardMsg) msg); break;
            case WAIT_CARD_SELECTION: actualScreen = new GenericMessageScreen(gui.getSize(), "Waiting other players", this); break;
            case WAIT_OTHER_PLAYER: actualScreen = new GenericMessageScreen(gui.getSize(), "Waiting other players", this); break;
            case YOU_LOST: actualScreen = new GenericMessageScreen(gui.getSize(), "You lost!", this); break;
            case YOU_WIN: actualScreen = new GenericMessageScreen(gui.getSize(), "You are the winner!!", this); break;
            case SETUP: actualScreen = new SetUpScreen(this); break;
            case CARDLIST: actualScreen = new CardSelectorScreen(this, (((CardsListMsg) msg).getCards())); break;
            case STARTING: actualScreen = new GameScreen(this, (YourCardMsg) msg); break;
            default: actualScreen = new SetUpScreen(this);
        }

        gui.setContentPane(actualScreen);

        gui.revalidate();
        gui.repaint();
        gui.setVisible(true);
    }


    public void connectToServer(String hostname) throws IOException {
        System.out.println("Provo a connettermi");
        this.hostname = hostname;
        messageHandler = new MessageHandler(this);
        //messageHandler.startGame();
    }

    public void createRoom(int playerNumber) throws IOException {
        //TODO: Build a room with the helper
        System.out.println("Creo la stanza");
        messageHandler.sendToServer(new CreateMsg(playerNumber));
    }



    @Override
    public void writeOnStream(String s)
    {
        System.out.println("GUI " + s.toUpperCase());
    }

    @Override
    public ServerInfo getServerByIp() throws UnknownHostException {
        System.out.println("Getting server ip");
        return new ServerInfo((Inet4Address) Inet4Address.getByName(hostname));
    }

    @Override
    public void createsNewRoom() throws IOException
    {
        dialog = new SetupDialog(this, new CreateRoomPanel(this), "Create new Room");

    }

    @Override
    public void waitMessage() {
        //System.out.println("WE ARE WAITING");
        setup.dispayWaitBox();
    }

    @Override
    public void roomCreatedMessage(CreatedMsg createdMsg) throws IOException
    {
        messageHandler.setGamePort(createdMsg.getRoom().getAssignedPort());
        askPlayerName(0);
    }

    public void askPlayerName(int choosed)
    {
        dialog = new SetupDialog(this, new PlayerNamePanel(this, choosed),  "What's your name?");
    }

    public void setPlayerName(String playerName)
    {
        //System.out.println("Provo ad aggiungere il nome");
        messageHandler.sendToServer(new JoinMsg(playerName));
    }

    @Override
    public void roomCreatedFailedMessage() {

    }

    @Override
    public void invalidMaxPlayerMessage() {

    }

    @Override
    public void roomFullMessage() throws IOException {

    }


    @Override
    public void joinPlayerNameConfirmation()
    {
        System.out.println("Room joined");
        try {
            //gui.loadNewStatusScreen(GUIStatus.LOBBY, null);
            gui.loadNewStatusScreen(GUIStatus.WAIT_OTHER_PLAYER, null);
        } catch (GUIStatusErrorException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void joinPlayerNameAlreadyUsed() throws IOException {
        System.out.println("Playername already used");
        askPlayerName(1);
    }

    @Override
    public void chooseCard(CardsListMsg cardsListMsg) {
        try {
            gui.loadNewStatusScreen(GUIStatus.CARDLIST, (Message) cardsListMsg);
        } catch (GUIStatusErrorException e) {
            e.printStackTrace();
        }
    }

    public void sendCardToServer(Card card)
    {
        messageHandler.sendToServer(new SelectCardMsg(card));
    }

    public void sendSelectedWorkerToServer(SelectWorkerMsg msg)
    {
        System.out.println("Worker Selected");
        messageHandler.sendToServer(msg);
    }

    @Override
    public void requestStartInfo(RequestInfoMsg requestInfoMsg) throws IOException
    {

         game = (GameScreen) actualScreen;
         game.setUp(requestInfoMsg);

    }

    public void sendStartInfo(PlayerInfoMsg msg)
    {
        messageHandler.sendToServer(msg);
    }

    @Override
    public void chooseAction(ActionsListMsg actionsListMsg) throws IOException {

        // Set in the gamescreen the correct phase
        System.out.println("Choose action");
        game.setGamePhase(GamePhase.CHOOSE_ACTION);
        game.setPossibleActionList(actionsListMsg.getActions());
        game.displayActionSelection(actionsListMsg.getActions());

    }

    public void sendActionToServer(SelectActionMsg msg)
    {
        messageHandler.sendToServer(msg);
    }

    public void sendCellToServer(Cell sel)
    {
        messageHandler.sendToServer(new SelectCellMsg(sel));
    }

    @Override
    public void chooseCell(CellListMsg cellListMsg) throws IOException {
        System.out.println("Ora devi scegliere la cella");
        game.setGamePhase(GamePhase.ACTION);
        game.displayPossibility(cellListMsg.getCellList());
    }

    @Override
    public void chooseWorker(WorkersListMsg workersListMsg) {
        game.setGameInfo("Choose a worker for the turn");
        game.setGamePhase(GamePhase.CHOOSE_ACTION);
        List<Point> points = new ArrayList<>();
        List<Worker> workers = workersListMsg.getWorkers();

        for (Worker worker: workers)
        {
            points.add(worker.getPosition());
        }
        game.displayWorkerPos(points);
    }

    @Override
    public void chooseBuildOption(OptionsListMsg optionsListMsg) {

    }

    @Override
    public void chooseUndo() {

        System.out.println("Waiting for undo!");
        game.setGameInfo("Terminate the turn or repeat?");
        game.displayUndoSelector();
    }

    public void sendUndoToServer(MsgCommand cmd)
    {
        System.out.println("Sending undo to server");
        messageHandler.sendToServer(new Message(cmd));
    }

    @Override
    public void endTurnMessage() {
        //System.out.println("Your turn is ended");
        game.setGamePhase(GamePhase.NOT_MY_TURN);
        game.setGameInfo("Your turn is ended");
        game.destroyUndoBox();
    }


    @Override
    public void updateBoard(UpdateBoardMsg updateBoardMsg) {
        //System.out.println("Ho ricevuto una Board");

        game = (GameScreen) actualScreen;
        game.drawBoard(updateBoardMsg);
    }

    @Override
    public void youWonMessage() {
        try {
            gui.loadNewStatusScreen(GUIStatus.YOU_WIN, null);
        } catch (GUIStatusErrorException e) {
            e.printStackTrace();
        }
        startReloadTimer();
    }

    @Override
    public void youLostMessage() {
        try {
            gui.loadNewStatusScreen(GUIStatus.YOU_LOST, null);
        } catch (GUIStatusErrorException e) {
            e.printStackTrace();
        }
        startReloadTimer();
    }

    @Override
    public void otherPlayerLostMessage(OtherLostMsg otherLostMsg) {
        System.out.println("Non hai perso tu");
        // Non so ancora che fare, da decidere (mahari un messagebox ch scompare dopo qualche secondo
    }

    @Override
    public void closeGameMessage() {
        System.out.println("Game is closing");
    }

    @Override
    public void yourCardMessage(YourCardMsg yourCardMsg) {
        try {
            if (needCard)
            {
                this.loadNewStatusScreen(GUIStatus.GAME, (Message) yourCardMsg);
                needCard = false;
            }
        } catch (GUIStatusErrorException e) {
            e.printStackTrace();
        }
    }

    /**
     * Start a timer, whene expires reload the setupscreen
     */
    private void startReloadTimer()
    {
        //TODO: SETUP the timer
        System.out.println("[DEBUG]: Starting reload timer");
    }



}
