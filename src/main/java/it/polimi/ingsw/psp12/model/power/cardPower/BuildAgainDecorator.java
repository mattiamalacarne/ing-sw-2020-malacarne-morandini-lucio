package it.polimi.ingsw.psp12.model.power.cardPower;

import it.polimi.ingsw.psp12.model.power.ExtendedPowerDecorator;
import it.polimi.ingsw.psp12.model.power.Power;

public class BuildAgainDecorator extends ExtendedPowerDecorator{

    public BuildAgainDecorator(Power power) {
        this.power = power;
    }

    /*@Override
    public int getBuildsCount() {
        //It's possible to do a second build action
        return power.getBuildsCount() + 1;
    }*/

}
