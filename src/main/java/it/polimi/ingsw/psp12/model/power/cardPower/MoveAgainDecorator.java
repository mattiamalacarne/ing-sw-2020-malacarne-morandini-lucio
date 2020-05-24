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
 * Your Worker may move one additional time, but not
 * back to its initial space.
 *
 * @author Michele Lucio
 */
public class MoveAgainDecorator extends ExtendedPowerDecorator {

    public MoveAgainDecorator(Power power) {
        this.power = power;
    }

    @Override
    public List<Action> nextActions(TurnState turnState) {

        List<Action> actions = new ArrayList<>();

        switch (turnState) {
            case INIT:
                actions.add(Action.MOVE);
                break;
            case MOVE:
                if (getMovesCount() == 1) {
                    actions.add(Action.MOVE);
                }
                actions.add(Action.BUILD);
                break;
            case BUILD:
                actions.add(Action.END);
                break;
        }

        return actions;

    }

    @Override
    public List<Cell> getPossibleMoves(Board b, Worker w) {

        List<Cell> possibleMoves = super.getPossibleMoves(b, w);

        if (getMovesCount() == 1) {

            possibleMoves = possibleMoves.stream()
                    .filter(c -> !c.equals(getLastPositions()[0]))
                    .collect(Collectors.toList());
        }
        return possibleMoves;
    }

}
