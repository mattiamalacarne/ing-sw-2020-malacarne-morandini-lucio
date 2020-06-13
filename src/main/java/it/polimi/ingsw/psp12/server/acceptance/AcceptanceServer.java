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
import java.util.concurrent.*;

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
     * Synchronization lock for running variable
     */
    private final Object runningLock;

    /**
     * Room currently in the creation and clients subscription phases
     */
    private Room waitingRoom;

    /**
     * Client that has created the current room
     */
    private ClientHandler creator;

    /**
     * Synchronization lock for waitingRoom and creator variables
     */
    private final Object waitingRoomLock;

    /**
     * List of connected clients that are waiting to be assigned to a room
     */
    private final Queue<ClientHandler> waitingClients;

    /**
     * Clients thread executor
     */
    private final ExecutorService executor;

    /**
     * Timer used to cancel a request of room creation
     */
    private ScheduledExecutorService requestTimer;

    public AcceptanceServer(int port) throws IOException {
        socket = new ServerSocket(port);

        // initialize ports manager for game servers
        PortsManager.init(Constants.GAME_PORTS);

        executor = Executors.newCachedThreadPool();
        waitingClients = new ConcurrentLinkedQueue<>();

        runningLock = new Object();
        waitingRoomLock = new Object();

        // reset current room
        resetWaitingRoom();
    }

    @Override
    public void run() {
        boolean isRunning;
        synchronized (runningLock) {
            running = true;
            isRunning = true;
        }

        while (isRunning) {
            try {
                Socket client = socket.accept();
                client.setSoTimeout(Constants.SOCKET_TIMEOUT);

                System.out.printf("client %s connected to acceptance server\n",
                        client.getInetAddress());

                try {
                    // create client handler
                    ClientHandler clientHandler = new ClientHandler(client);
                    // subscribe the server as system commands handler
                    clientHandler.setServer(this);

                    // add client to the list of waiting clients
                    executor.execute(clientHandler);
                    waitingClients.offer(clientHandler);

                    // determine the message to be sent to the connected client
                    handleClient(clientHandler);
                }
                catch (IOException e) {
                    System.out.println("acceptance server failed to connect to the client " + client.getInetAddress());
                }
            }
            catch (IOException e) {
                synchronized (runningLock) {
                    isRunning = running;
                }

                if (!isRunning) {
                    System.out.println("acceptance server closed");
                    return;
                }

                System.out.println("server connection failed");
                e.printStackTrace();
            }

            synchronized (runningLock) {
                isRunning = running;
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
        /*
            process incoming command from client
            commands to be managed by acceptance server:
            - create: create a room with the selected players count
            - disconnected: handle disconnection of a client
        */
        switch (message.getCommand())
        {
            case CREATE:
                CreateMsg msg = (CreateMsg)message;
                // create room and the corresponding game server
                createRoom(msg.getMaxPlayersCount(), client);
                // process the list of waiting clients
                processWaitingClients();
                break;
            case DISCONNECTED:
                // handle disconnection of a client
                disconnectedClient(client);
                // process the list of waiting clients
                processWaitingClients();
                break;
        }
    }

    /**
     * Handle the provided client and determine the message that has to be sent
     * @param client client to be processed
     */
    private void handleClient(ClientHandler client) {
        // check if there is a room in the creation process
        synchronized (waitingRoomLock) {
            if (waitingRoom == null) {
                // create new room
                waitingRoom = new Room();
                creator = client;

                // start timer for request of room creation
                startTimer();

                System.out.println("requesting room creation");
                // ask client to create a game
                client.send(new Message(MsgCommand.REQUEST_CREATE));
                return;
            }
        }

        // redirect clients to the game server when the room is initialized and
        // there are enough clients to fill the room
        if (waitingRoom.isRunning() && waitingClients.size() >= waitingRoom.getMaxPlayersCount()) {
            System.out.println("redirecting clients to game server");
            redirectClients();
            return;
        }

        System.out.println("sending wait command");
        // send to client wait command
        client.send(new Message(MsgCommand.WAIT));
    }

    /**
     * Redirect clients in batch to the assigned game server
     */
    private void redirectClients() {
        synchronized (waitingRoomLock) {
            for (int i = 0; i < waitingRoom.getMaxPlayersCount(); i++) {
                // remove processed client from the list of waiting clients
                ClientHandler client = waitingClients.poll();

                if (client != null) {
                    // send to client join command
                    // send to the user the room that has been created
                    client.send(new CreatedMsg(waitingRoom));
                    client.close();
                }
            }
        }

        // reset current room
        resetWaitingRoom();
    }

    /**
     * Process the list of waiting clients after the room has been initialized
     */
    private void processWaitingClients() {
        boolean stop = false;

        while (!waitingClients.isEmpty() && !stop) {
            ClientHandler client = waitingClients.peek();
            handleClient(client);

            // stop processing clients if room has been created but not initialized
            stop = waitingRoom != null;
        }
    }

    /**
     * Creates room for a new game and the server that handles the game
     * @param maxPlayers max number of players that can join the game
     */
    private synchronized void createRoom(int maxPlayers, ClientHandler client) {
        // stop timer for request of room creation
        cancelTimer();

        // check if there is a port available for the game server
        if (!PortsManager.available()) {
            // reset current room
            resetWaitingRoom();

            System.out.println("no ports available, aborting room creation");

            // notify the user that the creation of the room has failed
            client.send(new Message(MsgCommand.CREATE_FAILED));
            return;
        }

        // get the assigned port
        int port = PortsManager.assign();

        // create room and assign port
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

            // release assigned port
            PortsManager.release(port);

            // reset current room
            resetWaitingRoom();

            // notify the user that the creation of the room has failed
            client.send(new Message(MsgCommand.CREATE_FAILED));
            return;
        }
        catch (InvalidMaxPlayersException e) {
            System.out.println("invalid max players count: " + waitingRoom.getMaxPlayersCount());

            // release assigned port
            PortsManager.release(port);

            // reset current room
            resetWaitingRoom();

            // notify the user that the room can not be created
            client.send(new Message(MsgCommand.INVALID_MAX_PLAYERS));
            return;
        }

        String threadName = "game_server_" + port;
        Thread thread = new Thread(gameServer, threadName);
        thread.start();

        System.out.println("game created on port " + waitingRoom.getAssignedPort() +
                " [" + waitingRoom.getMaxPlayersCount() + "]");
    }

    /**
     * Reset information about the current room
     */
    private void resetWaitingRoom() {
        synchronized (waitingRoomLock) {
            waitingRoom = null;
            creator = null;
        }

        System.out.println("reset waiting room");
    }

    /**
     * Process the disconnection of a client
     * @param client disconnected client
     */
    private synchronized void disconnectedClient(ClientHandler client) {
        System.out.println("client disconnected from acceptance server");

        // close socket
        client.close();

        // remove client from the list of waiting clients
        waitingClients.remove(client);

        // reset waiting room if the creator has disconnected
        if (client.equals(creator)) {
            resetWaitingRoom();

            System.out.println("creator disconnected, aborting room creation");
        }
    }

    /**
     * Stop acceptance server
     */
    public void close() {
        synchronized (runningLock) {
            running = false;
        }

        System.out.println("disconnecting clients...");

        // disconnect waiting clients
        while (!waitingClients.isEmpty()) {
            waitingClients.poll().close();
        }

        // shutdown clients thread executor
        executor.shutdownNow();

        // stop request timer if it is running
        if (requestTimer != null) {
            requestTimer.shutdownNow();
        }

        System.out.println("shutting down acceptance server...");

        try {
            socket.close();
        }
        catch (IOException e) {
            System.out.println("error while shutting down acceptance server");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Start timer for request of room creation
     */
    private void startTimer() {
        System.out.println("starting request timer");

        requestTimer = Executors.newSingleThreadScheduledExecutor();
        requestTimer.schedule(() -> {
            // TODO: handle multi threading
            System.out.println("request timer expired");
            // disconnect client
            disconnectedClient(creator);
            // process the list of waiting clients
            processWaitingClients();
        }, Constants.REQUEST_INTERVAL, TimeUnit.SECONDS);
    }

    /**
     * Stop timer for request of room creation
     */
    private void cancelTimer() {
        requestTimer.shutdownNow();
        System.out.println("request timer stopped");
    }
}
