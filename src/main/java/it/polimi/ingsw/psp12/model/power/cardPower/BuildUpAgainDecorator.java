package it.polimi.ingsw.psp12.model.power.cardPower;

import it.polimi.ingsw.psp12.model.power.ExtendedPowerDecorator;
import it.polimi.ingsw.psp12.model.power.Power;

public class BuildUpAgainDecorator extends ExtendedPowerDecorator {

    public BuildUpAgainDecorator(Power power) {
        this.power = power;
    }

}
