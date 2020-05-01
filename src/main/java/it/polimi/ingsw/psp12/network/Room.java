package it.polimi.ingsw.psp12.network;

import it.polimi.ingsw.psp12.server.acceptance.AcceptanceServer;

import java.io.Serializable;

/**
 * Room that holds information about a game
 * @author Luca Morandini
 */
public class Room implements Serializable{
    /**
     * Port of the server that manages the game
     */
    private int assignedPort;

    /**
     * Name of the room
     */
    private String name;

    /**
     * Max number of players that can join the game
     */
    private int maxPlayersCount;

    /**
     * Number of players that has joined the game
     */
    private int playersCount;

    /**
     * Determines if the game is currently active
     */
    private boolean active;

    /**
     * Server that has created the room and is responsible for closing
     */
    //FIXME: uncomment transient to not cause the serialization problem of socket
    /*transient */private AcceptanceServer creator;

    public Room(String name, int maxPlayersCount, AcceptanceServer creator) {
        this.name = name;
        this.maxPlayersCount = maxPlayersCount;
        this.creator = creator;
        this.active = true;
    }

    /**
     * Returns port on where the game server has to listen
     * @return assigned port for the game
     */
    public int getAssignedPort() {
        return assignedPort;
    }

    /**
     * Set the port on where the game server has to listen
     * @param assignedPort port for the game
     */
    public void setAssignedPort(int assignedPort) {
        this.assignedPort = assignedPort;
    }

    /**
     * Register that a new client joined the game
     */
    public void clientJoined() {
        playersCount++;
    }

    /**
     * Get the name of the room
     * @return room name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns max number of players that can join the game
     * @return max players count
     */
    public int getMaxPlayersCount() {
        return maxPlayersCount;
    }

    /**
     * Returns the number of players that has joined the game
     * @return number of players that has joined the game
     */
    public int getPlayersCount() {
        return playersCount;
    }

    /**
     * Checks if the all clients have joined the game and so the room is full
     * @return true if room is full
     */
    public boolean isFull() {
        return (playersCount >= maxPlayersCount);
    }

    /**
     * Checks if the game is currently active
     * @return true if game is active
     */
    public boolean isRunning() {
        return active;
    }

    /**
     * Interrupts the game and marks it as not active
     */
    public void close() {
        active = false;
    }

    /**
     * Removes the room from the active rooms when the game ended
     */
    public void gameEnded() {
        creator.gameEnded(assignedPort);
    }
}
