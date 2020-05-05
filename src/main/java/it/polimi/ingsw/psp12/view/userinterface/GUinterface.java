package it.polimi.ingsw.psp12.view.userinterface;

import it.polimi.ingsw.psp12.client.MessageHandler;
import it.polimi.ingsw.psp12.client.ServerInfo;
import it.polimi.ingsw.psp12.exceptions.GUIStatusErrorException;
import it.polimi.ingsw.psp12.network.messages.CellListMsg;
import it.polimi.ingsw.psp12.network.messages.RequestInfoMsg;
import it.polimi.ingsw.psp12.network.messages.RoomsMsg;
import it.polimi.ingsw.psp12.network.messages.UpdateBoardMsg;
import it.polimi.ingsw.psp12.view.userinterface.GUI.GUIStatus;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.GameScreen;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.Screen;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.SetupScreen;

import javax.swing.*;
import java.io.IOException;
import java.net.UnknownHostException;
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
    private int rightMenuSize;

    private Scanner cmdIn;


    private MessageHandler messageHandler;

    private GUinterface gui;

    /**
     * Init the UI starting the mainWindow and populate it with the necessary panel
     */
    public GUinterface()
    {

        super("Santorini");
        gui = this;
        // Init the dimensions
        windowDimY = 800;
        rightMenuSize = 100;
        aspectRatio = 16.0/9.0;
        windowDimX = (int) (windowDimY*aspectRatio);

        this.setSize((int) windowDimX, (int) windowDimY + 40);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Load the default screen
        try {
            loadNewStatusScreen(GUIStatus.GAME);
        } catch (GUIStatusErrorException e) {
            e.printStackTrace();
        }

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
            case GAME: newScreen = new GameScreen(this); break;
            case SETUP: newScreen = new SetupScreen(this); break;
            default: newScreen = new SetupScreen(this);
        }

        gui.setContentPane(newScreen);

        gui.revalidate();
        gui.repaint();
        gui.setVisible(true);
    }


    @Override
    public void writeOnStream(String s)
    {
        System.out.println("GUI " + s.toUpperCase());
    }

    @Override
    public ServerInfo getServerByIp() throws UnknownHostException {
        return null;
    }

    @Override
    public void getGamePort() throws IOException {

    }


    @Override
    public void roomCreatedMessage() throws IOException {

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
