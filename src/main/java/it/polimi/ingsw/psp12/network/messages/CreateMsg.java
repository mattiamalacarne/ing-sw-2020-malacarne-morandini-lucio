package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;

/**
 * Class used to pass information to the server for creating a new room
 * @author Luca Morandini
 */
public class CreateMsg extends Message {
    /**
     * Name of the room
     */
    String roomName;

    /**
     * Max number of players that can join the game
     */
    int maxPlayersCount;

    /**
     * Construct the message
     * @param roomName name of the room
     * @param maxPlayersCount max number of players of the game
     * @deprecated
     */
    public CreateMsg(String roomName, int maxPlayersCount) {
        super(MsgCommand.CREATE);
        this.roomName = roomName;
        this.maxPlayersCount = maxPlayersCount;
    }

    /**
     * Construct the message
     * @param maxPlayersCount max number of players of the game
     */
    public CreateMsg(int maxPlayersCount) {
        super(MsgCommand.CREATE);
        this.maxPlayersCount = maxPlayersCount;
    }

    /** @deprecated */
    public String getRoomName() {
        return roomName;
    }

    public int getMaxPlayersCount() {
        return maxPlayersCount;
    }
}
