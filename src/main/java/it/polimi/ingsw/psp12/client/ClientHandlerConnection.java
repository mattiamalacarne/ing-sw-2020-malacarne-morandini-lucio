package it.polimi.ingsw.psp12.client;


import it.polimi.ingsw.psp12.network.messages.Message;
import it.polimi.ingsw.psp12.utils.Observable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * <p><b>Class</b> responsable for the connection with the main server</p>
 *
 * @author Mattia Malacarne
 */
public class ClientHandlerConnection extends Observable<Message> implements Runnable
{
    private ServerInfo serverInfo;
    private Socket clientSocket;
    private ObjectOutputStream output_stream;
    private ObjectInputStream input_stream;

    /**
     * Identifier of the client in the connection;
     */
    private String clientName;

    /**
     * prepare the client for connect to the server
     * @param server the server info containing ip and port
     */
    public ClientHandlerConnection(ServerInfo server, String clientName)
    {
        this.serverInfo = server;
        this.clientName = clientName;
    }

    @Override
    public void run()
    {
        // Connect to the server
        try {
            clientSocket = new Socket(serverInfo.serverIp, serverInfo.serverPort);
            System.out.println("Connesso al server!");

            // Init the stream after connection
            output_stream = new ObjectOutputStream(clientSocket.getOutputStream());
            output_stream.flush();

            input_stream = new ObjectInputStream(clientSocket.getInputStream());
            System.out.println("55555");

            while (true){

                Message msg = (Message) input_stream.readObject();
                notifyObservers(msg);

            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void sendRequestToServer(Message msg) throws IOException
    {
        output_stream.writeObject(msg);
        output_stream.flush();
    }


}
