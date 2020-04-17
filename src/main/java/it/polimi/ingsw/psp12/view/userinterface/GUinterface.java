package it.polimi.ingsw.psp12.view.userinterface;

import it.polimi.ingsw.psp12.client.ServerInfo;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class GUinterface extends JFrame implements UserInterface
{

    private Scanner cmdIn;

    JPanel setup;
    JPanel test;

    GUinterface main;

    public GUinterface()
    {
       super("Santorini gruppo 12");
       this.setSize(300,300);
       this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

       main = this;

       test = new GUItestElem();
       setup = new GUISetup(this);


       this.add(setup);

       System.out.println("GUI START");
       this.setVisible(true);
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
    public String setClientName()
    {
        System.out.println("Reomoving");
        main.add(test);
        setup.setVisible(false);
        return null;
    }

    @Override
    public int getGamePort(ServerInfo serverInfo) throws IOException {
        return 0;
    }
}
