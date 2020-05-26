package it.polimi.ingsw.psp12.model.power.cardPower;

import it.polimi.ingsw.psp12.model.Worker;
import it.polimi.ingsw.psp12.model.board.Board;
import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.model.enumeration.Action;
import it.polimi.ingsw.psp12.model.enumeration.TurnState;
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

public class BuildBeforeMoveAgainDecoratorTest {
    Power power = null;
    Board gameBoard = null;

    @Before
    public void setUp() {
        power = new BuildBeforeMoveAgainDecorator(new BasicPower());
        gameBoard = new Board();
    }

    @Test
    public void nextActions_Init_ShouldReturnMoveBuild() {
        // set initial state
        Cell c = new Cell(0, 0);
        Worker w = new Worker();
        w.move(c.getLocation());
        c.addWorker(w);
        power.setInitialPosition(c);

        List<Action> actions = power.nextActions(TurnState.INIT, gameBoard);

        assertEquals(2, actions.size());
        assertTrue(actions.contains(Action.MOVE));
        assertTrue(actions.contains(Action.BUILD));
    }

    @Test
    public void nextActions_Build_ShouldReturnMove() {
        // set initial state
        power.setInitialPosition(new Cell(0, 0));
        power.hasBuilt(new Cell(0, 1));

        List<Action> actions = power.nextActions(TurnState.BUILD, gameBoard);

        assertEquals(1, actions.size());
        assertTrue(actions.contains(Action.MOVE));
    }

    @Test
    public void nextActions_Move_ShouldReturnBuild() {
        // set initial state
        power.setInitialPosition(new Cell(0, 0));
        power.moved(new Cell(1, 0));

        List<Action> actions = power.nextActions(TurnState.MOVE, gameBoard);

        assertEquals(1, actions.size());
        assertTrue(actions.contains(Action.BUILD));
    }

    @Test
    public void nextActions_BuildMove_ShouldReturnBuild() {
        // set initial state
        power.setInitialPosition(new Cell(0, 0));
        power.hasBuilt(new Cell(0, 1));
        power.moved(new Cell(1, 0));

        List<Action> actions = power.nextActions(TurnState.MOVE, gameBoard);

        assertEquals(1, actions.size());
        assertTrue(actions.contains(Action.BUILD));
    }

    @Test
    public void nextActions_MoveBuild_ShouldReturnEnd() {
        // set initial state
        power.setInitialPosition(new Cell(0, 0));
        power.moved(new Cell(1, 0));
        power.hasBuilt(new Cell(0, 2));

        List<Action> actions = power.nextActions(TurnState.BUILD, gameBoard);

        assertEquals(1, actions.size());
        assertTrue(actions.contains(Action.END));
    }

    @Test
    public void nextActions_BuildMoveBuild_ShouldReturnEnd() {
        // set initial state
        power.setInitialPosition(new Cell(0, 0));
        power.hasBuilt(new Cell(0, 1));
        power.moved(new Cell(1, 0));
        power.hasBuilt(new Cell(0, 2));

        List<Action> actions = power.nextActions(TurnState.BUILD, gameBoard);

        assertEquals(1, actions.size());
        assertTrue(actions.contains(Action.END));
    }

