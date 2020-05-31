package it.polimi.ingsw.psp12.model.power.cardPower;

import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.power.BasicPower;
import it.polimi.ingsw.psp12.model.power.Power;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class JumpToWinDecoratorTest {

    Power power = null;

    @Before
    public void setUp() {
        power = new JumpToWinDecorator( new BasicPower() );
    }

    @Test
    public void checkVictory_WithoutMove_ShouldReturnFalse(){

        Cell start = new Cell(0,0);
        power.setInitialPosition(start);

        assertFalse(power.checkVictory());
    }

    @Test
    public void checkVictory_MoveUp1Level_Basic_ShouldReturnTrue(){

        Cell start = new Cell(0,0);
        Cell end = new Cell(0,1);

        start.getTower().incrementLevel();
        start.getTower().incrementLevel();

        end.getTower().incrementLevel();
        end.getTower().incrementLevel();
        end.getTower().incrementLevel();

        power.setInitialPosition(start);
        power.moved(end);

        assertTrue(power.checkVictory());

    }

    @Test
    public void checkVictory_MoveUp1Level_ShouldReturnFalse(){

        Cell start = new Cell(0,0);
        Cell end = new Cell(0,1);

        end.getTower().incrementLevel();

        power.setInitialPosition(start);
        power.moved(end);

        assertFalse(power.checkVictory());

    }

    @Test
    public void checkVictory_MoveUp2Level_ShouldReturnFalse(){

        Cell start = new Cell(0,0);
        Cell end = new Cell(0,1);

        end.getTower().incrementLevel();
        end.getTower().incrementLevel();

        power.setInitialPosition(start);
        power.moved(end);

        assertFalse(power.checkVictory());

    }

    @Test
    public void checkVictory_MoveDown1Level_ShouldReturnFalse(){

        Cell start = new Cell(0,0);
        Cell end = new Cell(0,1);

        start.getTower().incrementLevel();

        power.setInitialPosition(start);
        power.moved(end);

        assertFalse(power.checkVictory());

    }

    @Test
    public void checkVictory_MoveDown2Level_ShouldReturnTrue(){

        Cell start = new Cell(0,0);
        Cell end = new Cell(0,1);

        start.getTower().incrementLevel();
        start.getTower().incrementLevel();

        power.setInitialPosition(start);
        power.moved(end);

        assertTrue(power.checkVictory());

    }

    @Test
    public void checkVictory_MoveDown3Level_ShouldReturnTrue(){

        Cell start = new Cell(0,0);
        Cell end = new Cell(0,1);

        start.getTower().incrementLevel();
        start.getTower().incrementLevel();
        start.getTower().incrementLevel();

        power.setInitialPosition(start);
        power.moved(end);

        assertTrue(power.checkVictory());

    }

}