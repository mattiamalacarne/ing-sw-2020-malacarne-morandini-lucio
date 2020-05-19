package it.polimi.ingsw.psp12.model.cards;

import java.beans.XMLDecoder;
import java.io.FileInputStream;
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
    private static final List<Card> cards = new ArrayList<>();

    /**
     * Loads cards from file on startup
     * @throws IOException IOException
     */
    public static void loadCards() throws IOException {

        List<LoadedCard> loadedCards;

        FileInputStream fis = new FileInputStream("src\\main\\resources\\cards\\Cards.xml");
        XMLDecoder decoder = new XMLDecoder(fis);

        loadedCards = (List<LoadedCard>) decoder.readObject( );

        decoder.close();
        fis.close();

        //Initialize the cards list
        loadedCards.forEach(
                n-> cards.add( new Card(n.getName(), n.getShortDescription(), n.getDescription(), n.getPowers()) )
        );

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
            this.availableCards.add(Card.getNoPowers());
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
        if (card.equals(Card.getNoPowers())) {
            // if the selected card is noPowers
            // the other player can only select the noPowers card
            this.availableCards.removeIf(c -> !c.equals(Card.getNoPowers()));
        }
        else {
            // if the selected card is different from noPowers
            // the other player must select another god power card
            this.availableCards.removeIf(c -> c.equals(card) || c.equals(Card.getNoPowers()));
        }
    }
}
