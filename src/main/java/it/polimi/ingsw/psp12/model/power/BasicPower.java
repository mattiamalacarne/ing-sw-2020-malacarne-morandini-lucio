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
        //maxBuildsLevel = 1;
        minDomeLevel = 3;
        movesCount = 0;
        buildsCount = 0;
        lastPositions = new Cell[2];
        lastBuild = null;

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
            case BUILD:
                actions.add(Action.END);
        }

        return actions;
    }

    @Override
    public boolean checkVictory() {
        // player win if it is on a tower with level == 3
        return lastPositions[0].getTower().getLevel() == 3;
    }

    @Override
    public List<Cell> getPossibleMoves(Board b, Worker w) {
        List<Cell> cells = getAdjoiningCells(b, w.getPosition());

        int currentWorkerLevel = b.getCell(w.getPosition()).getTower().getLevel();

        // worker can move on cells without another worker and
        // with a delta level <= 1
        return cells.stream()
                .filter(c -> !c.hasWorker() && c.canMoveOn(currentWorkerLevel))
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
