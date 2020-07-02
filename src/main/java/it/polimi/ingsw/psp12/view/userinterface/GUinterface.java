package it.polimi.ingsw.psp12.view.userinterface;

import it.polimi.ingsw.psp12.client.MessageHandler;
import it.polimi.ingsw.psp12.client.ServerInfo;
import it.polimi.ingsw.psp12.exceptions.GUIStatusErrorException;
import it.polimi.ingsw.psp12.model.Worker;
import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.model.cards.Card;
import it.polimi.ingsw.psp12.model.enumeration.BuildOption;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;
import it.polimi.ingsw.psp12.network.messages.*;
import it.polimi.ingsw.psp12.utils.Color;
import it.polimi.ingsw.psp12.view.userinterface.GUI.GUIStatus;
import it.polimi.ingsw.psp12.view.userinterface.GUI.SetupHelper;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.*;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.SetUpUtils.*;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.gameUtils.ChooseBuildPanel;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.gameUtils.ChooseUndoPanel;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.gameUtils.GamePhase;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.guiengine.GenericMessageDialog;

import javax.swing.*;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

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
     * @throws IOException the IOException
     */
    public GUinterface() throws IOException {

        super("Santorini");
        gui = this;
        // Init the dimensions
        windowDimY = 800;
        aspectRatio = 16.0/9.0;
        windowDimX = (int) (windowDimY*aspectRatio);

        needCard = true;

        messageHandler = new MessageHandler(this);

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
     * @param msg the message
     * @throws GUIStatusErrorException the GUIStatusErrorException
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
            case GAME_CLOSING: actualScreen = new GenericMessageScreen(gui.getSize(), "<html>The game is ended, Game is restarting</html>", this); break;
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


    /**
     * Connect to the server with the address provided by the user
     * @param hostname server address
     * @throws IOException the IOException
     */
    public void connectToServer(String hostname) throws IOException {
        //System.out.println("Provo a connettermi");
        this.hostname = hostname;
        ServerInfo serverInfo;

        try {
            serverInfo = new ServerInfo((Inet4Address) Inet4Address.getByName(hostname));
        } catch (UnknownHostException e) {
            SetupDialog setup = new SetupDialog(gui, new GenericMessageDialog(this, "Invalid address, please retry"), "Network error");
            return;
        }

        if (!messageHandler.connect(serverInfo)) {
            SetupDialog setup = new SetupDialog(gui, new GenericMessageDialog(this, "Invalid address, please retry"), "Network error");
        }
    }

    public void createRoom(int playerNumber) throws IOException {

        messageHandler.sendToServer(new CreateMsg(playerNumber));
    }


    @Override
    public void createsNewRoom() throws IOException
    {
        dialog = new SetupDialog(this, new CreateRoomPanel(this), "Create new Room");

    }

    @Override
    public void waitMessage() {
        setup.dispayWaitBox();
    }

    @Override
    public void roomCreatedMessage(CreatedMsg createdMsg) throws IOException
    {
        messageHandler.setGamePort(createdMsg.getRoom().getAssignedPort());
        askPlayerName(0);
    }

    /**
     * Display a dialog for set a player name
     * @param choosed to determine the text to show on the panel
     */
    public void askPlayerName(int choosed)
    {
        dialog = new SetupDialog(this, new PlayerNamePanel(this, choosed),  "What's your name?");
    }

    /**
     * Ask the server to set the choosed name
     * @param playerName the player name
     */
    public void setPlayerName(String playerName)
    {
        messageHandler.sendToServer(new JoinMsg(playerName));
    }

    @Override
    public void roomCreatedFailedMessage() {
        SetupDialog setup = new SetupDialog(gui, new GenericMessageDialog(this, "Error creating the room"), "Error");
    }

    @Override
    public void invalidMaxPlayerMessage() {
        SetupDialog setup = new SetupDialog(gui, new GenericMessageDialog(this, "Invalid max player number"), "Error");
    }

    @Override
    public void roomFullMessage() throws IOException {
        SetupDialog setup = new SetupDialog(gui, new GenericMessageDialog(this, "Sorry, this room is full"), "Ops!");
    }


    @Override
    public void joinPlayerNameConfirmation()
    {
        try {
            gui.loadNewStatusScreen(GUIStatus.WAIT_OTHER_PLAYER, null);
        } catch (GUIStatusErrorException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void joinPlayerNameAlreadyUsed() throws IOException {

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

    /**
     * Send the card to the server
     * @param card the card
     */
    public void sendCardToServer(Card card)
    {
        messageHandler.sendToServer(new SelectCardMsg(card));
    }

    /**
     * Send to server the worker selected int the current turn
     * @param msg the selected worker
     */
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


    /**
     * Send to server the info for start the game (Worker position and Color
     * @param msg the PlayerInfoMsg message
     */
    public void sendStartInfo(PlayerInfoMsg msg)
    {
        messageHandler.sendToServer(msg);
    }

    @Override
    public void chooseAction(ActionsListMsg actionsListMsg) throws IOException {

        // Set in the gamescreen the correct phase

        game.setGamePhase(GamePhase.CHOOSE_ACTION);
        game.setPossibleActionList(actionsListMsg.getActions());
        game.displayActionSelection(actionsListMsg.getActions());

    }

    /**
     * Send to server the selected action
     * @param msg action to send
     */
    public void sendActionToServer(SelectActionMsg msg)
    {
        messageHandler.sendToServer(msg);
    }

    /**
     * Send to server the selected cell
     * @param sel cell to send
     */
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

        game.displayBuildSelection(optionsListMsg.getOptions());

    }

    /**
     * Send selected build option to the server
     * @param buildOption the build option
     */
    public void sendBuildOptionToServer(BuildOption buildOption)
    {
        messageHandler.sendToServer(new SelectOptionMsg(buildOption));
    }

    @Override
    public void chooseUndo() {

        game.setGameInfo("Terminate the turn or repeat?");
        game.displayUndoSelector();
    }

    /**
     * Send to server the undo decision
     * @param cmd the command
     */
    public void sendUndoToServer(MsgCommand cmd)
    {
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
        SetupDialog setup = new SetupDialog(gui, new GenericMessageDialog(this, "You win"), "Message from Gods!");

        startReloadTimer();
    }

    @Override
    public void youLostMessage() {
        SetupDialog setup = new SetupDialog(gui, new GenericMessageDialog(this, "You lose"), "Message from Gods!");
        startReloadTimer();
    }

    @Override
    public void otherPlayerLostMessage(OtherLostMsg otherLostMsg) {
        SetupDialog setup = new SetupDialog(gui, new GenericMessageDialog(this, otherLostMsg.getPlayer()+" has lost the game"), "Message from Gods!");
    }

    @Override
    public void notYourTurnMessage() {
        // do nothing
    }

    @Override
    public void closeGameMessage() {

        try {
            gui.loadNewStatusScreen(GUIStatus.GAME_CLOSING, null);
        } catch (GUIStatusErrorException e) {
            e.printStackTrace();
        }
        startReloadTimer();
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

        //System.out.println("[DEBUG]: Starting reload timer");
        Timer timer = new Timer();

        needCard = true;
        TimerTask reloadGame = new TimerTask() {
            @Override
            public void run() {
                try {
                    gui.loadNewStatusScreen(GUIStatus.SETUP, null);
                    setup = (SetUpScreen) actualScreen;
                } catch (GUIStatusErrorException e) {
                    e.printStackTrace();
                }
            }
        };

        timer.schedule(reloadGame, 5000);
    }



}
