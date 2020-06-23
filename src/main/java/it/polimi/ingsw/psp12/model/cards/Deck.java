package it.polimi.ingsw.psp12.model.cards;

import java.beans.XMLDecoder;
import java.io.IOException;
import java.io.InputStream;
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
    @SuppressWarnings("unchecked")
    public static void loadCards() throws IOException {

        // make sure to load cards only one time
        if (cards.size() > 0) {
            return;
        }

        InputStream fis = Deck.class.getClassLoader().getResourceAsStream("cards/Cards.xml");
        if (fis == null) {
            throw new IOException("cards file not found");
        }

        XMLDecoder decoder = new XMLDecoder(fis);

        List<LoadedCard> loadedCards;
        try {
            loadedCards = (List<LoadedCard>) decoder.readObject();
        }
        catch (ClassCastException e) {
            throw new IOException("invalid cards file");
        }

        decoder.close();
        fis.close();

        //Initialize the cards list
        loadedCards.forEach(
                n-> cards.add( new Card(n.getName(), n.getShortDescription(), n.getDescription(), n.getPowers(), n.getImage()) )
        );

    }

    /**
     * List of available cards for every game
     */
    private final List<Card> availableCards;

    /**
     * List of selected cards by the first player that can be selected by the other players
     */
    private final List<Card> selectedCards;

    /**
     * Count of the cards that the first player has still to select
     */
    private int selectionRemainingCount;

    /**
     * Create a deck of cards
     * @param maxPlayersCount number of players in the game
     */
    public Deck(int maxPlayersCount) {
        this.availableCards = new ArrayList<>();
        this.selectedCards = new ArrayList<>();
        this.selectionRemainingCount = maxPlayersCount;

        // if there are two players
        // the game can be played without cards
        if (maxPlayersCount < 3) {
            this.availableCards.add(Card.getNoPowers());
        }

        this.availableCards.addAll(cards);
    }

    /**
     * Returns the list of available cards that the first player can select
     * @return available cards
     */
    public List<Card> getAvailableCards() {
        return new ArrayList<>(availableCards);
    }

    /**
     * Returns the list of cards selected by the first player that other players can select
     * @return selected cards
     */
    public List<Card> getSelectedCards() {
        return new ArrayList<>(selectedCards);
    }

    /**
     * Updates the deck when the first player select a card
     * @param card card selected by the first player
     */
    public void cardSelected(Card card) {
        // if the selected card is different from noPowers
        // player must select another god power card
        this.availableCards.removeIf(c -> c.equals(card) || c.equals(Card.getNoPowers()));

        this.selectionRemainingCount--;
        this.selectedCards.add(card);
    }

    /**
     * Updates the deck removing the card that has been selected by a player
     * @param card card selected by a player
     */
    public void cardAssigned(Card card) {
        this.selectedCards.removeIf(c -> c.equals(card));
    }

    /**
     * Returns the last card remained in the deck that will be assigned to the first player
     * @return last selected card
     */
    public Card getRemainingCard() {
        return this.selectedCards.remove(0);
    }

    /**
     * Count of the cards that the first player has still to select
     * @return selection remaining count
     */
    public int getSelectionRemainingCount() {
        return this.selectionRemainingCount;
    }

    /**
     * Count of cards that the players can select
     * @return assignment remaining count
     */
    public int getAssignmentRemainingCount() {
        return this.selectedCards.size();
    }
}
