package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.model.cards.Card;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;

/**
 * Class used to send to the server the selected card
 * @author Luca Morandini
 */
public class SelectCardMsg extends Message {
    /**
     * Selected card
     */
    Card card;

    /**
     * Create the message with the card selected by the user
     * @param card selected card
     */
    public SelectCardMsg(Card card) {
        super(MsgCommand.SELECTED_CARD);

        this.card = card;
    }

    public Card getCard() {
        return card;
    }
}
