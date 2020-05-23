package it.polimi.ingsw.psp12.model.power.cardPower;

import it.polimi.ingsw.psp12.model.Worker;
import it.polimi.ingsw.psp12.model.board.Board;
import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.model.power.ExtendedPowerDecorator;
import it.polimi.ingsw.psp12.model.power.Power;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Your worker may move into an opponent worker's space, if their worker
 * can be forced one space straight backwards to an unoccupied space at any level
 * @author Luca Morandini
 */
public class PushAwayWorkerDecorator extends ExtendedPowerDecorator {

    public PushAwayWorkerDecorator(Power power) {
        this.power = power;
    }

    @Override
    public List<Cell> getPossibleMoves(Board b, Worker w) {
        List<Cell> cells = super.getPossibleMoves(b, w);

        int currentWorkerLevel = b.getCell(w.getPosition()).getTower().getLevel();

        // get list of the cells where there is a worker
        List<Cell> otherWorkers = b.getCellsWithWorker().stream()
                // remove cells that are not near to the current cell
                .filter(c -> c.getLocation().isNear(w.getPosition()))
                // keep only cells with an opponent worker
                .filter(c -> c.getWorker().getPlayerId() != w.getPlayerId())
                // keep only cells with a delta level <= maxClimbLevel
                .filter(c -> c.canMoveOn(currentWorkerLevel, getMaxClimbLevel()))
                .collect(Collectors.toList());


        // remove cells with a worker that can not be pushed away
        for (Cell c : otherWorkers) {
            // calculate the new point for the worker to be pushed
            Point otherNewPoint = this.getOtherWorkerMove(w.getPosition(), c.getLocation());

            // check if the move is valid (the new point is on the board)
            if (otherNewPoint.isOnBoard()) {
                // get the new cell for the worker to be pushed
                Cell otherNewCell = b.getCell(otherNewPoint);

                // check if the cell is unoccupied (no dome and no worker)
                if (!otherNewCell.hasWorker() && !otherNewCell.getTower().hasDome()) {
                    cells.add(c);
                }
            }
        }

        return cells;
    }

    @Override
    public Point getOtherWorkerMove(Point currentPos, Point otherPos) {
        // calculate the new position of a worker that is pushed away

        // new+(new-old) == 2*new-old
        int x = 2 * otherPos.getX() - currentPos.getX();
        int y = 2 * otherPos.getY() - currentPos.getY();

        return new Point(x, y);
    }
}
