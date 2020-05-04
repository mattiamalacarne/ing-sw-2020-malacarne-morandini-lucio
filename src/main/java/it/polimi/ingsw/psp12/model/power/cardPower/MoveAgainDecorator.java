package it.polimi.ingsw.psp12.model.power.cardPower;

import it.polimi.ingsw.psp12.model.power.ExtendedPowerDecorator;
import it.polimi.ingsw.psp12.model.power.Power;

public class MoveAgainDecorator extends ExtendedPowerDecorator {

    public MoveAgainDecorator(Power power) {
        this.power = power;
    }

    /*@Override
    public int getMovesCount() {
        //It's possible to do a second move action
        return power.getMovesCount() + 1;
    }*/

}
