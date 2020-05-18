package it.polimi.ingsw.psp12.model.power;

import it.polimi.ingsw.psp12.model.board.Board;
import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.enumeration.Action;
import it.polimi.ingsw.psp12.model.Worker;
import it.polimi.ingsw.psp12.model.enumeration.TurnState;

import java.util.List;

/**
 * Abstract decorator that will be used as interface implemented by each decorator
 * @author Michele Lucio
 */
public abstract class ExtendedPowerDecorator extends Power{

    protected Power power;

    @Override
    public void reset() {
        power.reset();
    }

    @Override
    public List<Action> nextActions(TurnState turnState) {
        return power.nextActions(turnState);
    }

    @Override
    public boolean checkVictory() {
        return power.checkVictory();
    }

    @Override
    public List<Cell> getPossibleMoves(Board b, Worker w) {
        return power.getPossibleMoves(b, w);
    }

    @Override
    public List<Cell> getPossibleBuilds(Board b, Worker w) {
        return power.getPossibleBuilds(b, w);
    }
}
