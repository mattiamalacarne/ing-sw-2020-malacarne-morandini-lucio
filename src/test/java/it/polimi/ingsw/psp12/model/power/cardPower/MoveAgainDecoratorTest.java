package it.polimi.ingsw.psp12.model.power.cardPower;

import it.polimi.ingsw.psp12.model.power.BasicPower;
import it.polimi.ingsw.psp12.model.power.Power;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MoveAgainDecoratorTest {

    Power basicPowerDecorated = null;

    @Before
    public void setUp(){
        BasicPower basicPower = new BasicPower();
        basicPowerDecorated = new MoveAgainDecorator(basicPower);
    }

    @Test
    public void getMovesCount() {
        assertEquals(2, basicPowerDecorated.getMovesCount());
    }
}