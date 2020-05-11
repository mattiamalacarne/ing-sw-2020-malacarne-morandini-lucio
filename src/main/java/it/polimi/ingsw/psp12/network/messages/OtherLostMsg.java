package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;

/**
 * Message used to notify clients that a player has lost but the game can continue
 * @author Luca Morandini
 */
public class OtherLostMsg extends Message {
    /**
     * Name of the player that has lost
     */
    String player;

    /**
     * Construct the message
     * @param player name of the player that has lost
     */
    public OtherLostMsg(String player) {
        super(MsgCommand.OTHER_LOST);

        this.player = player;
    }

    public String getPlayer() {
        return player;
    }
}
