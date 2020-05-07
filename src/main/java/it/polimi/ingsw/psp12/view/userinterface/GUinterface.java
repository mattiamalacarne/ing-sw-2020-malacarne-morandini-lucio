package it.polimi.ingsw.psp12.view.userinterface;

import it.polimi.ingsw.psp12.client.MessageHandler;
import it.polimi.ingsw.psp12.client.ServerInfo;
import it.polimi.ingsw.psp12.exceptions.GUIStatusErrorException;
import it.polimi.ingsw.psp12.network.messages.*;
import it.polimi.ingsw.psp12.view.userinterface.GUI.GUIStatus;
import it.polimi.ingsw.psp12.view.userinterface.GUI.SetupHelper;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.GameScreen;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.Screen;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.SetUpScreen;

import javax.swing.*;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;

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
    private SetupHelper sHelper;


    private MessageHandler messageHandler;

    private GUinterface gui;

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


        this.setSize((int) windowDimX, (int) windowDimY + 40);
        this.setResizable(false);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Load the default screen
        try {
            loadNewStatusScreen(GUIStatus.SETUP);
        } catch (GUIStatusErrorException e) {
            e.printStackTrace();
        }

        gui.createSetupHelper(new SetupHelper("localhost", "mattia", "a stanz adi prova", 2));

        messageHandler = new MessageHandler(this);
        messageHandler.startGame();


        //this.add(actualScreen);

        this.setVisible(true);

    }

    public double getWindowDimY() { return windowDimY; }
    public double getAspectRatio() { return aspectRatio; }


    /**
     * Load a new screen depending by the status of the GUI
     * @param status the new screen to load
     */
    public void loadNewStatusScreen(GUIStatus status) throws GUIStatusErrorException
    {
        Screen newScreen;
        switch (status)
        {
            case GAME: actualScreen = new GameScreen(this); break;
            case SETUP: actualScreen = new SetUpScreen(this); break;
            default: actualScreen = new SetUpScreen(this);
        }

        gui.setContentPane(actualScreen);

        gui.revalidate();
        gui.repaint();
        gui.setVisible(true);
    }


    /**
     * Create setup helper and start the messageHandler
     * @param helper
     */
    public void createSetupHelper(SetupHelper helper) throws IOException {
        this.sHelper = helper;
        //messageHandler.sendToServer( new CreateMsg(sHelper.getRoomName(), sHelper.getRoomMaxPlayer())  );
        System.out.println("Helper OK!");
    }

    @Override
    public void writeOnStream(String s)
    {
        System.out.println("GUI " + s.toUpperCase());
    }

    @Override
    public ServerInfo getServerByIp() throws UnknownHostException {
        System.out.println("Getting server ip");
        return new ServerInfo((Inet4Address) Inet4Address.getByName(sHelper.getHostname()));
    }

    @Override
    public void getGamePort() throws IOException {

        System.out.println("GETGAMEPORT");

    }

    public void createRoom()
    {
        System.out.println("CREO UN TEST");
        try {
            messageHandler.sendToServer( new CreateMsg("Fottiti", 2));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void roomCreatedMessage() throws IOException {
        System.out.println("Ho creato la stanza");
    }

    @Override
    public void selectGamePort(RoomsMsg roomList) throws IOException {

    }

    @Override
    public void roomFull() throws IOException {

    }

    @Override
    public void joinPlayerNameConfirmation() {

    }


    @Override
    public void joinPlayerNameAlreadyUsed() throws IOException {

    }

    @Override
    public void requestStartInfo(RequestInfoMsg requestInfoMsg) throws IOException {

    }

    @Override
    public void move(CellListMsg cellListMsg) throws IOException {

    }

    @Override
    public void build(CellListMsg cellListMsg) throws IOException {

    }

    @Override
    public void updateBoard(UpdateBoardMsg updateBoardMsg) {

    }



}
