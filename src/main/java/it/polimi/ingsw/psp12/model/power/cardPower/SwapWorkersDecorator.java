package it.polimi.ingsw.psp12.model.power.cardPower;

import it.polimi.ingsw.psp12.model.Worker;
import it.polimi.ingsw.psp12.model.board.Board;
import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.power.ExtendedPowerDecorator;
import it.polimi.ingsw.psp12.model.power.Power;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Your worker may move into an opponent worker's space
 * by forcing their worker to the space yours just vacated
 * @author Luca Morandini
 */
public class SwapWorkersDecorator extends ExtendedPowerDecorator {

    public SwapWorkersDecorator(Power power) {
        this.power = power;
    }

    @Override
    public List<Cell> getPossibleMoves(Board b, Worker w) {
        List<Cell> cells = super.getPossibleMoves(b, w);

        // get list of the cells where there is a worker
        List<Cell> otherWorkers = b.getCellsWithWorker().stream()
                // remove cells that are not near to the current cell
                .filter(c -> c.getLocation().isNear(w.getPosition()))
                // keep only cells with an opponent worker
                .filter(c -> c.getWorker().getPlayerId() != w.getPlayerId())
                .collect(Collectors.toList());

        cells.addAll(otherWorkers);

        return cells;
    }
}
