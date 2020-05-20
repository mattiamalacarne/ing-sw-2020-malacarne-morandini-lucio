package it.polimi.ingsw.psp12.model.power;

import it.polimi.ingsw.psp12.model.Worker;
import it.polimi.ingsw.psp12.model.board.Board;
import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.model.enumeration.Action;
import it.polimi.ingsw.psp12.model.enumeration.TurnState;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class BasicPowerTest {

    BasicPower basicPower = null;
    Board gameBoard = null;

    @Before
    public void setUp(){
        basicPower = new BasicPower();
        gameBoard = new Board();
    }

    /*@Test
    public void getPowerId() {
        assertEquals(0, basicPower.getPowerId());
    }*/

    @Test
    public void getMaxClimbLevel() {
        assertEquals(1, basicPower.maxClimbLevel);
    }

    /*@Test
    public void getMaxMoves() {
        assertEquals(1, basicPower.getMaxMoves());
    }*/

    /*@Test
    public void getMaxBuildsLevel() {
        assertEquals(1, basicPower.getMaxBuildsLevel());
    }*/

    @Test
    public void getMinDomeLevel() {
        assertEquals(3, basicPower.getMinDomeLevel());
    }

    @Test
    public void getMovesCount() {
        assertEquals(0, basicPower.movesCount);
    }

    @Test
    public void getBuildsCount() {
        assertEquals(0, basicPower.buildsCount);
    }

    @Test
    public void getNextPlayerMaxClimb() {
        assertEquals(1, basicPower.getNextPlayerMaxClimb());
    }

    @Test
    public void reset() {
        //assertEquals(0, basicPower.getPowerId());
        assertEquals(1, basicPower.maxClimbLevel);
        //assertEquals(1, basicPower.getMaxMoves());
        //assertEquals(1, basicPower.getMaxBuildsLevel());
        assertEquals(3, basicPower.getMinDomeLevel());
        assertEquals(0, basicPower.movesCount);
        assertEquals(0, basicPower.buildsCount);
        assertEquals(1, basicPower.getNextPlayerMaxClimb());

    }

    @Test
    public void moved_ShouldUpdateMovesCountAndPositions() {
        // check initial state
        assertEquals(0, basicPower.movesCount);
        assertEquals(null, basicPower.lastPositions[0]);
        assertEquals(null, basicPower.lastPositions[1]);
        assertEquals(null, basicPower.lastPositions[2]);

        // move
        Cell c1 = new Cell(0, 0);
        basicPower.moved(c1);

        // check intermediate state
        assertEquals(1, basicPower.movesCount);
        assertEquals(c1, basicPower.lastPositions[0]);
        assertEquals(null, basicPower.lastPositions[1]);
        assertEquals(null, basicPower.lastPositions[2]);

        // move again
        Cell c2 = new Cell(1, 1);
        basicPower.moved(c2);

        // check final state
        assertEquals(2, basicPower.movesCount);
        assertEquals(c2, basicPower.lastPositions[0]);
        assertEquals(c1, basicPower.lastPositions[1]);
        assertEquals(null, basicPower.lastPositions[2]);
    }

    @Test
    public void setInitialPosition_ShouldSetPosition() {
        // check initial state
        assertEquals(0, basicPower.movesCount);
        assertEquals(null, basicPower.lastPositions[0]);
        assertEquals(null, basicPower.lastPositions[1]);
        assertEquals(null, basicPower.lastPositions[2]);

        // set initial position
        Cell c1 = new Cell(0, 0);
        basicPower.setInitialPosition(c1);

        // check final state
        assertEquals(0, basicPower.movesCount);
        assertEquals(c1, basicPower.lastPositions[0]);
        assertEquals(null, basicPower.lastPositions[1]);
        assertEquals(null, basicPower.lastPositions[2]);
    }

    @Test
    public void hasBuilt_Should_ShouldUpdateBuildsCountAndPosition() {
        // check initial state
        assertEquals(0, basicPower.buildsCount);
        assertEquals(null, basicPower.lastBuilds[0]);
        assertEquals(null, basicPower.lastBuilds[1]);

        // build
        Cell c1 = new Cell(2, 3);
        basicPower.hasBuilt(c1);

        // check final state
        assertEquals(1, basicPower.buildsCount);
        assertEquals(c1, basicPower.lastBuilds[0]);
        assertEquals(null, basicPower.lastBuilds[1]);
    }

    @Test
    public void getAdjoiningCells_OnCenter_ShouldReturnAllAdjoiningCells() {
        Point center = new Point(1, 2);

        // get cells
        List<Cell> cells = basicPower.getAdjoiningCells(gameBoard, center);

        // convert cells to points
        List<Point> points = cells.stream().map(c -> c.getLocation()).collect(Collectors.toList());

        List<Point> expected = new ArrayList<>(Arrays.asList(
                new Point(0, 1), new Point(1, 1), new Point(2, 1),
                new Point(0, 2), /*   center point   */ new Point(2, 2),
                new Point(0, 3), new Point(1, 3), new Point(2, 3)
        ));

        assertEquals(expected.size(), points.size());
        assertTrue(expected.containsAll(points));
        assertTrue(points.containsAll(expected));
    }

    @Test
    public void getAdjoiningCells_OnBorder_ShouldReturnAdjoiningCellsOnBoard() {
        Point center = new Point(3, 0);

        // get cells
        List<Cell> cells = basicPower.getAdjoiningCells(gameBoard, center);

        // convert cells to points
        List<Point> points = cells.stream().map(c -> c.getLocation()).collect(Collectors.toList());

        List<Point> expected = new ArrayList<>(Arrays.asList(
                new Point(2, 0), /*   center point   */ new Point(4, 0),
                new Point(2, 1), new Point(3, 1), new Point(4, 1)
        ));

        assertEquals(expected.size(), points.size());
        assertTrue(expected.containsAll(points));
        assertTrue(points.containsAll(expected));
    }

    @Test
    public void getAdjoiningCells_OnCorner_ShouldReturnAdjoiningCellsOnBoard() {
        Point center = new Point(0, 4);

        // get cells
        List<Cell> cells = basicPower.getAdjoiningCells(gameBoard, center);

        // convert cells to points
        List<Point> points = cells.stream().map(c -> c.getLocation()).collect(Collectors.toList());

        List<Point> expected = new ArrayList<>(Arrays.asList(
                new Point(0, 3), new Point(1, 3),
                /*   center point   */ new Point(1, 4)
        ));

        assertEquals(expected.size(), points.size());
        assertTrue(expected.containsAll(points));
        assertTrue(points.containsAll(expected));
    }

    @Test
    public void nextActions_InitState_ShouldReturnMove() {
        List<Action> actions = basicPower.nextActions(TurnState.INIT);
        assertEquals(1, actions.size());
        assertEquals(Action.MOVE, actions.get(0));
    }

    @Test
    public void nextActions_MoveState_ShouldReturnBuild() {
        List<Action> actions = basicPower.nextActions(TurnState.MOVE);
        assertEquals(1, actions.size());
        assertEquals(Action.BUILD, actions.get(0));
    }

    @Test
    public void nextActions_BuildState_ShouldReturnEnd() {
        List<Action> actions = basicPower.nextActions(TurnState.BUILD);
        assertEquals(1, actions.size());
        assertEquals(Action.END, actions.get(0));
    }

    @Test
    public void checkVictory_NotThirdLevel_ShouldReturnFalse() {
        Cell c = new Cell(0, 0);
        c.getTower().incrementLevel();
        basicPower.lastPositions[0] = c;

        assertFalse(basicPower.checkVictory());
    }

    @Test
    public void checkVictory_ThirdLevel_ShouldReturnTrue() {
        Cell c = new Cell(0, 0);
        c.getTower().incrementLevel();
        c.getTower().incrementLevel();
        c.getTower().incrementLevel();
        basicPower.lastPositions[0] = c;

        assertTrue(basicPower.checkVictory());
    }

    @Test
    public void getPossibleMoves_ShouldReturnValidCells() {
        Point center = new Point(2, 2);
        Worker w = new Worker();
        w.move(center);
        // place worker on the board
        gameBoard.getCell(center).addWorker(w);

        // place neighbors workers
        gameBoard.getCell(new Point(1, 2)).addWorker(new Worker());
        gameBoard.getCell(new Point(3, 3)).addWorker(new Worker());

        // build towers levels
        gameBoard.getCell(new Point(1, 3)).getTower().incrementLevel();
        gameBoard.getCell(new Point(2, 3)).getTower().incrementLevel();
        gameBoard.getCell(new Point(2, 3)).getTower().incrementLevel();

        // build domes
        gameBoard.getCell(new Point(3, 1)).getTower().buildDome();

        // get possible moves
        List<Cell> cells = basicPower.getPossibleMoves(gameBoard, w);

        // convert cells to points
        List<Point> points = cells.stream().map(c -> c.getLocation()).collect(Collectors.toList());

        List<Point> expected = new ArrayList<>(Arrays.asList(
                new Point(1, 1), new Point(2, 1), /*      dome      */
                /*      worker      */ /*   center point   */ new Point(3, 2),
                new Point(1, 3) /*    two levels    */ /*      worker      */
        ));

        assertEquals(expected.size(), points.size());
        assertTrue(expected.containsAll(points));
        assertTrue(points.containsAll(expected));
    }

    @Test
    public void getPossibleBuilds_ShouldReturnValidCells() {
        Point center = new Point(2, 2);
        Worker w = new Worker();
        w.move(center);
        // place worker on the board
        gameBoard.getCell(center).addWorker(w);

        // place neighbors workers
        gameBoard.getCell(new Point(1, 2)).addWorker(new Worker());
        gameBoard.getCell(new Point(3, 3)).addWorker(new Worker());

        // build towers levels
        gameBoard.getCell(new Point(1, 3)).getTower().incrementLevel();
        gameBoard.getCell(new Point(2, 3)).getTower().incrementLevel();
        gameBoard.getCell(new Point(2, 3)).getTower().incrementLevel();
        gameBoard.getCell(new Point(2, 3)).getTower().incrementLevel();
        gameBoard.getCell(new Point(2, 3)).getTower().incrementLevel();

        // build domes
        gameBoard.getCell(new Point(3, 1)).getTower().buildDome();

        // get possible moves
        List<Cell> cells = basicPower.getPossibleBuilds(gameBoard, w);

        // convert cells to points
        List<Point> points = cells.stream().map(c -> c.getLocation()).collect(Collectors.toList());

        List<Point> expected = new ArrayList<>(Arrays.asList(
                new Point(1, 1), new Point(2, 1), /*      dome      */
                /*      worker      */ /*   center point   */ new Point(3, 2),
                new Point(1, 3) /*       dome       */ /*      worker      */
        ));

        assertEquals(expected.size(), points.size());
        assertTrue(expected.containsAll(points));
        assertTrue(points.containsAll(expected));
    }

    @Test
    public void buildInProgress() {
        Cell currentCell = new Cell(0, 0);

        basicPower.setBuildInProgress(currentCell);

        assertEquals(currentCell, basicPower.getBuildInProgress());
    }
}