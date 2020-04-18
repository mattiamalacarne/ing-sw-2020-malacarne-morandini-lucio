package it.polimi.ingsw.psp12.server.acceptance;

import it.polimi.ingsw.psp12.network.ClientHandler;
import it.polimi.ingsw.psp12.network.messages.Message;
import it.polimi.ingsw.psp12.server.acceptance.Constants;
import it.polimi.ingsw.psp12.server.acceptance.Room;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Server implements Runnable{
    private ServerSocket socket;

    private boolean running;

    private Map<Integer, Room> availableRooms;

    public Server(int port) throws IOException {
        socket = new ServerSocket(port);
        availableRooms = new HashMap<>();
    }

    @Override
    public void run() {
        running = true;

        while (running) {
            try {
                Socket client = socket.accept();
                ClientHandler clientHandler = new ClientHandler(client);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
            catch (IOException e) {
                System.out.println("client connection failed");
                e.printStackTrace();
            }
        }

        // TODO: kick off clients

        try {
            socket.close();
            System.out.println("acceptance server closed");
        }
        catch (IOException e) {
            System.out.println("error while shutting down server");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Stop acceptance server
     */
    public void close() {
        running = false;
    }

    public void processCommand(ClientHandler client, String cmd) {
        switch (cmd)
        {
            case "create":
            {
                // TODO: change port assignment strategy
                int port = Constants.MATCHES_STARTING_PORT + availableRooms.size();
                Room room = new Room(port);
                room.addClient(client);

                availableRooms.put(port, room);

                //client.send("created");
                //client.send(new Message());
                break;
            }
            case "join":
            {
                // TODO: get port
                int port = 0;
                Room room = availableRooms.get(port);
                if (room == null) {
                    //client.send("invalid request");
                    //client.send(new Message());
                }

                room.addClient(client);
                break;
            }
            case "list":
            {
                Collection<Room> rooms = availableRooms.values();
            }
        }
    }
}
