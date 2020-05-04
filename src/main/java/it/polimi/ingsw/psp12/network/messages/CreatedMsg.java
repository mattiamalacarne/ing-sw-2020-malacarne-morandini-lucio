package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.network.Room;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;

/**
 * Class used to notify the client that a new room has been created
 * @author Luca Morandini
 */
public class CreatedMsg extends Message {
    /**
     * Room that has been created
     */
    Room room;

    /**
     * Construct the message
     * @param room room that has been created
     */
    public CreatedMsg(Room room) {
        super(MsgCommand.CREATED);

        this.room = room;
    }

    public Room getRoom() {
        return room;
    }
}
