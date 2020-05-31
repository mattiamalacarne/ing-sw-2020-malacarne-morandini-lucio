package it.polimi.ingsw.psp12.model.power.cardPower;

import it.polimi.ingsw.psp12.model.power.ExtendedPowerDecorator;
import it.polimi.ingsw.psp12.model.power.Power;

/**
 * You also win if your Worker moves down two or more levels.
 *
 * @author Michele Lucio
 */
public class JumpToWinDecorator extends ExtendedPowerDecorator {

    public JumpToWinDecorator(Power power) {
        this.power = power;
    }

    @Override
    public boolean checkVictory() {

        //Check if two last positions are available
        if (getLastPositions()[0] == null || getLastPositions()[1] == null) {
            return false;
        }

        //Difference from current level and previous level
        int levelDifference = getLastPositions()[0].getTower().getLevel() - getLastPositions()[1].getTower().getLevel();

        //True if player moves down 2 or more levels or with the basic win condition
        return (getLastPositions()[0].getTower().getLevel() <= getLastPositions()[1].getTower().getLevel() - 2  ||
                getLastPositions()[0].getTower().getLevel() == 3 && levelDifference >= 1);

    }

}
