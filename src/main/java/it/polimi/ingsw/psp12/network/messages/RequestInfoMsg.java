package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.model.cards.Card;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;
import it.polimi.ingsw.psp12.utils.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used by the server to notify the user about the colors, positions and cards that a user can select
 * @author Luca Morandini
 */
public class RequestInfoMsg extends Message {

    /**
     * List of available colors
     */
    List<Color> availableColors;

    /**
     * List of available positions
     */
    List<Point> availablePositions;

    /**
     * List of available cards
     */
    List<Card> availableCards;

    /**
     * Construct the message
     */
    public RequestInfoMsg() {
        super(MsgCommand.REQUEST_INFO);
        availablePositions = new ArrayList<>();
    }

    public void setAvailableColors(List<Color> availableColors) {
        this.availableColors = availableColors;
    }

    public List<Color> getAvailableColors() {
        return availableColors;
    }

    public void addPosition(Point p) {
        availablePositions.add(new Point(p.getX(), p.getY()));
    }

    public List<Point> getAvailablePositions() {
        return availablePositions;
    }

    public void setAvailableCards(List<Card> availableCards) {
        this.availableCards = availableCards;
    }

    public List<Card> getAvailableCards() {
        return availableCards;
    }
}