    @Test
    public void getPossibleMoves_NotBuild_ShouldReturnAllCells() {
        // set initial state
        Point center = new Point(2, 2);
        Worker w = new Worker(0, "P1", 0);
        w.move(center);
        // place worker on the board
        gameBoard.getCell(center).addWorker(w);
        power.setInitialPosition(new Cell(2, 2));

        // place opponent worker
        gameBoard.getCell(new Point(1, 2)).addWorker(new Worker(0, "P2", 0));

        // build towers levels
        gameBoard.getCell(center).getTower().incrementLevel();

        gameBoard.getCell(new Point(1, 1)).getTower().incrementLevel();

        gameBoard.getCell(new Point(3, 2)).getTower().incrementLevel();
        gameBoard.getCell(new Point(3, 2)).getTower().incrementLevel();

        gameBoard.getCell(new Point(2, 3)).getTower().incrementLevel();
        gameBoard.getCell(new Point(2, 3)).getTower().incrementLevel();

        gameBoard.getCell(new Point(3, 3)).getTower().incrementLevel();
        gameBoard.getCell(new Point(3, 3)).getTower().incrementLevel();
        gameBoard.getCell(new Point(3, 3)).getTower().incrementLevel();

        gameBoard.getCell(new Point(1, 3)).getTower().incrementLevel();

        // build domes
        gameBoard.getCell(new Point(3, 1)).getTower().buildDome();

        // get possible moves
        List<Cell> cells = power.getPossibleMoves(gameBoard, w);

        // convert cells to points
        List<Point> points = cells.stream().map(c -> c.getLocation()).collect(Collectors.toList());

        List<Point> expected = new ArrayList<>(Arrays.asList(
                new Point(1, 1), new Point(2, 1), /*      dome      */
                /*      worker      */ /*   center point   */ new Point(3, 2),
                new Point(1, 3), new Point(2, 3)  /*  three levels   */
        ));

        assertEquals(expected.size(), points.size());
        assertTrue(expected.containsAll(points));
        assertTrue(points.containsAll(expected));
    }

    @Test
    public void getPossibleMoves_BuildBefore_ShouldReturnCellsOnTheSameOrLowerLevel() {
        // set initial state
        Point center = new Point(2, 2);
        Worker w = new Worker(0, "P1", 0);
        w.move(center);
        // place worker on the board
        gameBoard.getCell(center).addWorker(w);
        power.setInitialPosition(gameBoard.getCell(center));

        power.hasBuilt(new Cell(1, 1));

        // place opponent worker
        gameBoard.getCell(new Point(1, 2)).addWorker(new Worker(0, "P2", 0));

        // build towers levels
        gameBoard.getCell(center).getTower().incrementLevel();

        gameBoard.getCell(new Point(1, 1)).getTower().incrementLevel();

        gameBoard.getCell(new Point(3, 2)).getTower().incrementLevel();
        gameBoard.getCell(new Point(3, 2)).getTower().incrementLevel();

        gameBoard.getCell(new Point(2, 3)).getTower().incrementLevel();
        gameBoard.getCell(new Point(2, 3)).getTower().incrementLevel();

        gameBoard.getCell(new Point(3, 3)).getTower().incrementLevel();
        gameBoard.getCell(new Point(3, 3)).getTower().incrementLevel();
        gameBoard.getCell(new Point(3, 3)).getTower().incrementLevel();

        gameBoard.getCell(new Point(1, 3)).getTower().incrementLevel();

        // build domes
        gameBoard.getCell(new Point(3, 1)).getTower().buildDome();

        // get possible moves
        List<Cell> cells = power.getPossibleMoves(gameBoard, w);

        // convert cells to points
        List<Point> points = cells.stream().map(c -> c.getLocation()).collect(Collectors.toList());

        List<Point> expected = new ArrayList<>(Arrays.asList(
                new Point(1, 1), new Point(2, 1), /*      dome      */
                /*      worker      */ /*   center point   */ /*   two levels   */
                new Point(1, 3) /*   two levels    */  /*  three levels   */
        ));

        assertEquals(expected.size(), points.size());
        assertTrue(expected.containsAll(points));
        assertTrue(points.containsAll(expected));
    }

    @Test
    public void turnSimulation1_AllowBuildBeforeWithRestrictions() {
        // set initial state
        Point center = new Point(0, 0);
        Worker w = new Worker(0, "P1", 0);
        w.move(center);
        // place worker on the board
        gameBoard.getCell(center).addWorker(w);
        power.setInitialPosition(gameBoard.getCell(center));

        // place opponent worker
        gameBoard.getCell(new Point(0, 1)).addWorker(new Worker(0, "P2", 0));

        // build towers levels
        gameBoard.getCell(new Point(1, 1)).getTower().incrementLevel();

        // get possible actions
        List<Action> actions = power.nextActions(TurnState.INIT, gameBoard);

        assertEquals(2, actions.size());
        assertTrue(actions.contains(Action.MOVE));
        assertTrue(actions.contains(Action.BUILD));

        // get possible builds
        List<Cell> cells = power.getPossibleBuilds(gameBoard, w);

        // convert cells to points
        List<Point> points = cells.stream().map(c -> c.getLocation()).collect(Collectors.toList());

        List<Point> expected = new ArrayList<>(Arrays.asList(
                /*   center point   */ /*   same level    */
                /*      worker      */ new Point(1, 1)
        ));

        assertEquals(expected.size(), points.size());
        assertTrue(expected.containsAll(points));
        assertTrue(points.containsAll(expected));
    }

