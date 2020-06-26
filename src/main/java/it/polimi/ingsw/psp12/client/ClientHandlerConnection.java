package it.polimi.ingsw.psp12.client;


import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;
import it.polimi.ingsw.psp12.network.messages.Message;
import it.polimi.ingsw.psp12.utils.Constants;
import it.polimi.ingsw.psp12.utils.Observable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * <p><b>Class</b> responsible for the connection with the main server</p>
 *
 * @author Mattia Malacarne
 */
public class ClientHandlerConnection extends Observable<Message> implements Runnable
{
    private ServerInfo serverInfo;
    private Socket clientSocket;
    private ObjectOutputStream output_stream;
    private ObjectInputStream input_stream;
    private Boolean running;
    private final Object runningLock;

    /**
     * Timer used to periodically send ping to keep the connection open
     */
    private ScheduledExecutorService pingTimer;


    /**
     * prepare the client for connect to the server
     * @param server the server info containing ip and port
     */
    public ClientHandlerConnection(ServerInfo server) throws IOException {
        this.serverInfo = server;
        running = true;

        this.pingTimer = Executors.newSingleThreadScheduledExecutor();
        this.pingTimer.scheduleAtFixedRate(() -> {
            sendRequestToServer(new Message(MsgCommand.PING));
        }, Constants.PING_INTERVAL, Constants.PING_INTERVAL, TimeUnit.MILLISECONDS);

        clientSocket = new Socket(serverInfo.serverIp, serverInfo.serverPort);
        clientSocket.setSoTimeout(Constants.SOCKET_TIMEOUT);
        System.out.println("Connected to server on port " + serverInfo.serverPort);

        // Init the stream after connection
        output_stream = new ObjectOutputStream(clientSocket.getOutputStream());
        output_stream.flush();

        input_stream = new ObjectInputStream(clientSocket.getInputStream());
        this.runningLock = new Object();
    }

    @Override
    public void run()
    {
        boolean isRunning;
        synchronized (runningLock) {
            isRunning = running;
        }
        // Connect to the server
        try {

            while (isRunning){

                Message msg = (Message) input_stream.readObject();
                notifyObservers(msg);

                synchronized (runningLock) {
                    isRunning = running;
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            //Game server unreachable
            notifyObservers(new Message(MsgCommand.CLOSE));
        }

    }

    /**
     * Send a message through the socket connection
     * @param msg The message to be sent
     */
    public synchronized void sendRequestToServer(Message msg)
    {

        try {
            output_stream.writeObject(msg);
            output_stream.flush();
        } catch (IOException e) {
            //Game server unreachable
            notifyObservers(new Message(MsgCommand.CLOSE));
        }

    }

    /**
     * Closes the active socket
     * @throws IOException IO Exception
     */
    public void close() throws IOException {
        synchronized (runningLock) {
            running = false;
        }
        pingTimer.shutdownNow();
        clientSocket.close();
    }

}
