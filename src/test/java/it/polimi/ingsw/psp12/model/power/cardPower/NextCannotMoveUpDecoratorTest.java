package it.polimi.ingsw.psp12.model.power.cardPower;

import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.power.BasicPower;
import it.polimi.ingsw.psp12.model.power.Power;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NextCannotMoveUpDecoratorTest {
    Power power = null;

    @Before
    public void setUp() {
        power = new NextCannotMoveUpDecorator(new BasicPower());
    }

    @Test
    public void getNextPlayerMaxClimb_notInitialized_ShouldReturn1() {
        assertEquals(1, power.getNextPlayerMaxClimb());
    }

    @Test
    public void getNextPlayerMaxClimb_sameLevel_ShouldReturn1() {
        Cell start = new Cell(0, 0);
        Cell end = new Cell(0, 1);

        // initialize moves history
        power.setInitialPosition(start);
        // simulate move
        power.moved(end);

        assertEquals(0, start.getTower().getLevel());
        assertEquals(0, end.getTower().getLevel());
        assertEquals(1, power.getNextPlayerMaxClimb());
    }

    @Test
    public void getNextPlayerMaxClimb_movedDown_ShouldReturn1() {
        Cell start = new Cell(0, 0);
        start.getTower().incrementLevel();
        Cell end = new Cell(0, 1);

        // initialize moves history
        power.setInitialPosition(start);
        // simulate move
        power.moved(end);

        assertEquals(1, start.getTower().getLevel());
        assertEquals(0, end.getTower().getLevel());
        assertEquals(1, power.getNextPlayerMaxClimb());
    }

    @Test
    public void movedUp_getNextPlayerMaxClimbShouldReturn0() {
        Cell start = new Cell(0, 0);
        Cell end = new Cell(0, 1);
        end.getTower().incrementLevel();

        // initialize moves history
        power.setInitialPosition(start);
        // simulate move
        power.moved(end);

        assertEquals(0, start.getTower().getLevel());
        assertEquals(1, end.getTower().getLevel());
        assertEquals(0, power.getNextPlayerMaxClimb());
    }
}
