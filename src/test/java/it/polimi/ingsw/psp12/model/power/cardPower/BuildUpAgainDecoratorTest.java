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

public class BuildUpAgainDecoratorTest {
    Power power = null;
    Board gameBoard = null;

    @Before
    public void setUp() {
        power = new BuildUpAgainDecorator(new BasicPower());
        gameBoard = new Board();
    }

    @Test
    public void nextActions_Init_ShouldReturnMove() {
        // set initial state
        power.setInitialPosition(new Cell(0, 0));

        List<Action> actions = power.nextActions(TurnState.INIT, gameBoard);

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
    public void nextActions_FirstBuild_SecondPossible_ShouldReturnBuildEnd() {
        // set initial state
        power.setInitialPosition(new Cell(0, 0));
        power.moved(new Cell(1, 0));
        Cell build = new Cell(0, 1);
        build.getTower().incrementLevel();
        build.getTower().incrementLevel();
        power.hasBuilt(build);

        List<Action> actions = power.nextActions(TurnState.BUILD, gameBoard);

        assertEquals(2, actions.size());
        assertTrue(actions.contains(Action.BUILD));
        assertTrue(actions.contains(Action.END));
    }

    @Test
    public void nextActions_FirstBuild_SecondNotPossible_ShouldReturnEnd() {
        // set initial state
        power.setInitialPosition(new Cell(0, 0));
        power.moved(new Cell(1, 0));
        Cell build = new Cell(0, 1);
        build.getTower().incrementLevel();
        build.getTower().incrementLevel();
        build.getTower().incrementLevel();
        power.hasBuilt(build);

        List<Action> actions = power.nextActions(TurnState.BUILD, gameBoard);

        assertEquals(1, actions.size());
        assertTrue(actions.contains(Action.END));
    }

    @Test
    public void nextActions_SecondBuild_ShouldReturnEnd() {
        // set initial state
        power.setInitialPosition(new Cell(0, 0));
        power.moved(new Cell(1, 0));
        power.hasBuilt(new Cell(0, 1));
        power.hasBuilt(new Cell(0, 2));

        List<Action> actions = power.nextActions(TurnState.BUILD, gameBoard);

        assertEquals(1, actions.size());
        assertTrue(actions.contains(Action.END));
    }

    @Test
    public void getPossibleBuilds_FirstBuild_ShouldReturnValidCells() {
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
        List<Cell> cells = power.getPossibleBuilds(gameBoard, w);

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
    public void getPossibleBuilds_SecondBuild_ShouldReturnCellOfFirstBuild() {
        Point center = new Point(2, 2);
        Worker w = new Worker();
        w.move(center);

        Point build = new Point(1, 3);
        power.hasBuilt(gameBoard.getCell(build));

        // place worker on the board
        gameBoard.getCell(center).addWorker(w);

        // place neighbors workers
        gameBoard.getCell(new Point(1, 2)).addWorker(new Worker());
        gameBoard.getCell(new Point(3, 3)).addWorker(new Worker());

        // build towers levels
        gameBoard.getCell(build).getTower().incrementLevel();
        gameBoard.getCell(new Point(2, 3)).getTower().incrementLevel();
        gameBoard.getCell(new Point(2, 3)).getTower().incrementLevel();
        gameBoard.getCell(new Point(2, 3)).getTower().incrementLevel();
        gameBoard.getCell(new Point(2, 3)).getTower().incrementLevel();

        // build domes
        gameBoard.getCell(new Point(3, 1)).getTower().buildDome();

        // get possible moves
        List<Cell> cells = power.getPossibleBuilds(gameBoard, w);

        // convert cells to points
        List<Point> points = cells.stream().map(c -> c.getLocation()).collect(Collectors.toList());


        assertEquals(1, points.size());
        assertTrue(points.contains(build));
    }
}
