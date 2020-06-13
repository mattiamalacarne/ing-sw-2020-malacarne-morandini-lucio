package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.model.cards.Card;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;

import java.util.List;

/**
 * Class for sending to the client a list of cards that can be selected
 * @author Luca Morandini
 */
public class CardsListMsg extends Message {
    /**
     * List of cards that can be selected
     */
    private final List<Card> cards;

    /**
     * Remaining cards count that the player have to select
     */
    private final int remaining;

    /**
     * Create the message with the list of cards that can be selected
     * @param cards list of available cards
     * @param remaining remaining cards count that the player have to select
     */
    public CardsListMsg(List<Card> cards, int remaining) {
        super(MsgCommand.CARDS_LIST);

        this.cards = cards;
        this.remaining = remaining;
    }

    public List<Card> getCards() {
        return cards;
    }

    public int getRemaining() {
        return remaining;
    }
}
