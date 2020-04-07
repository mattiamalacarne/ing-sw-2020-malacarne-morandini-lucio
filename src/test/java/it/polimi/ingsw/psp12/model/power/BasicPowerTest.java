package it.polimi.ingsw.psp12.model.power;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BasicPowerTest {

    BasicPower basicPower = null;

    @Before
    public void setUp(){
        basicPower = new BasicPower();
    }

    @Test
    public void getPowerId() {
        assertEquals(0, basicPower.getPowerId());
    }

    @Test
    public void getMaxClimbLevel() {
        assertEquals(1, basicPower.getMaxClimbLevel());
    }

    @Test
    public void getMaxMoves() {
        assertEquals(1, basicPower.getMaxMoves());
    }

    @Test
    public void getMaxBuildsLevel() {
        assertEquals(1, basicPower.getMaxBuildsLevel());
    }

    @Test
    public void getMinDomeLevel() {
        assertEquals(4, basicPower.getMinDomeLevel());
    }

    @Test
    public void getMovesCount() {
        assertEquals(1, basicPower.getMovesCount());
    }

    @Test
    public void getBuildsCount() {
        assertEquals(1, basicPower.getBuildsCount());
    }

    @Test
    public void getNextPlayerMaxClimb() {
        assertEquals(1, basicPower.getNextPlayerMaxClimb());
    }

    @Test
    public void reset() {
        assertEquals(0, basicPower.getPowerId());
        assertEquals(1, basicPower.getMaxClimbLevel());
        assertEquals(1, basicPower.getMaxMoves());
        assertEquals(1, basicPower.getMaxBuildsLevel());
        assertEquals(4, basicPower.getMinDomeLevel());
        assertEquals(1, basicPower.getMovesCount());
        assertEquals(1, basicPower.getBuildsCount());
        assertEquals(1, basicPower.getNextPlayerMaxClimb());

    }
}