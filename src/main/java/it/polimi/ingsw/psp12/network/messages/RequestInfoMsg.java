package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used by the server to notify the user about the colors and positions that a user can select
 * @author Luca Morandini
 */
public class RequestInfoMsg extends Message {

    /**
     * List of available colors
     */
    List<String> availableColors;

    /**
     * List of available positions
     */
    List<Point> availablePositions;

    /**
     * Construct the message
     */
    public RequestInfoMsg() {
        super(MsgCommand.REQUEST_INFO);
        availablePositions = new ArrayList<>();
    }

    public void setAvailableColors(List<String> availableColors) {
        this.availableColors = availableColors;
    }

    public List<String> getAvailableColors() {
        return availableColors;
    }

    public void addPosition(Point p) {
        availablePositions.add(new Point(p.getX(), p.getY()));
    }

    public List<Point> availablePositions() {
        return availablePositions;
    }
}
