package it.polimi.ingsw.psp12.model;

import it.polimi.ingsw.psp12.model.board.Board;
import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.model.board.Tower;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class BoardTest {
    private Board testBoard;
    private Board testEmptyBoard;

    @Before
    public void setUp() {
        testBoard = new Board();
        testEmptyBoard = new Board();

        // add test worker
        testBoard.getCell(new Point(1, 2)).addWorker(new Worker());
    }

    @Test
    public void getCell_ValidPoint_ShouldReturnCell() {
        Cell cell = testBoard.getCell(new Point(0, 1));
        Point loc = cell.getLocation();
        Tower tow = cell.getTower();

        // check location
        assertEquals(0, loc.getX());
        assertEquals(1, loc.getY());

        // check worker
        assertFalse(cell.hasWorker());

        // check tower
        assertEquals(0, tow.getLevel());
        assertFalse(tow.hasDome());
    }

    @Test
    public void getCell_InvalidPoint_ShouldThrowException() {
        Cell cell = testBoard.getCell(new Point(0, 5));

        // TODO: search how to manage exception in tests
        fail();
    }

    @Test
    public void move_ValidPoints_ShouldUpdateBoard() {
        Point oldPoint = new Point(1, 2);
        Point newPoint = new Point(1, 3);

        // check initial state
        assertTrue(testBoard.getCell(oldPoint).hasWorker());
        assertFalse(testBoard.getCell(newPoint).hasWorker());

        // move
        testBoard.move(oldPoint, newPoint);

        // check final state
        assertFalse(testBoard.getCell(oldPoint).hasWorker());
        assertTrue(testBoard.getCell(newPoint).hasWorker());
    }

    @Test
    public void move_InvalidPoints_ShouldThrowException() {
        Point oldPoint = new Point(1, 5); // invalid point
        Point newPoint = new Point(1, 3);

        // move
        testBoard.move(oldPoint, newPoint);

        // TODO: search how to manage exception in tests
        fail();
    }

    @Test
    public void build_ValidPoint_ShouldUpdateBoard() {
        Point pos = new Point(0, 1);

        // check initial state
        assertEquals(0, testBoard.getCell(pos).getTower().getLevel());

        // build
        testBoard.build(pos);

        // check final state
        assertEquals(1, testBoard.getCell(pos).getTower().getLevel());
    }

    @Test
    public void build_InvalidPoints_ShouldThrowException() {
        Point pos = new Point(5, 1); // invalid point

        // build
        testBoard.build(pos);

        // TODO: search how to manage exception in tests
        fail();
    }

    @Test
    public void getCellsWithoutWorker_ShouldReturnAvailableCells() {
        // initialize state
        testEmptyBoard.getCell(new Point(0, 0)).addWorker(new Worker());
        testEmptyBoard.getCell(new Point(1, 1)).addWorker(new Worker());
        testEmptyBoard.getCell(new Point(2, 3)).addWorker(new Worker());
        testEmptyBoard.getCell(new Point(4, 3)).addWorker(new Worker());

        List<Cell> availableCells = testEmptyBoard.getCellsWithoutWorker();
        assertEquals(21, availableCells.size());
        for (Cell c : availableCells) {
            assertFalse(c.hasWorker());
        }
    }
}
