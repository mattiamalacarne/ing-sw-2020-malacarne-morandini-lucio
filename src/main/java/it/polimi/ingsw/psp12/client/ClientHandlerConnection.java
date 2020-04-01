package it.polimi.ingsw.psp12.client;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * <p><b>Class</b> responsable for the connection with the main server</p>
 *
 * @author mattia
 */
public class ClientHandlerConnection implements Runnable
{
    ServerInfo serverInfo;
    private Socket clientSocket;
    private ObjectOutputStream output_stream;
    private ObjectInputStream input_stream;

    /**
     * prepare the client for connect to the server
     * @param server the server info containing ip and port
     */
    public ClientHandlerConnection(ServerInfo server)
    {
        this.serverInfo = server;
    }

    @Override
    public void run()
    {
        // Connect to the server
        try {
            clientSocket = new Socket(serverInfo.serverIp, serverInfo.serverPort);
            System.out.println("Copnnesso al server!");

            // Init the stream after connection
            output_stream = new ObjectOutputStream(clientSocket.getOutputStream());
            output_stream.flush();

            input_stream = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendRequestToServer(String msg) throws IOException
    {
        output_stream.writeObject(msg);
        output_stream.flush();
    }
}
