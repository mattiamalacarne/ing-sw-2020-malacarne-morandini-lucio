package it.polimi.ingsw.psp12.model.power.cardPower;

import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.power.ExtendedPowerDecorator;
import it.polimi.ingsw.psp12.model.power.Power;

/**
 * If one of your workers moved up on your last turn,
 * opponent workers cannot move up this turn
 * @author Luca Morandini
 */
public class NextCannotMoveUpDecorator extends ExtendedPowerDecorator {

    public NextCannotMoveUpDecorator(Power power) {
        this.power = power;
    }

    @Override
    public int getNextPlayerMaxClimb() {
        Cell[] lastPositions = getLastPositions();

        // check if two last positions are available
        if (lastPositions[0] == null || lastPositions[1] == null) {
            return super.getNextPlayerMaxClimb();
        }

        // calculate difference from the two last positions
        int diff = lastPositions[0].getTower().getLevel() - lastPositions[1].getTower().getLevel();

        // if current player moved up, other players can not move up (max climb is 0)
        return (diff >= 1) ? 0 : 1;
    }
}
