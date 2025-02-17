package it.polimi.ingsw.psp12.model.cards;

import it.polimi.ingsw.psp12.model.power.BasicPower;
import it.polimi.ingsw.psp12.model.power.Power;
import it.polimi.ingsw.psp12.model.power.cardPower.BuildAgainDecorator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class CardTest {
    @Test
    public void noPowersCard_ShouldReturnBasicPower() {
        Card card = Card.getNoPowers();
        assertEquals("No cards", card.getName());

        Power power = card.getPower();
        assertTrue(power instanceof BasicPower);
    }

    @Test
    public void loadedCard_ShouldReturnDecoratedPower() {
        int id = 0;
        String name = "DemoCard";
        String shortDesc = "Short description";
        String desc = "Full description";
        List<Integer> powers = new ArrayList<>();
        String image = "src/main/resources/cards/NoPower.png";
        powers.add(4);

        Card card = new Card(name, shortDesc, desc, powers, image);

        assertEquals(name, card.getName());
        assertEquals(shortDesc, card.getShortDescription());
        assertEquals(desc, card.getDescription());

        Power power = card.getPower();
        assertTrue(power instanceof BuildAgainDecorator);

        assertEquals(card.getImage(),"src/main/resources/cards/NoPower.png");
    }
}
