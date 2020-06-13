package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.model.cards.Card;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;

/**
 * Class used to send to the the player the assigned card
 * @author Luca Morandini
 */
public class YourCardMsg extends Message {
    /**
     * Card assigned to the player
     */
    private final Card card;

    /**
     * Create the message with the card assigned to the player
     * @param card assigned card
     */
    public YourCardMsg(Card card) {
        super(MsgCommand.YOUR_CARD);

        this.card = card;
    }

    public Card getCard() {
        return card;
    }
}
