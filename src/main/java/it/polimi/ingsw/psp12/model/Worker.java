package it.polimi.ingsw.psp12.model;

import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.utils.Color;

import java.io.Serializable;

/**
 * <p><b>Class</b> that represent a worker</p>
 *
 * @author Mattia Malacarne
 */

public class Worker implements Serializable
{
    private Point position;

    private Color color;

    private final String playerName;

    private final int playerId;

    private final int workerId;

    public Worker() {
        this(0, "worker", 0);
    }

    public Worker(int playerId, String playerName, int workerId) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.workerId = workerId;
    }

    /**
     * get the name of the player that holds the worker
     * @return player name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * get the id of the player that holds the worker
     * @return player id
     */
    public int getPlayerId() {
        return playerId;
    }

    /**
     * get the id of the worker
     * @return worker id
     */
    public int getId() {
        return workerId;
    }

    /**
     * get the selected worker position
     * @return the worker position in the board
     */
    public Point getPosition() {
        return position;
    }

    /**
     * move the worker from a cell to another
     * @param pos where to move the worker
     */
    public void move(Point pos) {
        position = pos;
    }

    /**
     * Set the color of the worker selected by the user
     * @param color color selected by the user
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * Get the color of the worker
     * @return the color of the worker
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns a clone of the worker
     * @return worker clone
     */
    @Override
    public Worker clone() {
        Worker w = new Worker(this.playerId, this.playerName, this.workerId);
        w.color = this.color;
        if (this.position != null) {
            w.position = this.position.clone();
        }
        return w;
    }
}
