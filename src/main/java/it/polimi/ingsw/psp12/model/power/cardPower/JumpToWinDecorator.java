package it.polimi.ingsw.psp12.model.power.cardPower;

import it.polimi.ingsw.psp12.model.power.ExtendedPowerDecorator;
import it.polimi.ingsw.psp12.model.power.Power;

public class JumpToWinDecorator extends ExtendedPowerDecorator {

    public JumpToWinDecorator(Power power) {
        this.power = power;
    }

    @Override
    public boolean checkVictory() {
        //FIXME: controllare condizione particolare di vittoria
        return power.checkVictory();
    }

}
