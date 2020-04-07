package it.polimi.ingsw.psp12.model.power.cardPower;

import it.polimi.ingsw.psp12.model.power.ExtendedPowerDecorator;
import it.polimi.ingsw.psp12.model.power.Power;

public class NextCannotMoveUpDecorator extends ExtendedPowerDecorator {

    public NextCannotMoveUpDecorator(Power power) {
        this.power = power;
    }

    @Override
    public int getNextPlayerMaxClimb() {
        //FIXME: next or this player ?
        return 0;
    }

}
