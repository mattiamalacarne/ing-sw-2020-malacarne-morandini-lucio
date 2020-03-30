package it.polimi.ingsw.psp12.model;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CellTest {
    @Test
    public void canMoveOn_00_ShouldReturnTrue() {
        Cell c1 = new Cell();
        assertTrue(c1.canMoveOn(0));
    }

    @Test
    public void canMoveOn_11_ShouldReturnTrue() {
        Cell c1 = new Cell();
        c1.getTower().incrementLevel();
        assertTrue(c1.canMoveOn(1));
    }

    @Test
    public void canMoveOn_01_ShouldReturnTrue() {
        Cell c1 = new Cell();
        c1.getTower().incrementLevel();
        assertTrue(c1.canMoveOn(0));
    }

    @Test
    public void canMoveOn_02_ShouldReturnFalse() {
        Cell c1 = new Cell();
        c1.getTower().incrementLevel();
        c1.getTower().incrementLevel();
        assertFalse(c1.canMoveOn(0));
    }

    @Test
    public void canMoveOn_13_ShouldReturnFalse() {
        Cell c1 = new Cell();
        c1.getTower().incrementLevel();
        c1.getTower().incrementLevel();
        c1.getTower().incrementLevel();
        assertFalse(c1.canMoveOn(1));
    }
}
