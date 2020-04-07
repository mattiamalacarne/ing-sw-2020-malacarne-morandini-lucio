package it.polimi.ingsw.psp12.view.userinterface;

import it.polimi.ingsw.psp12.client.ServerInfo;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class GUinterface implements UserInterface
{

    private Scanner cmdIn;

    public GUinterface()
    {
        this.waitingForInput();
    }

    public void waitingForInput()
    {
        cmdIn = new Scanner(System.in);
        while (true)
        {
            String in = cmdIn.nextLine();
            this.writeOnStream(in);
        }
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
}
