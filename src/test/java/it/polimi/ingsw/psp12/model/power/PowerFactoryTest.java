package it.polimi.ingsw.psp12.model.power;

import it.polimi.ingsw.psp12.model.power.cardPower.*;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class PowerFactoryTest {
    @Test
    public void decorate_InvalidId_ShouldNotDecorate() {
        Power power = new BasicPower();

        power = PowerFactory.decorate(power, -1);
        assertTrue(power instanceof BasicPower);
    }

    @Test
    public void decorate_ValidId_ShouldDecorate() {
        Power power = new BasicPower();

        power = PowerFactory.decorate(power, 0);
        assertTrue(power instanceof SwapWorkersDecorator);

        ExtendedPowerDecorator p1 = (ExtendedPowerDecorator)power;
        assertTrue(p1.power instanceof BasicPower);
    }

    @Test
    public void decorate_MultiDecorate_ShouldDecorate() {
        Power power = new BasicPower();

        power = PowerFactory.decorate(power, 2);
        assertTrue(power instanceof NextCannotMoveUpDecorator);

        power = PowerFactory.decorate(power, 3);
        assertTrue(power instanceof DomeAnyLevelDecorator);

        ExtendedPowerDecorator p1 = (ExtendedPowerDecorator)power;
        assertTrue(p1.power instanceof NextCannotMoveUpDecorator);

        ExtendedPowerDecorator p2 = (NextCannotMoveUpDecorator)p1.power;
        assertTrue(p2.power instanceof BasicPower);
    }

    @Test
    public void decorate_0_SwapWorkersDecorator() {
        Power power = new BasicPower();

        power = PowerFactory.decorate(power, 0);
        assertTrue(power instanceof SwapWorkersDecorator);
    }

    @Test
    public void decorate_1_MoveAgainDecorator() {
        Power power = new BasicPower();

        power = PowerFactory.decorate(power, 1);
        assertTrue(power instanceof MoveAgainDecorator);
    }

    @Test
    public void decorate_2_NextCannotMoveUpDecorator() {
        Power power = new BasicPower();

        power = PowerFactory.decorate(power, 2);
        assertTrue(power instanceof NextCannotMoveUpDecorator);
    }

    @Test
    public void decorate_3_DomeAnyLevelDecorator() {
        Power power = new BasicPower();

        power = PowerFactory.decorate(power, 3);
        assertTrue(power instanceof DomeAnyLevelDecorator);
    }

    @Test
    public void decorate_4_BuildAgainDecorator() {
        Power power = new BasicPower();

        power = PowerFactory.decorate(power, 4);
        assertTrue(power instanceof BuildAgainDecorator);
    }

    @Test
    public void decorate_5_BuildUpAgainDecorator() {
        Power power = new BasicPower();

        power = PowerFactory.decorate(power, 5);
        assertTrue(power instanceof BuildUpAgainDecorator);
    }

    @Test
    public void decorate_6_PushAwayWorkerDecorator() {
        Power power = new BasicPower();

        power = PowerFactory.decorate(power, 6);
        assertTrue(power instanceof PushAwayWorkerDecorator);
    }

    @Test
    public void decorate_7_JumpToWinDecorator() {
        Power power = new BasicPower();

        power = PowerFactory.decorate(power, 7);
        assertTrue(power instanceof JumpToWinDecorator);
    }

    @Test
    public void decorate_8_BuildBeforeMoveAgainDecorator() {
        Power power = new BasicPower();

        power = PowerFactory.decorate(power, 8);
        assertTrue(power instanceof BuildBeforeMoveAgainDecorator);
    }
}
