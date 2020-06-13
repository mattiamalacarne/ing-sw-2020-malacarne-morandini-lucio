package it.polimi.ingsw.psp12.network;

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
     * Max number of players that can join the game
     */
    private int maxPlayersCount;

    /**
     * Number of players that have joined the game
     */
    private int playersCount;

    /**
     * Determines if the game is currently active and initialized
     */
    private boolean active;

    public Room() {
        this.active = false;
        this.playersCount = 0;
    }

    /**
     * Set the max number of player that can join the room
     * @param maxPlayersCount max number of players
     */
    public void setMaxPlayersCount(int maxPlayersCount) {
        this.maxPlayersCount = maxPlayersCount;
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
     * Returns max number of players that can join the game
     * @return max players count
     */
    public int getMaxPlayersCount() {
        return maxPlayersCount;
    }

    /**
     * Register that a new client joined the game
     */
    public void clientJoined() {
        playersCount++;
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
     * Activate the room after the initialization process
     */
    public synchronized void activate() {
        active = true;
    }

    /**
     * Checks if the game is currently active
     * @return true if game is active
     */
    public synchronized boolean isRunning() {
        return active;
    }

    /**
     * Interrupts the game and marks it as not active
     */
    public synchronized void close() {
        active = false;
    }
}
