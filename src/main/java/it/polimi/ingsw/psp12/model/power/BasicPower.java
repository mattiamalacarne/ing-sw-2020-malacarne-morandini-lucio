package it.polimi.ingsw.psp12.model.power;

import it.polimi.ingsw.psp12.model.Worker;
import it.polimi.ingsw.psp12.model.board.Board;
import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.model.enumeration.Action;
import it.polimi.ingsw.psp12.model.enumeration.TurnState;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Basic implementation of power class
 * @author Michele Lucio, Luca Morandini
 */
public class BasicPower extends Power {

    /**
     * Constructor
     */
    public BasicPower() {
        lastPositions = new Cell[3];
        lastBuilds = new Cell[2];
        //minDomeLevel = 3;

        reset();
    }

    @Override
    public void setMaxClimbLevel(int maxClimbLevel) {
        this.maxClimbLevel = maxClimbLevel;
    }

    @Override
    public int getMinDomeLevel() {
        return 3;
    }

    @Override
    public int getNextPlayerMaxClimb() {
        return 1;
    }

    @Override
    public boolean checkVictory() {
        // check if two last positions are available
        if (lastPositions[0] == null || lastPositions[1] == null) {
            return false;
        }

        // calculate difference from the two last positions
        int diff = lastPositions[0].getTower().getLevel() - lastPositions[1].getTower().getLevel();

        // player win if it is on a tower with level == 3 and moved up from a lower level
        return (lastPositions[0].getTower().getLevel() == 3 && diff >= 1);
    }

    @Override
    public List<Cell> getPossibleMoves(Board b, Worker w) {
        List<Cell> cells = getAdjoiningCells(b, w.getPosition());

        int currentWorkerLevel = b.getCell(w.getPosition()).getTower().getLevel();

        // worker can move on cells without another worker and
        // with a delta level <= maxClimbLevel
        return cells.stream()
                .filter(c -> !c.hasWorker() && c.canMoveOn(currentWorkerLevel, this.maxClimbLevel))
                .collect(Collectors.toList());
    }

    @Override
    public List<Cell> getPossibleBuilds(Board b, Worker w) {
        List<Cell> cells = getAdjoiningCells(b, w.getPosition());

        // worker can build on cells without another worker and
        // with a tower that does not have the dome
        return cells.stream()
                .filter(c -> !c.hasWorker() && c.canBuildOn())
                .collect(Collectors.toList());
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
                actions.add(Action.END);
                break;
        }

        return actions;
    }

    @Override
    public Point getOtherWorkerMove(Point currentPos, Point otherPos) {
        return currentPos;
    }

    @Override
    public void moved(Cell position) {
        // store at index 2 the second-last position
        this.lastPositions[2] = this.lastPositions[1];
        // store at index 1 the previous position
        this.lastPositions[1] = this.lastPositions[0];
        // store at index 0 the current position
        this.lastPositions[0] = position;
        movesCount++;
    }

    @Override
    public void setInitialPosition(Cell position) {
        this.lastPositions[0] = position;
    }

    @Override
    public void hasBuilt(Cell position) {
        // store at index 1 the previous position
        this.lastBuilds[1] = this.lastBuilds[0];
        // store at index 0 the current position
        this.lastBuilds[0] = position;
        buildsCount++;
    }

    @Override
    public void setBuildInProgress(Cell position) {
        this.buildInProgress = position;
    }

    @Override
    public Cell getBuildInProgress() {
        return this.buildInProgress;
    }

    @Override
    public void reset() {
        //powerId = 0;
        maxClimbLevel = 1;
        //maxMoves = 1;
        //maxBuildsLevel = 1;
        //minDomeLevel = 3;
        movesCount = 0;
        buildsCount = 0;
        //nextPlayerMaxClimb = 1;
    }

    /**
     * Generates the list of adjoining cells of the provided cell
     * @param b game board
     * @param center cell used to find adjoins
     * @return list of adjoining cells
     */
    List<Cell> getAdjoiningCells(Board b, Point center) {
        List<Cell> cells = new ArrayList<>();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                Point p = new Point(center.getX() + dx, center.getY() + dy);
                // keep only cells that are on the board and different from the current cell
                if (p.isOnBoard() && !p.equals(center)) {
                    // return a clone of the cell
                    cells.add(b.getCell(p).clone());
                }
            }
        }

        return cells;
    }
}
