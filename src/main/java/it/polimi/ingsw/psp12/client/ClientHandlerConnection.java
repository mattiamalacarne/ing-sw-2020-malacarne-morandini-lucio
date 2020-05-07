package it.polimi.ingsw.psp12.client;


import it.polimi.ingsw.psp12.network.messages.CreateMsg;
import it.polimi.ingsw.psp12.network.messages.Message;
import it.polimi.ingsw.psp12.utils.Observable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

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


    /**
     * prepare the client for connect to the server
     * @param server the server info containing ip and port
     */
    public ClientHandlerConnection(ServerInfo server)
    {
        this.serverInfo = server;
        running = true;
    }

    @Override
    public void run()
    {
        // Connect to the server

        try {
            clientSocket = new Socket(serverInfo.serverIp, serverInfo.serverPort);
            System.out.println("Connected to server on port " + serverInfo.serverPort);

            // Init the stream after connection
            output_stream = new ObjectOutputStream(clientSocket.getOutputStream());
            output_stream.flush();

            input_stream = new ObjectInputStream(clientSocket.getInputStream());

            while (running){

                Message msg = (Message) input_stream.readObject();
                notifyObservers(msg);

            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Send a message through the socket connection
     * @param msg The message to be sent
     * @throws IOException IO Exception
     */
    public void sendRequestToServer(Message msg)
    {

        try {
            output_stream.writeObject(msg);
            output_stream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Closes the active socket
     * @throws IOException IO Exception
     */
    public void close() throws IOException {
        running = false;
        clientSocket.close();
    }

}
