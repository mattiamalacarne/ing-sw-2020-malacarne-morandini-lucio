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

public class PushAwayWorkerDecoratorTest {
    Power power = null;
    Board gameBoard = null;

    @Before
    public void setUp() {
        power = new PushAwayWorkerDecorator(new BasicPower());
        gameBoard = new Board();
    }

    @Test
    public void getPossibleMoves1_ShouldReturnValidCells() {
        Point center = new Point(2, 2);
        Worker w = new Worker(0, "P1", 0);
        w.move(center);
        // place worker on the board
        gameBoard.getCell(center).addWorker(w);

        // place current player neighbor worker
        gameBoard.getCell(new Point(1, 1)).addWorker(new Worker(0, "P1", 1));

        // place neighbors workers
        gameBoard.getCell(new Point(2, 1)).addWorker(new Worker(1, "P2", 0));
        gameBoard.getCell(new Point(1, 2)).addWorker(new Worker(1, "P2", 1));
        gameBoard.getCell(new Point(2, 3)).addWorker(new Worker(2, "P3", 1));
        gameBoard.getCell(new Point(3, 2)).addWorker(new Worker(2, "P3", 0));

        // build towers levels
        gameBoard.getCell(new Point(4, 2)).getTower().incrementLevel();
        gameBoard.getCell(new Point(4, 2)).getTower().incrementLevel();

        // build domes
        gameBoard.getCell(new Point(2, 4)).getTower().buildDome();
        gameBoard.getCell(new Point(0, 2)).getTower().buildDome();

        // get possible moves
        List<Cell> cells = power.getPossibleMoves(gameBoard, w);

        // convert cells to points
        List<Point> points = cells.stream().map(c -> c.getLocation()).collect(Collectors.toList());

        List<Point> expected = new ArrayList<>(Arrays.asList(
                /*     my worker    */ new Point(2, 1), new Point(3, 1),
                /*  worker blocked  */ /*   center point   */ new Point(3, 2),
                new Point(1, 3), /*  worker blocked  */ new Point(3, 3)
        ));

        assertEquals(expected.size(), points.size());
        assertTrue(expected.containsAll(points));
        assertTrue(points.containsAll(expected));
    }

    @Test
    public void getPossibleMoves2_ShouldReturnValidCells() {
        Point center = new Point(2, 2);
        Worker w = new Worker(0, "P1", 0);
        w.move(center);
        // place worker on the board
        gameBoard.getCell(center).addWorker(w);

        // place current player neighbor worker
        gameBoard.getCell(new Point(2, 3)).addWorker(new Worker(0, "P1", 1));

        // place neighbors workers
        gameBoard.getCell(new Point(1, 1)).addWorker(new Worker(1, "P2", 0));
        gameBoard.getCell(new Point(3, 1)).addWorker(new Worker(1, "P2", 1));
        gameBoard.getCell(new Point(1, 3)).addWorker(new Worker(2, "P3", 1));
        gameBoard.getCell(new Point(3, 3)).addWorker(new Worker(2, "P3", 0));

        // build towers levels
        gameBoard.getCell(new Point(0, 0)).getTower().incrementLevel();
        gameBoard.getCell(new Point(0, 0)).getTower().incrementLevel();
        gameBoard.getCell(new Point(0, 0)).getTower().incrementLevel();

        // build domes
        gameBoard.getCell(new Point(4, 0)).getTower().buildDome();
        gameBoard.getCell(new Point(0, 4)).getTower().buildDome();

        // get possible moves
        List<Cell> cells = power.getPossibleMoves(gameBoard, w);

        // convert cells to points
        List<Point> points = cells.stream().map(c -> c.getLocation()).collect(Collectors.toList());

        List<Point> expected = new ArrayList<>(Arrays.asList(
                new Point(1, 1), new Point(2, 1), /*  worker blocked  */
                new Point(1, 2), /*   center point   */ new Point(3, 2),
                /*  worker blocked  */ /*     my worker    */ new Point(3, 3)
        ));

        assertEquals(expected.size(), points.size());
        assertTrue(expected.containsAll(points));
        assertTrue(points.containsAll(expected));
    }

