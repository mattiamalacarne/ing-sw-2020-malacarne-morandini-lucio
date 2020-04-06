package it.polimi.ingsw.psp12.model.power.cardPower;

import it.polimi.ingsw.psp12.model.power.ExtendedPowerDecorator;
import it.polimi.ingsw.psp12.model.power.Power;

public class SwapWorkersDecorator extends ExtendedPowerDecorator {

    public SwapWorkersDecorator(Power power) {
        this.power = power;
    }

}
