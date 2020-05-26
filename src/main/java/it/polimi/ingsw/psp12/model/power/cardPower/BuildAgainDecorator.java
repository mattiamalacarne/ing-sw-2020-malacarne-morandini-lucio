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
import java.util.stream.Collectors;

/**
 * Your Worker may build one additional time, but not
 * on the same space.
 *
 * @author Michele Lucio
 */
public class BuildAgainDecorator extends ExtendedPowerDecorator {

    public BuildAgainDecorator(Power power) {
        this.power = power;
    }

    @Override
    public List<Action> nextActions(TurnState turnState, Board b) {

        List<Action> actions = new ArrayList<>();

        switch (turnState) {
            case INIT:
                actions.add(Action.MOVE);
                break;
            case MOVE:
                actions.add(Action.BUILD);
                break;
            case BUILD:
                if (getBuildsCount() == 1) {
                    actions.add(Action.BUILD);
                }
                actions.add(Action.END);
                break;
        }

        return actions;

    }

    @Override
    public List<Cell> getPossibleBuilds(Board b, Worker w) {

        List<Cell> possibleBuilds = super.getPossibleBuilds(b, w);

        if (getBuildsCount() == 1) {

            possibleBuilds = possibleBuilds.stream()
                    .filter(c -> !c.equals(getLastBuilds()[0]))
                    .collect(Collectors.toList());
        }
        return possibleBuilds;
    }

}