    @Test
    public void getPossibleMoves3_ShouldReturnValidCells() {
        Point center = new Point(3, 3);
        Worker w = new Worker(0, "P1", 0);
        w.move(center);
        // place worker on the board
        gameBoard.getCell(center).addWorker(w);

        // place current player not neighbor worker
        gameBoard.getCell(new Point(1, 1)).addWorker(new Worker(0, "P1", 1));

        // place neighbors workers
        gameBoard.getCell(new Point(3, 4)).addWorker(new Worker(1, "P2", 0));
        gameBoard.getCell(new Point(4, 2)).addWorker(new Worker(1, "P2", 1));
        gameBoard.getCell(new Point(4, 3)).addWorker(new Worker(2, "P3", 1));
        gameBoard.getCell(new Point(4, 4)).addWorker(new Worker(2, "P3", 0));

        // build towers levels
        gameBoard.getCell(new Point(2, 3)).getTower().incrementLevel();
        gameBoard.getCell(new Point(2, 3)).getTower().incrementLevel();

        // build domes
        gameBoard.getCell(new Point(4, 0)).getTower().buildDome();

        // get possible moves
        List<Cell> cells = power.getPossibleMoves(gameBoard, w);

        // convert cells to points
        List<Point> points = cells.stream().map(c -> c.getLocation()).collect(Collectors.toList());

        List<Point> expected = new ArrayList<>(Arrays.asList(
                new Point(2, 2), new Point(3, 2), /*  worker blocked  */
                /*    two levels    */ /*   center point   */ /*  worker blocked  */
                new Point(2, 4)  /*  worker blocked  */ /*  worker blocked  */
        ));

        assertEquals(expected.size(), points.size());
        assertTrue(expected.containsAll(points));
        assertTrue(points.containsAll(expected));
    }

    @Test
    public void getPossibleMoves4_ShouldReturnValidCells() {
        Point center = new Point(1, 1);
        Worker w = new Worker(0, "P1", 0);
        w.move(center);
        // place worker on the board
        gameBoard.getCell(center).addWorker(w);

        // place current player not neighbor worker
        gameBoard.getCell(new Point(2, 3)).addWorker(new Worker(0, "P1", 1));

        // place neighbors workers
        gameBoard.getCell(new Point(0, 0)).addWorker(new Worker(1, "P2", 0));
        gameBoard.getCell(new Point(1, 0)).addWorker(new Worker(1, "P2", 1));
        gameBoard.getCell(new Point(2, 0)).addWorker(new Worker(2, "P3", 1));
        gameBoard.getCell(new Point(0, 1)).addWorker(new Worker(2, "P3", 0));

        // build towers levels
        gameBoard.getCell(new Point(1, 2)).getTower().incrementLevel();
        gameBoard.getCell(new Point(1, 2)).getTower().incrementLevel();

        // build domes
        gameBoard.getCell(new Point(3, 0)).getTower().buildDome();

        // get possible moves
        List<Cell> cells = power.getPossibleMoves(gameBoard, w);

        // convert cells to points
        List<Point> points = cells.stream().map(c -> c.getLocation()).collect(Collectors.toList());

        List<Point> expected = new ArrayList<>(Arrays.asList(
                /*  worker blocked  */ /*  worker blocked  */ /*  worker blocked  */
                /*  worker blocked  */ /*   center point   */ new Point(2, 1),
                new Point(0, 2), /*    two levels    */ new Point(2, 2)
        ));

        assertEquals(expected.size(), points.size());
        assertTrue(expected.containsAll(points));
        assertTrue(points.containsAll(expected));
    }

    @Test
    public void getPossibleMoves5_ShouldReturnValidCells() {
        Point center = new Point(3, 2);
        Worker w = new Worker(0, "P1", 0);
        w.move(center);
        // place worker on the board
        gameBoard.getCell(center).addWorker(w);

        // place current player neighbor worker
        gameBoard.getCell(new Point(3, 1)).addWorker(new Worker(0, "P1", 1));

        // place neighbors workers
        gameBoard.getCell(new Point(2, 1)).addWorker(new Worker(1, "P2", 0));
        gameBoard.getCell(new Point(4, 1)).addWorker(new Worker(1, "P2", 1));
        gameBoard.getCell(new Point(4, 2)).addWorker(new Worker(2, "P3", 1));
        gameBoard.getCell(new Point(3, 3)).addWorker(new Worker(2, "P3", 0));

        // build towers levels
        gameBoard.getCell(new Point(1, 1)).getTower().incrementLevel();
        gameBoard.getCell(new Point(1, 1)).getTower().incrementLevel();
        gameBoard.getCell(new Point(3, 4)).getTower().incrementLevel();
        gameBoard.getCell(new Point(3, 4)).getTower().incrementLevel();
        gameBoard.getCell(new Point(3, 4)).getTower().incrementLevel();

        // build domes
        gameBoard.getCell(new Point(2, 0)).getTower().buildDome();

        // get possible moves
        List<Cell> cells = power.getPossibleMoves(gameBoard, w);

        // convert cells to points
        List<Point> points = cells.stream().map(c -> c.getLocation()).collect(Collectors.toList());

        List<Point> expected = new ArrayList<>(Arrays.asList(
                new Point(2, 1), /*     my worker    */ /*  worker blocked  */
                new Point(2, 2), /*   center point   */ /*  worker blocked  */
                new Point(2, 3), new Point(3, 3), new Point(4, 3)
        ));

        assertEquals(expected.size(), points.size());
        assertTrue(expected.containsAll(points));
        assertTrue(points.containsAll(expected));
    }