    @Test
    public void turnSimulation2_AllowBuildBeforeWithoutRestrictions() {
        // set initial state
        Point center = new Point(0, 0);
        Worker w = new Worker(0, "P1", 0);
        w.move(center);
        // place worker on the board
        gameBoard.getCell(center).addWorker(w);
        power.setInitialPosition(gameBoard.getCell(center));

        // place opponent worker
        gameBoard.getCell(new Point(0, 1)).addWorker(new Worker(0, "P2", 0));

        // build towers levels
        gameBoard.getCell(center).getTower().incrementLevel();

        gameBoard.getCell(new Point(1, 1)).getTower().incrementLevel();
        gameBoard.getCell(new Point(1, 1)).getTower().incrementLevel();

        // get possible actions
        List<Action> actions = power.nextActions(TurnState.INIT, gameBoard);

        assertEquals(2, actions.size());
        assertTrue(actions.contains(Action.MOVE));
        assertTrue(actions.contains(Action.BUILD));

        // get possible builds
        List<Cell> cells = power.getPossibleBuilds(gameBoard, w);

        // convert cells to points
        List<Point> points = cells.stream().map(c -> c.getLocation()).collect(Collectors.toList());

        List<Point> expected = new ArrayList<>(Arrays.asList(
                /*   center point   */ new Point(1, 0),
                /*      worker      */ new Point(1, 1)
        ));

        assertEquals(expected.size(), points.size());
        assertTrue(expected.containsAll(points));
        assertTrue(points.containsAll(expected));
    }

    @Test
    public void turnSimulation3_AllowBuildBeforeWithoutRestrictions() {
        // set initial state
        Point center = new Point(0, 0);
        Worker w = new Worker(0, "P1", 0);
        w.move(center);
        // place worker on the board
        gameBoard.getCell(center).addWorker(w);
        power.setInitialPosition(gameBoard.getCell(center));

        // place opponent worker
        gameBoard.getCell(new Point(0, 1)).addWorker(new Worker(0, "P2", 0));

        // build towers levels
        gameBoard.getCell(center).getTower().incrementLevel();

        // build domes
        gameBoard.getCell(new Point(1, 1)).getTower().buildDome();

        // get possible actions
        List<Action> actions = power.nextActions(TurnState.INIT, gameBoard);

        assertEquals(2, actions.size());
        assertTrue(actions.contains(Action.MOVE));
        assertTrue(actions.contains(Action.BUILD));

        // get possible builds
        List<Cell> cells = power.getPossibleBuilds(gameBoard, w);

        // convert cells to points
        List<Point> points = cells.stream().map(c -> c.getLocation()).collect(Collectors.toList());

        List<Point> expected = new ArrayList<>(Arrays.asList(
                /*   center point   */ new Point(1, 0)
                /*      worker      */ /*      dome      */
        ));

        assertEquals(expected.size(), points.size());
        assertTrue(expected.containsAll(points));
        assertTrue(points.containsAll(expected));
    }

    @Test
    public void turnSimulation4_DoNotAllowBuildBefore() {
        // set initial state
        Point center = new Point(0, 0);
        Worker w = new Worker(0, "P1", 0);
        w.move(center);
        // place worker on the board
        gameBoard.getCell(center).addWorker(w);
        power.setInitialPosition(gameBoard.getCell(center));

        // place opponent worker
        gameBoard.getCell(new Point(0, 1)).addWorker(new Worker(0, "P2", 0));

        // build towers levels
        gameBoard.getCell(new Point(1, 0)).getTower().incrementLevel();

        gameBoard.getCell(new Point(1, 1)).getTower().incrementLevel();

        // get possible actions
        List<Action> actions = power.nextActions(TurnState.INIT, gameBoard);

        assertEquals(1, actions.size());
        assertTrue(actions.contains(Action.MOVE));
    }

