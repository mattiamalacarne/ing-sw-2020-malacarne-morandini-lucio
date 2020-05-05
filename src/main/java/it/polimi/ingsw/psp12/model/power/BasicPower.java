package it.polimi.ingsw.psp12.model.power;

import it.polimi.ingsw.psp12.model.Worker;
import it.polimi.ingsw.psp12.model.board.Board;
import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.enumeration.Action;
import it.polimi.ingsw.psp12.model.enumeration.TurnState;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic implementation of power class
 * @author Michele Lucio
 */
public class BasicPower extends Power {

    /**
     * Constructor
     */
    public BasicPower() {
        reset();
    }

    /**
     * Reset all Power's attributes to basic power condition
     */
    @Override
    public void reset() {

        powerId = 0;
        maxClimbLevel = 1;
        //maxMoves = 1;
        maxBuildsLevel = 1;
        minDomeLevel = 4;
        movesCount = 0;
        buildsCount = 0;

    }

    @Override
    public List<Action> nextActions(TurnState turnState) {
        List<Action> actions = new ArrayList<>();

        switch (turnState) {
            case INIT:
                actions.add(Action.MOVE);
                break;
            case MOVE:
                actions.add(Action.BUILD);
                break;
        }

        return actions;
    }

    @Override
    public boolean checkVictory() {
        return super.checkVictory();
    }

    @Override
    public List<Cell> getPossibleMoves(Board b, Worker w) {
        return super.getPossibleMoves(b, w);
    }

    @Override
    public List<Cell> getPossibleBuilds(Board b, Worker w) {
        return super.getPossibleBuilds(b, w);
    }
}
