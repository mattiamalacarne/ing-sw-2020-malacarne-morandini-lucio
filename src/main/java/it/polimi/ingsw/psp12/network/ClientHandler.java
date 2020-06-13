package it.polimi.ingsw.psp12.network;

import it.polimi.ingsw.psp12.network.messages.Message;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;
import it.polimi.ingsw.psp12.server.game.CommandHandler;
import it.polimi.ingsw.psp12.server.Server;
import it.polimi.ingsw.psp12.utils.Constants;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class that handles socket connection with a client
 * @author Luca Morandini
 */
public class ClientHandler implements Runnable {
    private final Socket socket;

    private final ObjectInputStream incoming;

    private final ObjectOutputStream outgoing;

    private boolean running;

    private final Object runningLock;

    /**
     * Timer used to periodically send ping to keep the connection open
     */
    private final ScheduledExecutorService pingTimer;

    /**
     * Handler of game commands
     */
    private CommandHandler handler;

    /**
     * Server that manages system commands
     */
    private Server server;

    public ClientHandler(Socket client) throws IOException {
        this.socket = client;
        this.running = true;
        this.runningLock = new Object();

        this.incoming = new ObjectInputStream(client.getInputStream());
        this.outgoing = new ObjectOutputStream(client.getOutputStream());

        this.pingTimer = Executors.newSingleThreadScheduledExecutor();
        this.pingTimer.scheduleAtFixedRate(() -> {
            // TODO: handle multi threading
            send(new Message(MsgCommand.PING));
        }, Constants.PING_INTERVAL, Constants.PING_INTERVAL, TimeUnit.MILLISECONDS);
    }

    /**
     * Determine if the socket is open
     * @return true if socket is open
     */
    public boolean isRunning() { // TODO: handle multi threading
        return running;
    }

    /**
     * Listen for incoming messages from the client
     */
    @Override
    public void run()
    {
        boolean isRunning;
        synchronized (runningLock) {
            isRunning = running;
        }

        while (isRunning) {
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

                synchronized (runningLock) {
                    isRunning = running;
                }

                if (isRunning) {
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

            synchronized (runningLock) {
                isRunning = running;
            }
        }
    }

    /**
     * Send a message to the client
     * @param message message to be sent to the client
     */
    public synchronized void send(Message message)
    {
        try {
            outgoing.writeObject(message);
        }
        catch (IOException e) {
            // TODO: manage exception
            //e.printStackTrace();
            // notify the server that the client has disconnected
            server.processCommand(new Message(MsgCommand.DISCONNECTED), this);
        }
    }

    /**
     * Terminate connection with the client
     */
    public void close()
    {
        if (running) {
            synchronized (runningLock) {
                running = false; // TODO: handle multi threading
            }

            pingTimer.shutdownNow();
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
     * @param commandHandler game commands handler
     */
    public void setCommandHandler(CommandHandler commandHandler) {
        handler = commandHandler;
    }

    /**
     * Set the server that manages system commands
     * @param server system commands handler
     */
    public void setServer(Server server) {
        this.server = server;
    }
}
