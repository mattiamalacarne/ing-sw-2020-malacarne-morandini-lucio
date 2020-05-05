package it.polimi.ingsw.psp12.server.acceptance;

import it.polimi.ingsw.psp12.network.ClientHandler;
import it.polimi.ingsw.psp12.network.messages.CreateMsg;
import it.polimi.ingsw.psp12.network.messages.CreatedMsg;
import it.polimi.ingsw.psp12.network.messages.Message;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;
import it.polimi.ingsw.psp12.network.messages.RoomsMsg;
import it.polimi.ingsw.psp12.network.Room;
import it.polimi.ingsw.psp12.server.Server;
import it.polimi.ingsw.psp12.server.game.GameServer;
import it.polimi.ingsw.psp12.utils.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Server that accepts clients, creates new rooms (games) and redirects clients to the specific game server
 * @author Luca Morandini
 */
public class AcceptanceServer implements Runnable, Server {
    /**
     * Socket used to accept clients
     */
    private ServerSocket socket;

    /**
     * Determines if the acceptance server is currently running
     */
    private boolean running;

    /**
     * List of currently active rooms
     */
    private List<Room> rooms;

    public AcceptanceServer(int port) throws IOException {
        socket = new ServerSocket(port);
        rooms = new ArrayList<>();
    }

    @Override
    public void run() {
        running = true;

        while (running) {
            try {
                Socket client = socket.accept();

                // create client handler
                ClientHandler clientHandler = new ClientHandler(client);
                // subscribe the server as system commands handler
                clientHandler.setServer(this);

                // TODO: change bare Thread class with Executor/ThreadPool?
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
            catch (IOException e) {
                System.out.println("client connection failed");
                e.printStackTrace();
            }
        }

        // TODO: kick off clients
        for (Room room : rooms) {
            // notify game server to close game and close clients connections
            room.close();
        }

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

    /**
     * Process system commands incoming from clients
     * @param message message to be processed
     * @param client client that generated the command
     */
    @Override
    public void processCommand(Message message, ClientHandler client) {
        /*
            commands to be managed by acceptance server:
            - list: return list of available rooms
            - create: create a room and add to the list of active rooms
        */
        switch (message.getCommand())
        {
            case LIST:
                List<Room> rooms = listAvailableRooms();
                client.send(new RoomsMsg(rooms));
                break;
            case CREATE:
                CreateMsg msg = (CreateMsg)message;
                // create room and the corresponding game server
                createRoom(msg.getRoomName(), msg.getMaxPlayersCount(), client);
                break;
            case DISCONNECTED:
                System.out.println("client disconnected from acceptance server");
                break;
        }
    }

    /**
     * Returns a list of available rooms that are not full and the game has not started
     * @return active rooms
     */
    private List<Room> listAvailableRooms() {
        return rooms.stream().filter(room -> !room.isFull()).collect(Collectors.toList());
    }

    /**
     * Creates room for a new game and the server that handles the game
     * @param name name of the room
     * @param maxPlayers max number of players that can join the game
     */
    private void createRoom(String name, int maxPlayers, ClientHandler client) {
        // TODO: change port assignment strategy
        int port = Constants.MATCHES_STARTING_PORT + rooms.size();

        // TODO: check that maxPlayers is 2 or 3

        // create room and assign port
        Room room = new Room(name, maxPlayers);
        room.setAssignedPort(port);
        rooms.add(room);

        GameServer gameServer;
        try {
            // create game server
            gameServer = new GameServer(room, this);
        }
        catch (IOException e) {
            System.out.println("failed to start game server on port " + port);
            e.printStackTrace();

            // notify the user that the creation of the room has failed
            client.send(new Message(MsgCommand.CREATE_FAILED));
            return;
        }

        // TODO: change bare Thread class with Executor/ThreadPool?
        String threadName = "game_server_" + port;
        Thread thread = new Thread(gameServer, threadName);
        thread.start();

        System.out.println("game created on port " + room.getAssignedPort() +
                " [0/" + room.getMaxPlayersCount() + "]");

        // send to the user the room that has been created
        client.send(new CreatedMsg(room));
    }

    /**
     * Close the room of an ended game and remove it from the list of active rooms
     * @param room room to be removed
     */
    public void gameEnded(Room room) {
        if (rooms.remove(room)) {
            System.out.println("game " + room.getAssignedPort() + " closed successfully");
        }
        else {
            System.out.printf("no game found on port " + room.getAssignedPort());
        }
    }
}
