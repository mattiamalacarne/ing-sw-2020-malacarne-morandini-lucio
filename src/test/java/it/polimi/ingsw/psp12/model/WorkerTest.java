package it.polimi.ingsw.psp12.model;

import it.polimi.ingsw.psp12.model.board.Point;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class WorkerTest {
    Worker w1;

    @Before
    public void setUp() {
        w1 = new Worker();
        w1.move(new Point(0, 2));
    }

    @Test
    public void Worker_WithName_ShouldInitializeName() {
        Worker worker1 = new Worker("P1", 0);
        Worker worker2 = new Worker("P1", 1);

        assertEquals("P1.0", worker1.getName());
        assertEquals("P1.1", worker2.getName());
    }

    @Test
    public void getPosition_ShouldReturnCurrentCell() {
        Point c1 = w1.getPosition();

        assertNotNull(c1);
        assertEquals(0, c1.getX());
        assertEquals(2, c1.getY());
    }

    @Test
    public void move_ShouldUpdateCurrentCell() {
        Point newPoint = new Point(3, 1);
        // check initial state
        assertEquals(0, w1.getPosition().getX());
        assertEquals(2, w1.getPosition().getY());

        w1.move(newPoint);

        // check final state
        assertEquals(3, w1.getPosition().getX());
        assertEquals(1, w1.getPosition().getY());
    }
}
