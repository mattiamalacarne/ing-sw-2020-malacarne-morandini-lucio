package it.polimi.ingsw.psp12.server.acceptance;

import it.polimi.ingsw.psp12.exceptions.InvalidMaxPlayersException;
import it.polimi.ingsw.psp12.network.ClientHandler;
import it.polimi.ingsw.psp12.network.messages.CreateMsg;
import it.polimi.ingsw.psp12.network.messages.CreatedMsg;
import it.polimi.ingsw.psp12.network.messages.Message;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;
import it.polimi.ingsw.psp12.network.Room;
import it.polimi.ingsw.psp12.server.PortsManager;
import it.polimi.ingsw.psp12.server.Server;
import it.polimi.ingsw.psp12.server.game.GameServer;
import it.polimi.ingsw.psp12.utils.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Server that accepts clients, creates new rooms (games) and redirects clients to the specific game server
 * @author Luca Morandini
 */
public class AcceptanceServer implements Runnable, Server {
    /**
     * Socket used to accept clients
     */
    private final ServerSocket socket;

    /**
     * Determines if the acceptance server is currently running
     */
    private boolean running;

    /**
     * List of currently active rooms
     */
    // TODO: remove!!!
    //private List<Room> rooms;

    /**
     * Room currently in the creation and clients subscription phases
     */
    private Room waitingRoom;
    //private List<ClientHandler> waitingClientsList;

    /**
     * List of connected clients that are waiting to be assigned to a room
     */
    private final Queue<ClientHandler> waitingClients;

    public AcceptanceServer(int port) throws IOException {
        socket = new ServerSocket(port);
        //rooms = new ArrayList<>();

        //waitingClientsList = new ArrayList<>();
        waitingClients = new ConcurrentLinkedQueue<>();
        waitingRoom = null;

        // initialize ports manager for game servers
        PortsManager.init(Constants.GAME_PORTS);
    }

    @Override
    public void run() {
        running = true;

        while (running) {
            try {
                Socket client = socket.accept();
                client.setSoTimeout(Constants.SOCKET_TIMEOUT);

                // create client handler
                ClientHandler clientHandler = new ClientHandler(client);
                // subscribe the server as system commands handler
                clientHandler.setServer(this);

                // TODO: change bare Thread class with Executor/ThreadPool?
                Thread thread = new Thread(clientHandler);
                thread.start();

                //waitingClientsList.add(clientHandler);
                waitingClients.offer(clientHandler);
                handleClient(clientHandler);
            }
            catch (IOException e) {
                if (!running) {
                    System.out.println("acceptance server closed");
                    return;
                }

                System.out.println("client connection failed");
                e.printStackTrace();
            }
        }

        // TODO: kick off clients
        /*for (Room room : rooms) {
            // notify game server to close game and close clients connections
            room.close();
        }*/
    }

    /**
     * Stop acceptance server
     */
    public void close() {
        running = false;

        System.out.println("disconnecting clients...");

        // disconnect clients
        while (!waitingClients.isEmpty()) {
            waitingClients.poll().close();
        }

        System.out.println("shutting down acceptance server...");

        try {
            socket.close();
        }
        catch (IOException e) {
            System.out.println("error while shutting down server");
            e.printStackTrace();
            System.exit(1);
        }
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

            - select players count
        */
        switch (message.getCommand())
        {
            /*case LIST:
                List<Room> rooms = listAvailableRooms();
                client.send(new RoomsMsg(rooms));
                break;
            case CREATE:
                CreateMsg msg = (CreateMsg)message;
                // create room and the corresponding game server
                createRoom(msg.getRoomName(), msg.getMaxPlayersCount(), client);
                break;*/
            case CREATE:
                CreateMsg msg = (CreateMsg)message;
                // create room and the corresponding game server
                createRoom(msg.getMaxPlayersCount(), client);
                //handleClient(client);
                processWaitingClients();
                break;
            case DISCONNECTED:
                System.out.println("client disconnected from acceptance server");
                break;
            case PING:
                //System.out.println("ping received");
                break;
        }
    }

