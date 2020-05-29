package it.polimi.ingsw.psp12.model.board;

import it.polimi.ingsw.psp12.model.Worker;

import it.polimi.ingsw.psp12.model.enumeration.BuildOption;
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

        // add test workers
        testBoard.getCell(new Point(1, 2)).addWorker(new Worker());
        testBoard.getCell(new Point(4, 2)).addWorker(new Worker());
        testBoard.getCell(new Point(4, 3)).addWorker(new Worker());
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
    public void move_TwoWorkers_ShouldMoveWorkers() {
        Point oldPoint = new Point(4, 2);
        Point newPoint = new Point(4, 3);
        Point otherNewPoint = new Point(4, 4);

        Worker w1 = testBoard.getCell(oldPoint).getWorker();
        Worker w2 = testBoard.getCell(newPoint).getWorker();

        // check initial state
        assertTrue(testBoard.getCell(oldPoint).hasWorker());
        assertTrue(testBoard.getCell(newPoint).hasWorker());
        assertFalse(testBoard.getCell(otherNewPoint).hasWorker());
        assertEquals(w1, testBoard.getCell(oldPoint).getWorker());
        assertEquals(w2, testBoard.getCell(newPoint).getWorker());

        // move
        testBoard.move(oldPoint, newPoint, otherNewPoint);

        // check final state
        assertFalse(testBoard.getCell(oldPoint).hasWorker());
        assertTrue(testBoard.getCell(newPoint).hasWorker());
        assertTrue(testBoard.getCell(otherNewPoint).hasWorker());
        assertEquals(w1, testBoard.getCell(newPoint).getWorker());
        assertEquals(w2, testBoard.getCell(otherNewPoint).getWorker());
    }

    @Test
    public void move_TwoWorkers_ShouldSwapWorkers() {
        Point oldPoint = new Point(4, 2);
        Point newPoint = new Point(4, 3);
        Point otherNewPoint = new Point(4, 2);

        Worker w1 = testBoard.getCell(oldPoint).getWorker();
        Worker w2 = testBoard.getCell(newPoint).getWorker();

        // check initial state
        assertTrue(testBoard.getCell(oldPoint).hasWorker());
        assertTrue(testBoard.getCell(newPoint).hasWorker());
        assertEquals(w1, testBoard.getCell(oldPoint).getWorker());
        assertEquals(w2, testBoard.getCell(newPoint).getWorker());

        // move
        testBoard.move(oldPoint, newPoint, otherNewPoint);

        // check final state
        assertTrue(testBoard.getCell(newPoint).hasWorker());
        assertTrue(testBoard.getCell(otherNewPoint).hasWorker());
        assertEquals(w1, testBoard.getCell(newPoint).getWorker());
        assertEquals(w2, testBoard.getCell(otherNewPoint).getWorker());
    }

    @Test
    public void build_Block_ShouldUpdateBoard() {
        Point pos = new Point(0, 1);

        // check initial state
        assertEquals(0, testBoard.getCell(pos).getTower().getLevel());
        assertFalse(testBoard.getCell(pos).getTower().hasDome());

        // build
        testBoard.build(pos, BuildOption.BLOCK);

        // check final state
        assertEquals(1, testBoard.getCell(pos).getTower().getLevel());
        assertFalse(testBoard.getCell(pos).getTower().hasDome());
    }

    @Test
    public void build_Dome_ShouldUpdateBoard() {
        Point pos = new Point(0, 1);

        // check initial state
        assertEquals(0, testBoard.getCell(pos).getTower().getLevel());
        assertFalse(testBoard.getCell(pos).getTower().hasDome());

        // build
        testBoard.build(pos, BuildOption.DOME);

        // check final state
        assertEquals(0, testBoard.getCell(pos).getTower().getLevel());
        assertTrue(testBoard.getCell(pos).getTower().hasDome());
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

    @Test
    public void getCellsWithWorker_ShouldReturnOccupiedCells() {
        // initialize state
        testEmptyBoard.getCell(new Point(0, 0)).addWorker(new Worker());
        testEmptyBoard.getCell(new Point(1, 1)).addWorker(new Worker());
        testEmptyBoard.getCell(new Point(2, 3)).addWorker(new Worker());
        testEmptyBoard.getCell(new Point(4, 3)).addWorker(new Worker());

        List<Cell> availableCells = testEmptyBoard.getCellsWithWorker();
        assertEquals(4, availableCells.size());
        for (Cell c : availableCells) {
            assertTrue(c.hasWorker());
        }
    }

    @Test
    public void getCellsWithBuild_ShouldReturnCellsWithTower() {
        // initialize state
        testEmptyBoard.getCell(new Point(1, 1)).getTower().incrementLevel();

        testEmptyBoard.getCell(new Point(0, 2)).getTower().incrementLevel();

        testEmptyBoard.getCell(new Point(3, 0)).getTower().incrementLevel();
        testEmptyBoard.getCell(new Point(3, 0)).getTower().incrementLevel();
        testEmptyBoard.getCell(new Point(3, 0)).getTower().incrementLevel();

        testEmptyBoard.getCell(new Point(2, 2)).getTower().buildDome();

        List<Cell> availableCells = testEmptyBoard.getCellsWithBuild();
        assertEquals(4, availableCells.size());
        for (Cell c : availableCells) {
            assertTrue(c.getTower().getLevel() > 0 || c.getTower().hasDome());
        }
    }

    @Test
    public void restoreSimulation_ShouldRestoreBoardState() {
        testEmptyBoard.build(new Point(0, 0), BuildOption.BLOCK);
        testEmptyBoard.build(new Point(0, 0), BuildOption.BLOCK);

        testEmptyBoard.build(new Point(1, 1), BuildOption.BLOCK);
        testEmptyBoard.build(new Point(1, 1), BuildOption.DOME);

        Worker w1 = new Worker(0, "P1", 0);
        Worker w2 = new Worker(0, "P1", 1);
        Point w1pos = new Point(2, 2);
        Point w1pos1 = new Point(2, 3);
        Point w2pos = new Point(3, 3);

        w1.move(w1pos);
        w2.move(w2pos);
        testEmptyBoard.getCell(w1pos).addWorker(w1);
        testEmptyBoard.getCell(w2pos).addWorker(w2);

        // check initial state
        assertEquals(2, testEmptyBoard.getCell(new Point(0, 0)).getTower().getLevel());
        assertFalse(testEmptyBoard.getCell(new Point(0, 0)).getTower().hasDome());
        assertEquals(1, testEmptyBoard.getCell(new Point(1, 1)).getTower().getLevel());
        assertTrue(testEmptyBoard.getCell(new Point(1, 1)).getTower().hasDome());
        assertEquals(0, testEmptyBoard.getCell(new Point(1, 2)).getTower().getLevel());
        assertFalse(testEmptyBoard.getCell(new Point(1, 2)).getTower().hasDome());
        assertTrue(testEmptyBoard.getCell(w1pos).hasWorker());
        assertFalse(testEmptyBoard.getCell(w1pos1).hasWorker());
        assertTrue(testEmptyBoard.getCell(w2pos).hasWorker());
        assertSame(w1, testEmptyBoard.getCell(w1pos).getWorker());
        assertSame(w2, testEmptyBoard.getCell(w2pos).getWorker());
        assertEquals(w1pos, w1.getPosition());
        assertEquals(w2pos, w2.getPosition());

        // save snapshot
        testEmptyBoard.saveSnapshot();

        // update board
        w1.move(w1pos1);
        testEmptyBoard.move(w1pos, w1pos1);
        testEmptyBoard.build(new Point(0, 0), BuildOption.BLOCK);
        testEmptyBoard.build(new Point(1, 2), BuildOption.DOME);

        // check state
        assertEquals(3, testEmptyBoard.getCell(new Point(0, 0)).getTower().getLevel());
        assertFalse(testEmptyBoard.getCell(new Point(0, 0)).getTower().hasDome());
        assertEquals(1, testEmptyBoard.getCell(new Point(1, 1)).getTower().getLevel());
        assertTrue(testEmptyBoard.getCell(new Point(1, 1)).getTower().hasDome());
        assertEquals(0, testEmptyBoard.getCell(new Point(1, 2)).getTower().getLevel());
        assertTrue(testEmptyBoard.getCell(new Point(1, 2)).getTower().hasDome());
        assertFalse(testEmptyBoard.getCell(w1pos).hasWorker());
        assertTrue(testEmptyBoard.getCell(w1pos1).hasWorker());
        assertTrue(testEmptyBoard.getCell(w2pos).hasWorker());
        assertSame(w1, testEmptyBoard.getCell(w1pos1).getWorker());
        assertSame(w2, testEmptyBoard.getCell(w2pos).getWorker());
        assertEquals(w1pos1, w1.getPosition());
        assertEquals(w2pos, w2.getPosition());

        // restore snapshot
        testEmptyBoard.restoreSnapshot();

        // check final state
        assertEquals(2, testEmptyBoard.getCell(new Point(0, 0)).getTower().getLevel());
        assertFalse(testEmptyBoard.getCell(new Point(0, 0)).getTower().hasDome());
        assertEquals(1, testEmptyBoard.getCell(new Point(1, 1)).getTower().getLevel());
        assertTrue(testEmptyBoard.getCell(new Point(1, 1)).getTower().hasDome());
        assertEquals(0, testEmptyBoard.getCell(new Point(1, 2)).getTower().getLevel());
        assertFalse(testEmptyBoard.getCell(new Point(1, 2)).getTower().hasDome());
        assertTrue(testEmptyBoard.getCell(w1pos).hasWorker());
        assertFalse(testEmptyBoard.getCell(w1pos1).hasWorker());
        assertTrue(testEmptyBoard.getCell(w2pos).hasWorker());
        assertSame(w1, testEmptyBoard.getCell(w1pos).getWorker());
        assertSame(w2, testEmptyBoard.getCell(w2pos).getWorker());
        assertEquals(w1pos, w1.getPosition());
        assertEquals(w2pos, w2.getPosition());
    }
}
