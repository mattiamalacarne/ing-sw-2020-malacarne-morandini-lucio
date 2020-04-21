package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;
import it.polimi.ingsw.psp12.network.Room;

import java.util.Collections;
import java.util.List;

/**
 * Class that holds a list of available (not full) rooms on the server
 * @author Luca Morandini
 */
public class RoomsMsg extends Message {
    /**
     * List of available rooms
     */
    List<Room> rooms;

    /**
     * Construct the message
     * @param rooms list of rooms to be returned to the client
     */
    public RoomsMsg(List<Room> rooms) {
        super(MsgCommand.ROOMS);
        this.rooms = rooms;
    }

    public List<Room> getRooms() {
        return Collections.unmodifiableList(rooms);
    }
}
