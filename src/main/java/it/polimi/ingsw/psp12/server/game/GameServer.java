package it.polimi.ingsw.psp12.server.game;

import it.polimi.ingsw.psp12.controller.Controller;
import it.polimi.ingsw.psp12.exceptions.InvalidMaxPlayersException;
import it.polimi.ingsw.psp12.model.GameState;
import it.polimi.ingsw.psp12.network.ClientHandler;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;
import it.polimi.ingsw.psp12.network.messages.JoinMsg;
import it.polimi.ingsw.psp12.network.messages.Message;
import it.polimi.ingsw.psp12.network.Room;
import it.polimi.ingsw.psp12.server.Server;
import it.polimi.ingsw.psp12.server.acceptance.AcceptanceServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server that manages a single game on the provided port
 * @author Luca Morandini
 */
public class GameServer implements Runnable, Server {
    /**
     * Socket used to accept clients
     */
    private ServerSocket socket;

    /**
     * Room that host the current game
     */
    private Room room;

    /**
     * Server that has created the room and is responsible for closing
     */
    private AcceptanceServer creator;

    /**
     * Controller of the game managed by the server
     */
    private Controller controller;

    /**
     * Model of the game managed by the server
     */
    private GameState model;

    public GameServer(Room room, AcceptanceServer creator) throws IOException, InvalidMaxPlayersException {
        this.room = room;
        this.creator = creator;

        socket = new ServerSocket(room.getAssignedPort());
        model = new GameState(room.getMaxPlayersCount());
        controller = new Controller(model, this);
    }

    @Override
    public void run() {
        while (room.isRunning()) {
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
                if (!room.isRunning()) {
                    System.out.println("game server closed");
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
                    // if all client have joined the game can start
                    System.out.println("room full, the game " + room.getAssignedPort() + " can start");
                    controller.initGame();
                }
                break;
            case DISCONNECTED:
                // TODO: remove client from game
                System.out.println("client disconnected from game server");
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

        // update room with the new client
        room.clientJoined();

        // send subscription confirmation to the client
        client.send(new Message(MsgCommand.JOINED));

        System.out.println("client subscribed to the game " + room.getAssignedPort() +
                " [" + room.getPlayersCount() + "/" + room.getMaxPlayersCount() + "]");
    }

    /**
     * Removes the room from the active rooms when the game ended
     */
    public void gameEnded() {
        System.out.println("game " + room.getAssignedPort() + " ended");

        // remove room from the list of available rooms
        creator.gameEnded(room);

        // close the room to disconnect clients
        room.close();

        System.out.println("shutting down game server...");

        try {
            socket.close();
        }
        catch (IOException e) {
            System.out.println("error while shutting down server");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
