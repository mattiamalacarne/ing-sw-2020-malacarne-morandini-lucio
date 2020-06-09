package it.polimi.ingsw.psp12.server.game;

import it.polimi.ingsw.psp12.controller.Controller;
import it.polimi.ingsw.psp12.exceptions.InvalidMaxPlayersException;
import it.polimi.ingsw.psp12.model.GameState;
import it.polimi.ingsw.psp12.network.ClientHandler;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;
import it.polimi.ingsw.psp12.network.messages.JoinMsg;
import it.polimi.ingsw.psp12.network.messages.Message;
import it.polimi.ingsw.psp12.network.Room;
import it.polimi.ingsw.psp12.server.PortsManager;
import it.polimi.ingsw.psp12.server.Server;
import it.polimi.ingsw.psp12.utils.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.*;

/**
 * Server that manages a single game on the provided port
 * @author Luca Morandini
 */
public class GameServer implements Runnable, Server {
    /**
     * Socket used to accept clients
     */
    private final ServerSocket socket;

    /**
     * Room that host the current game
     */
    private final Room room;

    /**
     * Controller of the game managed by the server
     */
    private final Controller controller;

    /**
     * Model of the game managed by the server
     */
    private final GameState model;

    /**
     * List of connected clients that have to join the game
     */
    private final Queue<ClientHandler> waitingClients;

    /**
     * Clients thread executor
     */
    private final ExecutorService executor;

    /**
     * Timer used for aborting a game if not all clients connect to the game server
     */
    private ScheduledExecutorService abortTimer;

    public GameServer(Room room) throws IOException, InvalidMaxPlayersException {
        this.room = room;

        socket = new ServerSocket(room.getAssignedPort());
        model = new GameState(room.getMaxPlayersCount());
        controller = new Controller(model, this);

        executor = Executors.newCachedThreadPool();
        waitingClients = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void run() {
        while (room.isRunning()) {
            try {
                Socket client = socket.accept();
                client.setSoTimeout(Constants.SOCKET_TIMEOUT);

                System.out.printf("client %s connected to game server %d\n",
                        client.getInetAddress(), room.getAssignedPort());

                try {
                    // create client handler
                    ClientHandler clientHandler = new ClientHandler(client);
                    // subscribe the server as system commands handler
                    clientHandler.setServer(this);

                    executor.execute(clientHandler);
                    waitingClients.offer(clientHandler);

                    // start abort game timer
                    startTimer();
                }
                catch (IOException e) {
                    System.out.println("game server failed to connect to the client " + client.getInetAddress());
                }
            }
            catch (IOException e) {
                if (!room.isRunning()) {
                    System.out.println("game server " + room.getAssignedPort() + " closed");
                    return;
                }

                System.out.println("client connection failed");
                e.printStackTrace();
            }
        }
    }

    /**
     * Process system commands incoming from clients
     * @param message message to be processed
     * @param client client that generated the command
     */
    @Override
    public void processCommand(Message message, ClientHandler client) {
        // process incoming command from client
        switch (message.getCommand())
        {
            case JOIN:
                JoinMsg msg = (JoinMsg)message;
                // subscribe the client to the game
                subscribeClient(msg.getUserName(), client);

                if (room.isFull()) {
                    // stop abort game timer
                    cancelTimer();
                    // if all client have joined the game can start
                    System.out.println("room full, the game " + room.getAssignedPort() + " can start");
                    controller.initGame();
                }
                break;
            case DISCONNECTED:
                disconnectedClient(client);
                break;
            case PING:
                //System.out.println("ping received");
                break;
        }
    }

    /**
     * Subscribe to a game if the room is not full and the name selected by the user is not already taken
     * @param name name provided by the user
     * @param client client handler of the user
     */
    private void subscribeClient(String name, ClientHandler client) {
        // subscribe the client only if the room is not full
        if (room.isFull()) {
            // notify the client that the game is already full and close connection
            client.send(new Message(MsgCommand.ROOM_FULL));
            client.close();
            System.out.println("client tried to subscribe to a full room");
            return;
        }

        // subscribe the client only if the selected name is not already taken
        if (model.alreadyRegistered(name)) {
            // ask user for another name
            client.send(new Message(MsgCommand.INVALID_NICKNAME));
            System.out.println("name already taken");
            return;
        }

        // register client to the game
        controller.addClient(client, name);
        waitingClients.remove(client);

        // update room with the new client
        room.clientJoined();

        // send subscription confirmation to the client
        client.send(new Message(MsgCommand.JOINED));

        System.out.println("client subscribed to the game " + room.getAssignedPort() +
                " [" + room.getPlayersCount() + "/" + room.getMaxPlayersCount() + "]");
    }

    /**
     * Process the disconnection of a client
     * @param client disconnected client
     */
    private void disconnectedClient(ClientHandler client) {
        System.out.println("client disconnected from game server " + room.getAssignedPort());
        // close socket to avoid sending close message
        client.close();

        // TODO: handle multi threading
        // stop game and disconnect all clients
        controller.endGame();
    }

    /**
     * Removes the room from the active rooms when the game ended
     */
    public void gameEnded() {
        System.out.println("game " + room.getAssignedPort() + " ended");

        // release assigned port
        PortsManager.release(room.getAssignedPort());

        // close the room
        room.close();

        // disconnect clients that do not have joined the game
        while (!waitingClients.isEmpty()) {
            waitingClients.poll().close();
        }

        // shutdown clients thread executor
        executor.shutdownNow();

        // stop abort timer if it is running
        if (abortTimer != null) {
            abortTimer.shutdownNow();
        }

        System.out.println("shutting down game server " + room.getAssignedPort() + "...");

        try {
            socket.close();
        }
        catch (IOException e) {
            System.out.println("error while shutting down game server " + room.getAssignedPort());
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Start timer for aborting a game
     */
    private void startTimer() {
        if (abortTimer == null) {
            System.out.println("starting abort timer");

            abortTimer = Executors.newSingleThreadScheduledExecutor();
            abortTimer.schedule(() -> {
                // TODO: handle multi threading
                System.out.println("abort timer expired");
                // stop game and disconnect all clients
                controller.endGame();
            }, Constants.ABORT_INTERVAL, TimeUnit.MINUTES);
        }
    }

    /**
     * Stop timer for aborting a game
     */
    private void cancelTimer() {
        abortTimer.shutdownNow();
        System.out.println("abort timer stopped");
    }
}
