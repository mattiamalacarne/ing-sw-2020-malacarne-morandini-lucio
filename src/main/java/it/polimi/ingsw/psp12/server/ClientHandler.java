package it.polimi.ingsw.psp12.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Class that handles socket connection with a client
 * @author Luca Morandini
 */
public class ClientHandler implements Runnable {
    private Socket socket;

    private ObjectInputStream incoming;

    private ObjectOutputStream outgoing;

    ClientHandler(Socket client)
    {
        this.socket = client;

        try {
            this.incoming = new ObjectInputStream(client.getInputStream());
            this.outgoing = new ObjectOutputStream(client.getOutputStream());

            System.out.println("connected to " + client.getInetAddress());
        }
        catch (IOException e) {
            // TODO: manage exception
            System.out.println("error while getting client " + client.getInetAddress() + " streams");
            e.printStackTrace();
        }

    }

    /**
     * Listen for incoming messages from the client
     */
    @Override
    public void run()
    {
        while (true) {
            try {
                Object next = incoming.readObject();
            }
            catch (IOException e) {
                // TODO: manage exception
                System.out.println("client " + socket.getInetAddress() + " connection dropped");
                e.printStackTrace();
            }
            catch (ClassNotFoundException e) {
                // TODO: manage exception
                e.printStackTrace();
            }
        }
    }

    /**
     * Send a message to the client
     * @param msg message to be sent to the client
     */
    public void send(String msg)
    {
        try {
            outgoing.writeObject(msg);
        }
        catch (IOException e) {
            // TODO: manage exception
            e.printStackTrace();
        }
    }

    /**
     * Terminate connection with the client
     */
    public void close()
    {
        try {
            incoming.close();
            outgoing.close();
            socket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
