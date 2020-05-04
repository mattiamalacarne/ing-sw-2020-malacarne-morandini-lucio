package it.polimi.ingsw.psp12.model;

import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.utils.Color;

import java.io.Serializable;

/**
 * <p><b>Class</b> that represent a worker</p>
 *
 * @author Mattia Malacarne
 */

public class Worker implements Serializable
{
    private Cell position;

    private Color color;

    /**
     * get the selected worker position
     * @return the worker position in the board
     */
    public Cell getPosition() {
        return position;
    }

    /**
     * move the worker from a cell to another
     * @param cell where to move the worker
     */
    public void move(Cell cell) {
        position = cell;
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
}
