package it.polimi.ingsw.psp12.server;

import it.polimi.ingsw.psp12.controller.Controller;
import it.polimi.ingsw.psp12.model.GameState;
import it.polimi.ingsw.psp12.network.ClientHandler;
import it.polimi.ingsw.psp12.network.messages.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server that manages a single game on the provided port
 * @author Luca Morandini
 */
public class GameServer implements Runnable {
    private ServerSocket socket;

    private boolean running;

    /**
     * Controller of the game managed by the server
     */
    private Controller controller;

    /**
     * Model of the game managed by the server
     */
    private GameState model;

    public GameServer(int port, int maxPlayersCount) throws IOException {
        socket = new ServerSocket(port);
        model = new GameState(maxPlayersCount);
        controller = new Controller(model);
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
                clientHandler.setGameServer(this);

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
    public void processCommand(Message message, ClientHandler client) {

        // TODO: if ("subscribe command")
        {
            String name = "test"; // "message.getName()"
            subscribeClient(name, client);
        }
        // TODO: else if ("disconnected command")
        {
            // remove client from game
            // and if necessary terminate game
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

            // send subscription confirmation to the client
            //client.send(new Message()); // TODO: subscribe confirmation command
        }
        else {
            // ask for another name
            //client.send(new Message()); // TODO: invalid name command
        }
    }
}
