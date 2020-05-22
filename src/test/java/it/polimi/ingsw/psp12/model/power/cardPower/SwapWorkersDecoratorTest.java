package it.polimi.ingsw.psp12.model.power.cardPower;

import it.polimi.ingsw.psp12.model.Worker;
import it.polimi.ingsw.psp12.model.board.Board;
import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.model.power.BasicPower;
import it.polimi.ingsw.psp12.model.power.Power;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SwapWorkersDecoratorTest {
    Power power = null;
    Board gameBoard = null;

    @Before
    public void setUp() {
        power = new SwapWorkersDecorator(new BasicPower());
        gameBoard = new Board();
    }

    @Test
    public void getPossibleMoves_WithNearWorkers_ShouldReturnValidCells() {
        Point center = new Point(2, 2);
        Worker w = new Worker(0, "P1", 0);
        w.move(center);
        // place worker on the board
        gameBoard.getCell(center).addWorker(w);

        // place current player neighbor worker
        gameBoard.getCell(new Point(1, 2)).addWorker(new Worker(0, "P1", 1));

        // place neighbors workers
        gameBoard.getCell(new Point(1, 1)).addWorker(new Worker(1, "P2", 0));
        gameBoard.getCell(new Point(3, 3)).addWorker(new Worker(2, "P3", 1));

        // place not neighbors workers
        gameBoard.getCell(new Point(0, 0)).addWorker(new Worker(1, "P2", 1));
        gameBoard.getCell(new Point(3, 4)).addWorker(new Worker(2, "P3", 0));

        // build towers levels
        gameBoard.getCell(new Point(1, 3)).getTower().incrementLevel();
        gameBoard.getCell(new Point(2, 3)).getTower().incrementLevel();
        gameBoard.getCell(new Point(2, 3)).getTower().incrementLevel();

        // build domes
        gameBoard.getCell(new Point(3, 1)).getTower().buildDome();

        // get possible moves
        List<Cell> cells = power.getPossibleMoves(gameBoard, w);

        // convert cells to points
        List<Point> points = cells.stream().map(c -> c.getLocation()).collect(Collectors.toList());

        List<Point> expected = new ArrayList<>(Arrays.asList(
                new Point(1, 1), new Point(2, 1), /*      dome      */
                /*     my worker    */ /*   center point   */ new Point(3, 2),
                new Point(1, 3), /*    two levels    */ new Point(3, 3)
        ));

        assertEquals(expected.size(), points.size());
        assertTrue(expected.containsAll(points));
        assertTrue(points.containsAll(expected));
    }

    @Test
    public void getPossibleMoves_WithoutNearWorkers_ShouldReturnValidCells() {
        Point center = new Point(2, 2);
        Worker w = new Worker(0, "P1", 0);
        w.move(center);
        // place worker on the board
        gameBoard.getCell(center).addWorker(w);

        // place current player neighbor worker
        gameBoard.getCell(new Point(1, 2)).addWorker(new Worker(0, "P1", 1));

        // place not neighbors workers
        gameBoard.getCell(new Point(1, 0)).addWorker(new Worker(1, "P2", 0));
        gameBoard.getCell(new Point(3, 0)).addWorker(new Worker(1, "P2", 1));
        gameBoard.getCell(new Point(0, 3)).addWorker(new Worker(2, "P3", 1));
        gameBoard.getCell(new Point(3, 4)).addWorker(new Worker(2, "P3", 0));

        // build towers levels
        gameBoard.getCell(new Point(1, 3)).getTower().incrementLevel();
        gameBoard.getCell(new Point(2, 3)).getTower().incrementLevel();
        gameBoard.getCell(new Point(2, 3)).getTower().incrementLevel();

        // build domes
        gameBoard.getCell(new Point(3, 1)).getTower().buildDome();

        // get possible moves
        List<Cell> cells = power.getPossibleMoves(gameBoard, w);

        // convert cells to points
        List<Point> points = cells.stream().map(c -> c.getLocation()).collect(Collectors.toList());

        List<Point> expected = new ArrayList<>(Arrays.asList(
                new Point(1, 1), new Point(2, 1), /*      dome      */
                /*     my worker    */ /*   center point   */ new Point(3, 2),
                new Point(1, 3), /*    two levels    */ new Point(3, 3)
        ));

        assertEquals(expected.size(), points.size());
        assertTrue(expected.containsAll(points));
        assertTrue(points.containsAll(expected));
    }
}
