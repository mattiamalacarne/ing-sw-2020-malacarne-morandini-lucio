package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;

/**
 * Class used by the client to send initialization information
 * @author Luca Morandini
 */
public class PlayerInfoMsg extends Message {

    /**
     * Color selected by the user
     */
    String color;

    /**
     * Positions of the workers
     */
    Point workers[];

    /**
     * Construct the message
     * @param color color selected by the user
     * @param w1 position of the first worker
     * @param w2 position of the second worker
     */
    public PlayerInfoMsg(String color, Point w1, Point w2) {
        // TODO: throw exception if points are not different?
        super(MsgCommand.PLAYER_INFO);
        this.color = color;

        this.workers = new Point[2];
        this.workers[0] = new Point(w1.getX(), w1.getY());
        this.workers[1] = new Point(w2.getX(), w2.getY());
    }

    public String getColor() {
        return color;
    }

    public Point[] getWorkersPositions() {
        return workers;
    }
}