    @Test
    public void getPossibleMoves6_ShouldReturnValidCells() {
        Point center = new Point(1, 2);
        Worker w = new Worker(0, "P1", 0);
        w.move(center);
        // place worker on the board
        gameBoard.getCell(center).addWorker(w);

        // place current player neighbor worker
        gameBoard.getCell(new Point(0, 2)).addWorker(new Worker(0, "P1", 1));

        // place neighbors workers
        gameBoard.getCell(new Point(0, 1)).addWorker(new Worker(1, "P2", 0));
        gameBoard.getCell(new Point(1, 1)).addWorker(new Worker(1, "P2", 1));
        gameBoard.getCell(new Point(2, 2)).addWorker(new Worker(2, "P3", 1));
        gameBoard.getCell(new Point(2, 3)).addWorker(new Worker(2, "P3", 0));

        // build towers levels
        gameBoard.getCell(new Point(2, 1)).getTower().incrementLevel();
        gameBoard.getCell(new Point(2, 1)).getTower().incrementLevel();

        // build domes
        gameBoard.getCell(new Point(3, 3)).getTower().buildDome();

        // get possible moves
        List<Cell> cells = power.getPossibleMoves(gameBoard, w);

        // convert cells to points
        List<Point> points = cells.stream().map(c -> c.getLocation()).collect(Collectors.toList());

        List<Point> expected = new ArrayList<>(Arrays.asList(
                /*  worker blocked  */ new Point(1, 1), /*    two levels    */
                /*     my worker    */ /*   center point   */ new Point(2, 2),
                new Point(0, 3), new Point(1, 3), new Point(2, 3)
        ));

        assertEquals(expected.size(), points.size());
        assertTrue(expected.containsAll(points));
        assertTrue(points.containsAll(expected));
    }

    @Test
    public void getPossibleMoves7_ShouldReturnValidCells() {
        Point center = new Point(2, 2);
        Worker w = new Worker(0, "P1", 0);
        w.move(center);
        // place worker on the board
        gameBoard.getCell(center).addWorker(w);

        // place current player not neighbor worker
        gameBoard.getCell(new Point(4, 4)).addWorker(new Worker(0, "P1", 1));

        // place neighbors workers
        gameBoard.getCell(new Point(1, 1)).addWorker(new Worker(1, "P2", 0));
        gameBoard.getCell(new Point(2, 1)).addWorker(new Worker(1, "P2", 1));
        gameBoard.getCell(new Point(3, 2)).addWorker(new Worker(2, "P3", 1));
        gameBoard.getCell(new Point(2, 0)).addWorker(new Worker(2, "P3", 0));

        // build towers levels
        gameBoard.getCell(new Point(3, 2)).getTower().incrementLevel();
        gameBoard.getCell(new Point(3, 2)).getTower().incrementLevel();

        // build domes
        gameBoard.getCell(new Point(1, 3)).getTower().buildDome();

        // get possible moves
        List<Cell> cells = power.getPossibleMoves(gameBoard, w);

        // convert cells to points
        List<Point> points = cells.stream().map(c -> c.getLocation()).collect(Collectors.toList());

        List<Point> expected = new ArrayList<>(Arrays.asList(
                new Point(1, 1), /*  worker blocked  */ new Point(3, 1),
                new Point(1, 2), /*   center point   */ /*    two levels    */
                /*       dome       */ new Point(2, 3), new Point(3, 3)
        ));

        assertEquals(expected.size(), points.size());
        assertTrue(expected.containsAll(points));
        assertTrue(points.containsAll(expected));
    }

    @Test
    public void getOtherWorkerMove1_ShouldReturnCorrectCell() {
        Point oldPoint = new Point(2, 2);
        Point newPoint = new Point(3, 1);

        Point otherNewPoint = new Point(4, 0);
        assertEquals(otherNewPoint, power.getOtherWorkerMove(oldPoint, newPoint));
    }

    @Test
    public void getOtherWorkerMove2_ShouldReturnCorrectCell() {
        Point oldPoint = new Point(4, 3);
        Point newPoint = new Point(4, 2);

        Point otherNewPoint = new Point(4, 1);
        assertEquals(otherNewPoint, power.getOtherWorkerMove(oldPoint, newPoint));
    }

    @Test
    public void getOtherWorkerMove3_ShouldReturnCorrectCell() {
        Point oldPoint = new Point(2, 1);
        Point newPoint = new Point(3, 1);

        Point otherNewPoint = new Point(4, 1);
        assertEquals(otherNewPoint, power.getOtherWorkerMove(oldPoint, newPoint));
    }

    @Test
    public void getOtherWorkerMove4_ShouldReturnCorrectCell() {
        Point oldPoint = new Point(3, 2);
        Point newPoint = new Point(2, 3);

        Point otherNewPoint = new Point(1, 4);
        assertEquals(otherNewPoint, power.getOtherWorkerMove(oldPoint, newPoint));
    }
}
