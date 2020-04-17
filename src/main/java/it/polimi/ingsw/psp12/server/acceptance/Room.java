package it.polimi.ingsw.psp12.server.acceptance;

import it.polimi.ingsw.psp12.network.ClientHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Temporary room that holds clients subscribed to a game
 * @author Luca Morandini
 */
public class Room {
    /**
     * List of clients subscribed to the game
     */
    private List<ClientHandler> clients;

    /**
     * Port of the server that manages the game
     */
    private int assignedPort;

    public Room(int port) {
        clients = new ArrayList<>();
        assignedPort = port;
    }

    public int getAssignedPort() {
        return assignedPort;
    }

    public List<ClientHandler> getClients() {
        return new ArrayList<>(clients);
    }

    /**
     * Add client that has subscribed to the game
     * @param client new client
     */
    public void addClient(ClientHandler client) {
        clients.add(client);
    }
}
