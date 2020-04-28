package it.polimi.ingsw.psp12.view.userinterface;

import it.polimi.ingsw.psp12.client.ServerInfo;
import it.polimi.ingsw.psp12.network.messages.CellListMsg;
import it.polimi.ingsw.psp12.network.messages.RoomsMsg;
import it.polimi.ingsw.psp12.network.messages.UpdateBoardMsg;
import it.polimi.ingsw.psp12.view.userinterface.GUI.GUIMainWindow;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * Class for the GUI interface of the game
 * @author Mattia Malacarne
 */
public class GUinterface implements UserInterface
{

    private Scanner cmdIn;

    GUIMainWindow window;


    /**
     * Init the UI starting the mainWindow and populate it with the necessary panel
     */
    public GUinterface()
    {
       // Start mainwindow
    }


    @Override
    public void writeOnStream(String s)
    {
        System.out.println("GUI " + s.toUpperCase());
    }

    @Override
    public ServerInfo setUpServerInfo() throws UnknownHostException {
        return null;
    }

    @Override
    public String setClientName() {
        return null;
    }

    @Override
    public int getGamePort(ServerInfo serverInfo) throws IOException {
        return 0;
    }

    @Override
    public void roomCreatedMessage() throws IOException {

    }

    @Override
    public void selectGamePort(RoomsMsg roomList) throws IOException {

    }

    @Override
    public void joinPlayerNameConfirmation() throws IOException {

    }

    @Override
    public void joinPlayerNameAlreadyUsed() throws IOException {

    }

    @Override
    public void move(CellListMsg cellListMsg) throws IOException {

    }

    @Override
    public void build(CellListMsg cellListMsg) throws IOException {

    }

    @Override
    public void drawBoard(UpdateBoardMsg boardMsg) {

    }


}
