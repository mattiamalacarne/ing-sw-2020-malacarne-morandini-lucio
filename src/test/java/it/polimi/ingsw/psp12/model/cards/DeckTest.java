package it.polimi.ingsw.psp12.model.cards;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class DeckTest {
    private Deck deck2, deck3;

    @Before
    public void setUp() throws IOException {
        Deck.loadCards();

        deck2 = new Deck(2);
        deck3 = new Deck(3);
    }

    @Test
    public void getAvailableCards_TwoPlayers_ShouldIncludeNoPowersCard() {
        List<Card> cards = deck2.getAvailableCards();

        assertTrue(cards.contains(Card.getNoPowers()));
    }

    @Test
    public void getAvailableCards_ThreePlayers_ShouldNotIncludeNoPowersCard() {
        List<Card> cards = deck3.getAvailableCards();

        assertFalse(cards.contains(Card.getNoPowers()));
    }

    @Test
    public void cardSelected_NoPowersCard_ShouldReturnNoPowersCard() {
        List<Card> initialCards = deck2.getAvailableCards();
        Card selected = Card.getNoPowers();

        assertTrue(initialCards.contains(selected));
        assertEquals(Card.getNoPowers(), selected);

        deck2.cardSelected(selected);

        List<Card> finalCards = deck2.getAvailableCards();

        assertEquals(1, finalCards.size());
        assertEquals(Card.getNoPowers(), finalCards.get(0));
    }

    @Test
    public void cardSelected_TwoPlayersGodCard_ShouldReturnOthersCard() {
        // TODO: remove comment when Deck.loadCards() is implemented
        /*List<Card> initialCards = deck2.getAvailableCards();
        Card selected = deck2.getAvailableCards().get(1);

        assertTrue(initialCards.contains(selected));
        assertNotEquals(Card.getNoPowers(), selected);

        deck2.cardSelected(selected);

        List<Card> finalCards = deck2.getAvailableCards();

        assertEquals(initialCards.size()-1, finalCards.size());
        assertFalse(finalCards.contains(selected));
        assertFalse(finalCards.contains(Card.getNoPowers()));*/
    }

    @Test
    public void cardSelected_ThreePlayersGodCard_ShouldReturnOthersCard() {
        // TODO: remove comment when Deck.loadCards() is implemented
        /*List<Card> initialCards = deck3.getAvailableCards();
        Card selected = deck3.getAvailableCards().get(1);

        assertTrue(initialCards.contains(selected));
        assertNotEquals(Card.getNoPowers(), selected);

        deck3.cardSelected(selected);

        List<Card> finalCards = deck3.getAvailableCards();

        assertEquals(initialCards.size()-1, finalCards.size());
        assertFalse(finalCards.contains(selected));
        assertFalse(finalCards.contains(Card.getNoPowers()));*/
    }
}
