package it.polimi.ingsw.psp12.model;

import it.polimi.ingsw.psp12.model.board.Cell;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WorkerTest {
    Worker w1;

    @Before
    public void setUp() {
        w1 = new Worker();
        w1.move(new Cell(0, 2));
    }

    @Test
    public void getPosition_ShouldReturnCurrentCell() {
        Cell c1 = w1.getPosition();

        assertNotNull(c1);
        assertEquals(0, c1.getLocation().getX());
        assertEquals(2, c1.getLocation().getY());
    }

    @Test
    public void move_ShouldUpdateCurrentCell() {
        Cell newCell = new Cell(3, 1);
        // check initial state
        assertEquals(0, w1.getPosition().getLocation().getX());
        assertEquals(2, w1.getPosition().getLocation().getY());

        w1.move(newCell);

        // check final state
        assertEquals(3, w1.getPosition().getLocation().getX());
        assertEquals(1, w1.getPosition().getLocation().getY());
    }
}
