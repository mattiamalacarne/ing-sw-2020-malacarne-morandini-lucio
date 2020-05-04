package it.polimi.ingsw.psp12.model;

import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.utils.Color;
import org.junit.Before;
import org.junit.Test;

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

        assertNull(player.getWorker(0).getColor());
        assertNull(player.getWorker(0).getPosition());

        assertNull(player.getWorker(1).getColor());
        assertNull(player.getWorker(1).getPosition());

        // initialize player
        Cell cells[] = new Cell[] { new Cell(1, 2), new Cell(3, 4) };
        player.initialize(Color.RED, cells);

        // check final state
        assertTrue(player.isInitialized());

        assertEquals(Color.RED, player.getWorker(0).getColor());
        assertEquals(cells[0], player.getWorker(0).getPosition());
        assertEquals(player.getWorker(0), cells[0].getWorker());

        assertEquals(Color.RED, player.getWorker(1).getColor());
        assertEquals(cells[1], player.getWorker(1).getPosition());
        assertEquals(player.getWorker(1), cells[1].getWorker());
    }

    @Test
    public void updateWorkerPosition_ShouldMoveWorker() {
        // check initial state
        assertNull(player.getWorker(0).getPosition());
        assertNull(player.getWorker(1).getPosition());

        // move
        Cell c1 = new Cell(0, 1);
        player.updateWorkerPosition(0, c1);

        // check final state
        assertEquals(c1, player.getWorker(0).getPosition());
        assertNull(player.getWorker(1).getPosition());
    }
}
