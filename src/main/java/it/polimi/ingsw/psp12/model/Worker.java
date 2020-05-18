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

    private String name;

    public Worker() {
        this.name = "worker";
    }

    public Worker(String player, int id) {
        this.name = player + "." + id;
    }

    /**
     * get the name of the worker
     * @return worker name
     */
    public String getName() {
        return name;
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
    public Worker clone() {
        Worker w = new Worker();
        w.name = this.name;
        w.color = this.color;
        if (this.position != null) {
            w.position = this.position.clone();
        }
        return w;
    }
}
