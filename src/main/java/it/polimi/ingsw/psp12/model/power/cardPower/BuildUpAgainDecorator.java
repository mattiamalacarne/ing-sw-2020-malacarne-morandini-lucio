package it.polimi.ingsw.psp12.model.power.cardPower;

import it.polimi.ingsw.psp12.model.Worker;
import it.polimi.ingsw.psp12.model.board.Board;
import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.enumeration.Action;
import it.polimi.ingsw.psp12.model.enumeration.TurnState;
import it.polimi.ingsw.psp12.model.power.ExtendedPowerDecorator;
import it.polimi.ingsw.psp12.model.power.Power;

import java.util.ArrayList;
import java.util.List;

/**
 * Your worker may build one additional block (not dome),
 * on top of your block
 * @author Luca Morandini
 */
public class BuildUpAgainDecorator extends ExtendedPowerDecorator {

    public BuildUpAgainDecorator(Power power) {
        this.power = power;
    }

    @Override
    public List<Action> nextActions(TurnState turnState, Board b) {
        List<Action> actions = super.nextActions(turnState, b);

        // build again after the second build
        // only if the tower of the first build has level lower than 3
        if (turnState.equals(TurnState.BUILD) &&
                getBuildsCount() == 1 &&
                getLastBuilds()[0].getTower().getLevel() < 3) {
            actions.add(Action.BUILD);
        }

        return actions;
    }

    @Override
    public List<Cell> getPossibleBuilds(Board b, Worker w) {
        // in the second build return only the cell of the first build
        if (getBuildsCount() == 1) {
            List<Cell> cells = new ArrayList<>();
            cells.add(getLastBuilds()[0].clone());
            return cells;
        }

        return super.getPossibleBuilds(b, w);
    }
}
