package it.polimi.ingsw.psp12.model.board;

import it.polimi.ingsw.psp12.model.Worker;

import org.junit.Test;

import static org.junit.Assert.*;

public class CellTest {
    @Test
    public void canMoveOn_From0To0_ShouldReturnTrue() {
        Cell c1 = new Cell(0, 0);
        assertTrue(c1.canMoveOn(0, 1));
    }

    @Test
    public void canMoveOn_From1To1_ShouldReturnTrue() {
        Cell c1 = new Cell(0, 0);
        c1.getTower().incrementLevel();
        assertTrue(c1.canMoveOn(1, 1));
    }

    @Test
    public void canMoveOn_From0To1_ShouldReturnTrue() {
        Cell c1 = new Cell(0, 0);
        c1.getTower().incrementLevel();
        assertTrue(c1.canMoveOn(0, 1));
    }

    @Test
    public void canMoveOn_From0To2_ShouldReturnFalse() {
        Cell c1 = new Cell(0, 0);
        c1.getTower().incrementLevel();
        c1.getTower().incrementLevel();
        assertFalse(c1.canMoveOn(0, 1));
    }

    @Test
    public void canMoveOn_From1To3_ShouldReturnFalse() {
        Cell c1 = new Cell(0, 0);
        c1.getTower().incrementLevel();
        c1.getTower().incrementLevel();
        c1.getTower().incrementLevel();
        assertFalse(c1.canMoveOn(1, 1));
    }

    @Test
    public void canMoveOn_From0To0_WithDome_ShouldReturnFalse() {
        Cell c1 = new Cell(0, 0);
        c1.getTower().buildDome();
        assertFalse(c1.canMoveOn(0, 1));
    }

    @Test
    public void canMoveOn_From0To0_MaxClimb0_ShouldReturnTrue() {
        Cell c1 = new Cell(0, 0);
        assertTrue(c1.canMoveOn(0, 0));
    }

    @Test
    public void canMoveOn_From1To1_MaxClimb0_ShouldReturnTrue() {
        Cell c1 = new Cell(0, 0);
        c1.getTower().incrementLevel();
        assertTrue(c1.canMoveOn(1, 0));
    }

    @Test
    public void canMoveOn_From1To0_MaxClimb0_ShouldReturnTrue() {
        Cell c1 = new Cell(0, 0);
        assertTrue(c1.canMoveOn(1, 0));
    }

    @Test
    public void canMoveOn_From0To1_MaxClimb0_ShouldReturnFalse() {
        Cell c1 = new Cell(0, 0);
        c1.getTower().incrementLevel();
        assertFalse(c1.canMoveOn(0, 0));
    }

    @Test
    public void canBuildOn_WithoutDome_ShouldReturnTrue() {
        Cell c1 = new Cell(0, 0);
        assertTrue(c1.canBuildOn());
    }

    @Test
    public void canBuildOn_WithDome_ShouldReturnFalse() {
        Cell c1 = new Cell(0, 0);
        c1.getTower().buildDome();
        assertFalse(c1.canBuildOn());
    }

    @Test
    public void equals_SameCells_ShouldReturnTrue() {
        Cell c1 = new Cell(2, 3);
        Cell c2 = new Cell(2, 3);
        assertTrue(c1.equals(c2));
    }

    @Test
    public void equals_DifferentCells_ShouldReturnFalse() {
        Cell c1 = new Cell(3, 4);
        Cell c2 = new Cell(0, 1);
        assertFalse(c1.equals(c2));
    }

    @Test
    public void getLocation_ValidCell_ShouldReturnTrue() {
        Cell c1 = new Cell(2, 4);
        Point p1 = new Point(2, 4);
        assertEquals(p1, c1.getLocation());
    }

    @Test
    public void getTower_ValidCell_ShouldReturnTower() {
        Cell c1 = new Cell(0, 0);
        assertEquals(0, c1.getTower().getLevel());
    }

    @Test
    public void hasWorker_WithoutWorker_ShouldReturnFalse() {
        Cell c1 = new Cell(3, 3);
        assertFalse(c1.hasWorker());
    }

    @Test
    public void hasWorker_WithWorker_ShouldReturnTrue() {
        Cell c1 = new Cell(4, 4);
        c1.addWorker(new Worker());
        assertTrue(c1.hasWorker());
    }

    @Test
    public void getWorker_WithoutWorker_ShouldReturnNull() {
        Cell c1 = new Cell(3, 3);
        assertNull(c1.getWorker());
    }

    @Test
    public void getWorker_WithWorker_ShouldReturnWorker() {
        Cell c1 = new Cell(2, 2);
        c1.addWorker(new Worker());
        assertNotNull(c1.getWorker());
    }

    @Test
    public void addWorker_ShouldAddWorker() {
        Cell c1 = new Cell(1, 1);
        Worker w1 = new Worker();
        w1.move(c1.getLocation());

        // check initial state
        assertFalse(c1.hasWorker());

        c1.addWorker(w1);

        // check final state
        assertTrue(c1.hasWorker());
        assertEquals(w1, c1.getWorker());
    }

    @Test
    public void removeWorker_ShouldRemoveAndReturnWorker() {
        Cell c1 = new Cell(1, 1);
        Worker w1 = new Worker();
        w1.move(c1.getLocation());
        c1.addWorker(w1);

        // check initial state
        assertTrue(c1.hasWorker());

        Worker w2 = c1.removeWorker();
        assertEquals(w1, w2);

        // check final state
        assertFalse(c1.hasWorker());
        assertNull(c1.getWorker());
    }
}
