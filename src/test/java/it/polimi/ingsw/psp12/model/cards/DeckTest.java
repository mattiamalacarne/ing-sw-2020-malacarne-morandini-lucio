package it.polimi.ingsw.psp12.model.cards;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class DeckTest {
    private Deck deck2, deck3;

    @BeforeClass
    public static void init() throws IOException {
        Deck.loadCards();
    }

    @Before
    public void setUp() {
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
    public void cardSelected_TwoPlayersGodCard_ShouldReturnOthersCard() {
        List<Card> initialCards = deck2.getAvailableCards();
        Card selected = deck2.getAvailableCards().get(1);

        assertTrue(initialCards.contains(selected));
        assertNotEquals(Card.getNoPowers(), selected);
        assertEquals(2, deck2.getSelectionRemainingCount());
        assertEquals(0, deck2.getAssignmentRemainingCount());

        deck2.cardSelected(selected);

        List<Card> finalCards = deck2.getAvailableCards();

        assertEquals(initialCards.size()-2, finalCards.size());
        assertFalse(finalCards.contains(selected));
        assertFalse(finalCards.contains(Card.getNoPowers()));
        assertEquals(1, deck2.getSelectionRemainingCount());
        assertEquals(1, deck2.getAssignmentRemainingCount());
    }

    @Test
    public void cardSelected_ThreePlayersGodCard_ShouldReturnOthersCard() {
        List<Card> initialCards = deck3.getAvailableCards();
        Card selected = deck3.getAvailableCards().get(1);

        assertTrue(initialCards.contains(selected));
        assertNotEquals(Card.getNoPowers(), selected);
        assertEquals(3, deck3.getSelectionRemainingCount());
        assertEquals(0, deck3.getAssignmentRemainingCount());

        deck3.cardSelected(selected);

        List<Card> finalCards = deck3.getAvailableCards();

        assertEquals(initialCards.size()-1, finalCards.size());
        assertFalse(finalCards.contains(selected));
        assertFalse(finalCards.contains(Card.getNoPowers()));
        assertEquals(2, deck3.getSelectionRemainingCount());
        assertEquals(1, deck3.getAssignmentRemainingCount());
    }

    @Test
    public void cardAssigned_ShouldRemoveCardFromSelectedCards() {
        List<Card> initialCards = deck2.getAvailableCards();
        Card firstAssigned = initialCards.get(2);
        Card secondAssigned = initialCards.get(4);

        // first player selected first card
        deck2.cardSelected(secondAssigned);

        // check state
        List<Card> selectedCards = deck2.getSelectedCards();

        assertEquals(1, deck2.getSelectionRemainingCount());
        assertEquals(1, deck2.getAssignmentRemainingCount());
        assertTrue(selectedCards.contains(secondAssigned));
        assertFalse(selectedCards.contains(firstAssigned));

        // first player selected second card
        deck2.cardSelected(firstAssigned);

        // check state
        selectedCards = deck2.getSelectedCards();

        assertEquals(0, deck2.getSelectionRemainingCount());
        assertEquals(2, deck2.getAssignmentRemainingCount());
        assertTrue(selectedCards.contains(secondAssigned));
        assertTrue(selectedCards.contains(firstAssigned));

        // second player selected the card
        deck2.cardAssigned(firstAssigned);

        // check state
        selectedCards = deck2.getSelectedCards();

        assertEquals(1, deck2.getAssignmentRemainingCount());
        assertTrue(selectedCards.contains(secondAssigned));
        assertFalse(selectedCards.contains(firstAssigned));

        // card setup completed
        Card remainingCard = deck2.getRemainingCard();

        selectedCards = deck2.getSelectedCards();

        assertEquals(0, deck2.getAssignmentRemainingCount());
        assertFalse(selectedCards.contains(secondAssigned));
        assertFalse(selectedCards.contains(firstAssigned));
        assertEquals(secondAssigned, remainingCard);
    }

    @Test
    public void LoadedCard_Test(){
        LoadedCard loadedCard = new LoadedCard(
                "God Card",
                "Short description test",
                "Description test",
                Arrays.asList(0)
        );

        assertEquals(loadedCard.getName(),"God Card");
        assertEquals(loadedCard.getShortDescription(),"Short description test");
        assertEquals(loadedCard.getDescription(),"Description test");
        assertEquals(loadedCard.getPowers(),Arrays.asList(0));
    }
}
