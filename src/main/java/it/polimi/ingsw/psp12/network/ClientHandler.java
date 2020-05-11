package it.polimi.ingsw.psp12.network;

import it.polimi.ingsw.psp12.network.messages.Message;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;
import it.polimi.ingsw.psp12.server.game.CommandHandler;
import it.polimi.ingsw.psp12.server.Server;

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

    private boolean running;

    /**
     * Handler of game commands
     */
    private CommandHandler handler;

    /**
     * Server that manages system commands
     */
    private Server server;

    public ClientHandler(Socket client)
    {
        this.socket = client;
        this.running = true;

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
        while (running) {
            try {
                Message msg = (Message)incoming.readObject();

                switch (msg.getType())
                {
                    case SYSTEM:
                        // system commands are processed by the game server
                        server.processCommand(msg, this);
                        break;
                    case GAME:
                        // game commands are processed by the command handler
                        handler.processCommand(msg);
                        break;
                }
            }
            catch (IOException e) {
                // TODO: manage exception
                //System.out.println("client " + socket.getInetAddress() + " connection dropped");
                //e.printStackTrace();

                if (running) {
                    // notify the server that the client has disconnected
                    server.processCommand(new Message(MsgCommand.DISCONNECTED), this);
                }

                // close socket
                close();
            }
            catch (ClassNotFoundException e) {
                // TODO: manage exception
                e.printStackTrace();
            }
        }
    }

    /**
     * Send a message to the client
     * @param message message to be sent to the client
     */
    public void send(Message message)
    {
        try {
            outgoing.writeObject(message);
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
        if (running) {
            running = false; // TODO: handle multi threading
            try {
                incoming.close();
                outgoing.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Set the handler of game commands
     * @param commandHandler
     */
    public void setCommandHandler(CommandHandler commandHandler) {
        handler = commandHandler;
    }

    /**
     * Set the server that manages system commands
     * @param server
     */
    public void setServer(Server server) {
        this.server = server;
    }
}