    /**
     * Handle the provided client and determine the message thas has to be sent
     * @param client client to be processed
     */
    private void handleClient(ClientHandler client) {
        // check if there is a room in the creation process
        if (waitingRoom == null) {
            // create new room
            waitingRoom = new Room();

            // ask client to create a game
            client.send(new Message(MsgCommand.REQUEST_CREATE));
            return;
        }

        // check if room has been initialized
        if (!waitingRoom.isRunning()) {
            // send to client wait command
            client.send(new Message(MsgCommand.WAIT));
            return;
        }

        // send to client join command
        // send to the user the room that has been created
        client.send(new CreatedMsg(waitingRoom));
        client.close();

        //waitingClientsList.remove(client);
        // remove processed client from the list of waiting clients
        waitingClients.remove(client);

        // register that a new client subscribed the room
        waitingRoom.subscribe();

        // reset current room if all the clients have joined
        if (waitingRoom.isReady()) {
            waitingRoom = null;
        }
    }

    /**
     * Process the list of waiting clients after the room has been initialized
     */
    private void processWaitingClients() {
        boolean stop = false;

        //while (waitingClientsList.size() > 0) {
        while (!waitingClients.isEmpty() && !stop) {
            //ClientHandler client = waitingClientsList.get(0);
            ClientHandler client = waitingClients.peek();
            handleClient(client);

            // stop processing clients if room has been created but not initialized
            stop = (waitingRoom != null && !waitingRoom.isRunning());
        }
    }

    /**
     * Returns a list of available rooms that are not full and the game has not started
     * @return active rooms
     * @deprecated
     */
    // TODO: remove!!!
    /*private List<Room> listAvailableRooms() {
        return rooms.stream().filter(room -> !room.isFull()).collect(Collectors.toList());
    }*/

    /**
     * Creates room for a new game and the server that handles the game
     * @param maxPlayers max number of players that can join the game
     */
    private void createRoom(int maxPlayers, ClientHandler client) {
        // check if there is a port available for the game server
        if (!PortsManager.available()) {
            waitingRoom = null;

            // notify the user that the creation of the room has failed
            client.send(new Message(MsgCommand.CREATE_FAILED));
            return;
        }

        // get the assigned port
        int port = PortsManager.assign();

        // create room and assign port
        //Room room = new Room();
        waitingRoom.setAssignedPort(port);

        // initialize and activate current room
        waitingRoom.setMaxPlayersCount(maxPlayers);
        waitingRoom.activate();

        GameServer gameServer;
        try {
            // create game server
            gameServer = new GameServer(waitingRoom);
        }
        catch (IOException e) {
            System.out.println("failed to start game server on port " + port);
            e.printStackTrace();

            waitingRoom = null;

            // notify the user that the creation of the room has failed
            client.send(new Message(MsgCommand.CREATE_FAILED));
            return;
        }
        catch (InvalidMaxPlayersException e) {
            System.out.println("invalid max players count: " + waitingRoom.getMaxPlayersCount());

            waitingRoom = null;

            // notify the user that the room can not be created
            client.send(new Message(MsgCommand.INVALID_MAX_PLAYERS));
            return;
        }

        //rooms.add(room);

        // TODO: change bare Thread class with Executor/ThreadPool?
        String threadName = "game_server_" + port;
        Thread thread = new Thread(gameServer, threadName);
        thread.start();

        System.out.println("game created on port " + waitingRoom.getAssignedPort() +
                " [0/" + waitingRoom.getMaxPlayersCount() + "]");

        // send to the user the room that has been created
        //client.send(new CreatedMsg(waitingRoom));
    }

    /**
     * Close the room of an ended game and remove it from the list of active rooms
     * @param room room to be removed
     * @deprecated
     */
    // TODO: remove!!!
    public void gameEnded(Room room) {
        /*if (rooms.remove(room)) {
            System.out.println("game " + room.getAssignedPort() + " closed successfully");
        }
        else {
            System.out.printf("no game found on port " + room.getAssignedPort());
        }*/
    }
}
