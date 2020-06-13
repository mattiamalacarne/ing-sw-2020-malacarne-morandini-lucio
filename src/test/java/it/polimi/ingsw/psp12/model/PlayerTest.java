package it.polimi.ingsw.psp12.model;

import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.model.cards.Card;
import it.polimi.ingsw.psp12.model.cards.Deck;
import it.polimi.ingsw.psp12.model.power.BasicPower;
import it.polimi.ingsw.psp12.model.power.cardPower.MoveAgainDecorator;
import it.polimi.ingsw.psp12.model.power.cardPower.SwapWorkersDecorator;
import it.polimi.ingsw.psp12.utils.Color;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class PlayerTest {
    private Player player;

    @Before
    public void setUp() {
        player = new Player(0, "Test");
    }

    @Test
    public void equals_SamePlayer_ShouldReturnTrue() {
        Player p2 = new Player(0, "Test");

        assertTrue(player.equals(p2));
    }

    @Test
    public void equals_DifferentPlayers_ShouldReturnFalse() {
        Player p2 = new Player(1, "Player");

        assertFalse(player.equals(p2));
    }

    @Test
    public void initialize_ShouldInitializePlayer() {
        // check initial state
        assertEquals(0, player.getId());
        assertEquals("Test", player.getName());
        assertFalse(player.isInitialized());
        assertTrue(player.getPower() instanceof BasicPower);

        assertNull(player.getWorkerByIndex(0).getColor());
        assertNull(player.getWorkerByIndex(0).getPosition());

        assertNull(player.getWorkerByIndex(1).getColor());
        assertNull(player.getWorkerByIndex(1).getPosition());

        // initialize player
        Cell[] cells = new Cell[] { new Cell(1, 2), new Cell(3, 4) };
        player.initialize(Color.RED, cells);

        // check final state
        assertTrue(player.isInitialized());
        assertNotNull(player.getPower());

        assertEquals(Color.RED, player.getWorkerByIndex(0).getColor());
        assertEquals(cells[0].getLocation(), player.getWorkerByIndex(0).getPosition());
        assertEquals(player.getWorkerByIndex(0), cells[0].getWorker());

        assertEquals(Color.RED, player.getWorkerByIndex(1).getColor());
        assertEquals(cells[1].getLocation(), player.getWorkerByIndex(1).getPosition());
        assertEquals(player.getWorkerByIndex(1), cells[1].getWorker());
    }

    @Test
    public void updateWorkerPosition_ShouldMoveWorker() {
        // init
        player.selectCurrentWorker(0);

        // check initial state
        assertNull(player.getWorkerByIndex(0).getPosition());
        assertNull(player.getWorkerByIndex(1).getPosition());

        // move
        Point p1 = new Point(0, 1);
        player.updateWorkerPosition(p1);

        // check intermediate state
        assertEquals(p1, player.getWorkerByIndex(0).getPosition());
        assertNull(player.getWorkerByIndex(1).getPosition());

        // move
        Point p2 = new Point(2, 3);
        Point old = player.updateWorkerPosition(p2);

        // check final state
        assertEquals(old, p1);
        assertEquals(p2, player.getWorkerByIndex(0).getPosition());
        assertNull(player.getWorkerByIndex(1).getPosition());
    }

    @Test
    public void getWorkers_ShouldReturnWorkersInCorrectOrder() {
        Cell[] cells = new Cell[] { new Cell(1, 2), new Cell(3, 4) };
        player.initialize(Color.RED, cells);

        List<Worker> workers = player.getWorkers();

        assertEquals(2, workers.size());
        assertNotEquals(player.getWorkerByIndex(0), workers.get(0));
        assertEquals(player.getWorkerByIndex(0).getPosition(), workers.get(0).getPosition());
        assertNotEquals(player.getWorkerByIndex(1), workers.get(1));
        assertEquals(player.getWorkerByIndex(1).getPosition(), workers.get(1).getPosition());
    }

    @Test
    public void assignPower_ShouldSetCardAndPower() {
        Player player = new Player(0, "P1");
        Deck deck = new Deck(2);

        assertTrue(player.getPower() instanceof BasicPower);
        assertNull(player.getCard());

        Card card = deck.getAvailableCards().get(2);
        player.setPower(card);

        assertEquals(card, player.getCard());
        assertTrue(player.getPower() instanceof MoveAgainDecorator);
    }
}
