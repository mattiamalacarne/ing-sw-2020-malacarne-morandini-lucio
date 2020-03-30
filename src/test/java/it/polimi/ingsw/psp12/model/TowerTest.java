package it.polimi.ingsw.psp12.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class TowerTest {
    @Test
    public void hasDome_NoDome_ShouldReturnFalse() {
        Tower t1 = new Tower();
        assertFalse(t1.hasDome());
    }

    @Test
    public void hasDome_WithDome_ShouldReturnTrue() {
        Tower t1 = new Tower();
        t1.buildDome();
        assertTrue(t1.hasDome());
    }

    @Test
    public void incrementLevel_levelLt3_ShouldIncrement() {
        Tower t1 = new Tower();
        assertEquals(0, t1.getLevel());

        t1.incrementLevel();
        assertEquals(1, t1.getLevel());
    }

    @Test
    public void incrementLevel_levelEq3_ShouldNotIncrement() {
        Tower t1 = new Tower();
        t1.incrementLevel();
        t1.incrementLevel();
        t1.incrementLevel();
        assertEquals(3, t1.getLevel());

        t1.incrementLevel();
        assertEquals(3, t1.getLevel());
    }
}
