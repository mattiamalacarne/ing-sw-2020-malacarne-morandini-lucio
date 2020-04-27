package it.polimi.ingsw.psp12.server.game;

import it.polimi.ingsw.psp12.controller.Controller;
import it.polimi.ingsw.psp12.model.GameState;
import it.polimi.ingsw.psp12.network.ClientHandler;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;
import it.polimi.ingsw.psp12.network.messages.JoinMsg;
import it.polimi.ingsw.psp12.network.messages.Message;
import it.polimi.ingsw.psp12.network.Room;
import it.polimi.ingsw.psp12.server.Server;

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
     * Controller of the game managed by the server
     */
    private Controller controller;

    /**
     * Model of the game managed by the server
     */
    private GameState model;

    public GameServer(Room room) throws IOException {
        this.room = room;

        socket = new ServerSocket(room.getAssignedPort());
        model = new GameState(room.getMaxPlayersCount());
        controller = new Controller(model);
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
                System.out.println("client connection failed");
                e.printStackTrace();
            }
        }

        // TODO: terminate game and kick off clients

        try {
            socket.close();
            System.out.println("game server closed");
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
        // process incoming command from client
        switch (message.getCommand())
        {
            case JOIN:
                JoinMsg msg = (JoinMsg)message;
                // subscribe the client to the game
                subscribeClient(msg.getUserName(), client);

                if (room.isFull()) {
                    // if all client have joined the game can start
                    controller.initGame();
                }
                break;
            case DISCONNECTED:
                // remove client from game
                // and if necessary terminate game
                break;
        }
    }

    /**
     * Subscribe to a game if the name selected by the user is not already taken
     * @param name name provided by the user
     * @param client client handler of the user
     */
    private void subscribeClient(String name, ClientHandler client) {
        if (!model.alreadyRegistered(name)) {
            // register client to the game
            controller.addClient(client, name);

            // update room with the new client
            room.clientJoined();

            // send subscription confirmation to the client
            client.send(new Message(MsgCommand.JOINED));
        }
        else {
            // ask user for another name
            client.send(new Message(MsgCommand.INVALID_NICKNAME));
        }
    }
}