    @Test
    public void turnSimulation5_AllowBuildBeforeWithoutRestrictions() {
        // set initial state
        Point center = new Point(3, 3);
        Worker w = new Worker(0, "P1", 0);
        w.move(center);
        // place worker on the board
        gameBoard.getCell(center).addWorker(w);
        power.setInitialPosition(gameBoard.getCell(center));

        // place opponent worker
        gameBoard.getCell(new Point(2, 4)).addWorker(new Worker(0, "P2", 0));

        // build towers levels
        gameBoard.getCell(center).getTower().incrementLevel();
        gameBoard.getCell(center).getTower().incrementLevel();

        gameBoard.getCell(new Point(4, 2)).getTower().incrementLevel();

        gameBoard.getCell(new Point(2, 3)).getTower().incrementLevel();
        gameBoard.getCell(new Point(2, 3)).getTower().incrementLevel();

        gameBoard.getCell(new Point(3, 4)).getTower().incrementLevel();
        gameBoard.getCell(new Point(3, 4)).getTower().incrementLevel();
        gameBoard.getCell(new Point(3, 4)).getTower().incrementLevel();

        gameBoard.getCell(new Point(4, 4)).getTower().incrementLevel();
        gameBoard.getCell(new Point(4, 4)).getTower().incrementLevel();
        gameBoard.getCell(new Point(4, 4)).getTower().incrementLevel();

        // build domes
        gameBoard.getCell(new Point(3, 2)).getTower().buildDome();
        gameBoard.getCell(new Point(4, 3)).getTower().buildDome();

        // get possible actions
        List<Action> actions = power.nextActions(TurnState.INIT, gameBoard);

        assertEquals(2, actions.size());
        assertTrue(actions.contains(Action.MOVE));
        assertTrue(actions.contains(Action.BUILD));

        // get possible builds
        List<Cell> cells = power.getPossibleBuilds(gameBoard, w);

        // convert cells to points
        List<Point> points = cells.stream().map(c -> c.getLocation()).collect(Collectors.toList());

        List<Point> expected = new ArrayList<>(Arrays.asList(
                new Point(2, 2), /*       dome       */ new Point(4, 2),
                new Point(2, 3), /*   center point   */ /*      dome      */
                /*      worker      */ new Point(3, 4), new Point(4, 4)
        ));

        assertEquals(expected.size(), points.size());
        assertTrue(expected.containsAll(points));
        assertTrue(points.containsAll(expected));
    }

    @Test
    public void turnSimulation6_DoNotAllowBuildBefore() {
        // set initial state
        Point center = new Point(0, 0);
        Worker w = new Worker(0, "P1", 0);
        w.move(center);
        // place worker on the board
        gameBoard.getCell(center).addWorker(w);
        power.setInitialPosition(gameBoard.getCell(center));

        // build domes
        gameBoard.getCell(new Point(0, 1)).getTower().buildDome();
        gameBoard.getCell(new Point(1, 1)).getTower().buildDome();

        // get possible actions
        List<Action> actions = power.nextActions(TurnState.INIT, gameBoard);

        assertEquals(1, actions.size());
        assertTrue(actions.contains(Action.MOVE));
    }

