package it.polimi.ingsw.psp12.model.power;

import it.polimi.ingsw.psp12.model.board.Board;
import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.model.enumeration.Action;
import it.polimi.ingsw.psp12.model.Worker;
import it.polimi.ingsw.psp12.model.enumeration.TurnState;

import java.util.List;

/**
 * Abstract decorator that will be used as interface implemented by each decorator
 * @author Michele Lucio, Luca Morandini
 */
public abstract class ExtendedPowerDecorator extends Power{

    protected Power power;

    @Override
    public void setMaxClimbLevel(int maxClimbLevel) {
        power.setMaxClimbLevel(maxClimbLevel);
    }

    @Override
    public int getMinDomeLevel() {
        return power.getMinDomeLevel();
    }

    @Override
    public int getNextPlayerMaxClimb() {
        return power.getNextPlayerMaxClimb();
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

    @Override
    public List<Action> nextActions(TurnState turnState) {
        return power.nextActions(turnState);
    }

    @Override
    public Point getOtherWorkerMove(Point currentPos, Point otherPos) {
        return power.getOtherWorkerMove(currentPos, otherPos);
    }

    @Override
    public void moved(Cell position) {
        power.moved(position);
    }

    @Override
    public void setInitialPosition(Cell position) {
        power.setInitialPosition(position);
    }

    @Override
    public void hasBuilt(Cell position) {
        power.hasBuilt(position);
    }

    @Override
    public void reset() {
        power.reset();
    }

    /**
     * Getter for decorators of the last positions history of the basic power
     * @return last positions history
     */
    protected Cell[] getLastPositions() {
        return power.lastPositions;
    }

    /**
     * Getter for decorators of the last builds history of the basic power
     * @return last builds history
     */
    protected Cell[] getLastBuilds() {
        return power.lastBuilds;
    }
}
