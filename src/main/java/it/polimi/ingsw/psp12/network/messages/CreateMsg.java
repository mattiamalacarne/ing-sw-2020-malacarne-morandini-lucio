package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;

/**
 * Class used to pass information to the server for creating a new room
 * @author Luca Morandini
 */
public class CreateMsg extends Message {
    /**
     * Max number of players that can join the game
     */
    private final int maxPlayersCount;

    /**
     * Construct the message
     * @param maxPlayersCount max number of players of the game
     */
    public CreateMsg(int maxPlayersCount) {
        super(MsgCommand.CREATE);
        this.maxPlayersCount = maxPlayersCount;
    }

    public int getMaxPlayersCount() {
        return maxPlayersCount;
    }
}