    @Test
    public void turnSimulation7_AllowBuildBeforeWithRestrictions() {
        // set initial state
        Point center = new Point(3, 4);
        Worker w = new Worker(0, "P1", 0);
        w.move(center);
        // place worker on the board
        gameBoard.getCell(center).addWorker(w);
        power.setInitialPosition(gameBoard.getCell(center));

        // build towers levels
        gameBoard.getCell(new Point(3, 3)).getTower().incrementLevel();

        gameBoard.getCell(new Point(4, 3)).getTower().incrementLevel();

        // build domes
        gameBoard.getCell(new Point(2, 4)).getTower().buildDome();
        gameBoard.getCell(new Point(4, 4)).getTower().buildDome();

        // get possible actions
        List<Action> actions = power.nextActions(TurnState.INIT, gameBoard);

        assertEquals(2, actions.size());
        assertTrue(actions.contains(Action.MOVE));
        assertTrue(actions.contains(Action.BUILD));

        // get possible builds
        List<Cell> cells = power.getPossibleBuilds(gameBoard, w);

        // convert cells to points
        List<Point> points = cells.stream().map(c -> c.getLocation()).collect(Collectors.toList());

        List<Point> expected = new ArrayList<>(Arrays.asList(
                /*    same level    */ new Point(3, 3), new Point(4, 3)
                /*       dome       */ /*   center point   */ /*      dome      */
        ));

        assertEquals(expected.size(), points.size());
        assertTrue(expected.containsAll(points));
        assertTrue(points.containsAll(expected));
    }

    @Test
    public void turnSimulation8_AllowBuildBeforeWithoutRestrictions() {
        // set initial state
        Point center = new Point(0, 1);
        Worker w = new Worker(0, "P1", 0);
        w.move(center);
        // place worker on the board
        gameBoard.getCell(center).addWorker(w);
        power.setInitialPosition(gameBoard.getCell(center));

        // build towers levels
        gameBoard.getCell(center).getTower().incrementLevel();

        gameBoard.getCell(new Point(0, 0)).getTower().incrementLevel();

        gameBoard.getCell(new Point(0, 2)).getTower().incrementLevel();

        gameBoard.getCell(new Point(1, 1)).getTower().incrementLevel();
        gameBoard.getCell(new Point(1, 1)).getTower().incrementLevel();

        // build domes
        gameBoard.getCell(new Point(1, 0)).getTower().buildDome();
        gameBoard.getCell(new Point(1, 2)).getTower().buildDome();

        // get possible actions
        List<Action> actions = power.nextActions(TurnState.INIT, gameBoard);

        assertEquals(2, actions.size());
        assertTrue(actions.contains(Action.MOVE));
        assertTrue(actions.contains(Action.BUILD));

        // get possible builds
        List<Cell> cells = power.getPossibleBuilds(gameBoard, w);

        // convert cells to points
        List<Point> points = cells.stream().map(c -> c.getLocation()).collect(Collectors.toList());

        List<Point> expected = new ArrayList<>(Arrays.asList(
                new Point(0, 0), /*      dome      */
                /*   center point   */ new Point(1, 1),
                new Point(0, 2) /*      dome      */
        ));

        assertEquals(expected.size(), points.size());
        assertTrue(expected.containsAll(points));
        assertTrue(points.containsAll(expected));
    }

    @Test
    public void turnSimulation9_AllowBuildBeforeWithoutRestrictions() {
        // set initial state
        Point center = new Point(0, 0);
        Worker w = new Worker(0, "P1", 0);
        w.move(center);
        // place worker on the board
        gameBoard.getCell(center).addWorker(w);
        power.setInitialPosition(gameBoard.getCell(center));

        // build towers levels
        gameBoard.getCell(new Point(1, 0)).getTower().incrementLevel();

        // get possible actions
        List<Action> actions = power.nextActions(TurnState.INIT, gameBoard);

        assertEquals(2, actions.size());
        assertTrue(actions.contains(Action.MOVE));
        assertTrue(actions.contains(Action.BUILD));

        // get possible builds
        List<Cell> cells = power.getPossibleBuilds(gameBoard, w);

        // convert cells to points
        List<Point> points = cells.stream().map(c -> c.getLocation()).collect(Collectors.toList());

        List<Point> expected = new ArrayList<>(Arrays.asList(
                /*   center point   */ new Point(1, 0),
                new Point(0, 1), new Point(1, 1)
        ));

        assertEquals(expected.size(), points.size());
        assertTrue(expected.containsAll(points));
        assertTrue(points.containsAll(expected));
    }
}
