package it.polimi.ingsw.psp12.model.power.cardPower;

import it.polimi.ingsw.psp12.model.power.ExtendedPowerDecorator;
import it.polimi.ingsw.psp12.model.power.Power;

public class PushAwayWorkerDecorator extends ExtendedPowerDecorator {

    public PushAwayWorkerDecorator(Power power) {
        this.power = power;
    }

}
