package it.polimi.ingsw.psp12.model.cards;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that loads cars from file and manages decks for the games
 * @author Luca Morandini
 */
public class Deck {
    /**
     * List of cards loaded from file
     */
    private static List<Card> cards;

    /**
     * Card that represent the basic game without god powers
     */
    private static Card noPowers;

    /**
     * Loads cards from file on startup
     * @throws IOException
     */
    public static void loadCards() throws IOException {
        // define the no powers card
        noPowers = new Card(-1,
                "No card",
                "Play without powers",
                "Play the game without using god powers",
                new ArrayList<>());

        cards = new ArrayList<>();

        // TODO: load cards from files
    }

    /**
     * List of available cards for every game
     */
    private final List<Card> availableCards;

    /**
     * Create a deck of cards
     * @param maxPlayersCount number of players in the game
     */
    public Deck(int maxPlayersCount) {
        this.availableCards = new ArrayList<>();

        // if there are two players
        // the game can be played without cards
        if (maxPlayersCount < 3) {
            this.availableCards.add(noPowers);
        }

        this.availableCards.addAll(cards);
    }

    /**
     * Returns the list of available cards that a player can select
     * @return available cards
     */
    public List<Card> getAvailableCards() {
        return new ArrayList<>(availableCards);
    }

    /**
     * Updates the deck when a player select a card
     * @param card card selected by the player
     */
    public void cardSelected(Card card) {
        if (card.equals(noPowers)) {
            // if the selected card is noPowers
            // the other player can only select the noPowers card
            this.availableCards.removeIf(c -> !c.equals(noPowers));
        }
        else {
            // if the selected card is different from noPowers
            // the other player must select another god power card
            this.availableCards.removeIf(c -> c.equals(card) || c.equals(noPowers));
        }
    }
}
