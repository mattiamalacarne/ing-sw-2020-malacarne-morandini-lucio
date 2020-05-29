package it.polimi.ingsw.psp12.model.board;

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

    @Test
    public void restoreSimulation_ShouldResetTower() {
        Tower tower = new Tower();
        tower.incrementLevel();

        // check initial state
        assertEquals(1, tower.getLevel());
        assertFalse(tower.hasDome());

        // save tower snapshot
        Tower snap = new Tower();
        snap.setLevel(tower.getLevel());
        snap.setDome(tower.hasDome());

        // update tower
        tower.buildDome();
        tower.incrementLevel();

        // check state
        assertEquals(2, tower.getLevel());
        assertTrue(tower.hasDome());
        assertEquals(1, snap.getLevel());
        assertFalse(snap.hasDome());

        // restore tower snapshot
        tower.setLevel(snap.getLevel());
        tower.setDome(snap.hasDome());

        // check final state
        assertEquals(1, tower.getLevel());
        assertFalse(tower.hasDome());
    }
}
