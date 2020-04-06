package it.polimi.ingsw.psp12.model.power.cardPower;

import it.polimi.ingsw.psp12.model.power.ExtendedPowerDecorator;
import it.polimi.ingsw.psp12.model.power.Power;

public class BuildBeforeMoveAgainDecorator extends ExtendedPowerDecorator {

    public BuildBeforeMoveAgainDecorator(Power power) {
        this.power = power;
    }

}
