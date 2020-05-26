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
 * If your worker does not move up,
 * it may build both before and after moving
 * @author Luca Morandini
 */
public class BuildBeforeMoveAgainDecorator extends ExtendedPowerDecorator {

    public BuildBeforeMoveAgainDecorator(Power power) {
        this.power = power;
    }

    @Override
    public List<Action> nextActions(TurnState turnState, Board b) {
        List<Action> actions = new ArrayList<>();

        switch (turnState) {
            case INIT:
                // at the beginning of the turn, player can move or build (if possible)
                actions.add(Action.MOVE);

                int currentWorkerLevel = getLastPositions()[0].getTower().getLevel();
                Worker w = getLastPositions()[0].getWorker();

                // get the list of possible moves if building before moving
                // (keep only the cells on the same or lower level)
                List<Cell> moves = getPossibleMoves(b, w);
                moves.removeIf(c -> c.getTower().getLevel() > currentWorkerLevel);

                // if there is at least one move, check if it is possible to build without blocking
                if (moves.size() > 0) {
                    // get the list of possible builds
                    // (the cells are filtered to remove the blocking builds)
                    List<Cell> builds = getPossibleBuilds(b, w);

                    // if there is at least one non-blocking build, allow to build before move
                    if (builds.size() > 0) {
                        actions.add(Action.BUILD);
                    }
                }
                break;
            case MOVE:
                // after moving, player has to build
                actions.add(Action.BUILD);
                break;
            case BUILD:
                if (getMovesCount() == 0)
                    // if player has not moved, must move after the first build
                    actions.add(Action.MOVE);
                else
                    // otherwise if player moved and has built, must end the turn
                    actions.add(Action.END);
                break;
        }

        return actions;
    }

    @Override
    public List<Cell> getPossibleMoves(Board b, Worker w) {
        List<Cell> cells = super.getPossibleMoves(b, w);

        // if player has built, can not move up
        if (getBuildsCount() >= 1) {
            int currentWorkerLevel = b.getCell(w.getPosition()).getTower().getLevel();

            // remove cells with a level greater than the current level
            cells.removeIf(c -> c.getTower().getLevel() > currentWorkerLevel);
        }

        return cells;
    }

    @Override
    public List<Cell> getPossibleBuilds(Board b, Worker w) {
        List<Cell> cells = super.getPossibleBuilds(b, w);

        // if build before move, avoid to block the move action
        if (getMovesCount() == 0) {
            int currentWorkerLevel = b.getCell(w.getPosition()).getTower().getLevel();

            // get the cells on the same level of the worker
            List<Cell> sameLevel = cells.stream()
                    .filter(c -> c.getTower().getLevel() == currentWorkerLevel)
                    .collect(Collectors.toList());

            // if there is only one cell on the same level, check if building on it is blocking
            // (the build is blocking if there are no other cells on lower levels)
            if (sameLevel.size() == 1) {
                // get the cells on lower level
                long lowerLevelCount = cells.stream()
                        .filter(c -> c.getTower().getLevel() < currentWorkerLevel)
                        .count();

                // if there are no cells on lower levels, remove the blocking cell
                if (lowerLevelCount == 0) {
                    cells.remove(sameLevel.get(0));
                }
            }
        }

        return cells;
    }
}
