package it.polimi.ingsw.psp12.model.power.cardPower;

import it.polimi.ingsw.psp12.model.power.ExtendedPowerDecorator;
import it.polimi.ingsw.psp12.model.power.Power;

public class DomeAnyLevelDecorator extends ExtendedPowerDecorator {

    public DomeAnyLevelDecorator(Power power) {
        this.power = power;
    }

    @Override
    public int getMinDomeLevel() {
        //It's possible to build a dome at any level
        return 0;
    }
}
