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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BuildAgainDecoratorTest {

    Power power = null;
    Board gameBoard = null;

    @Before
    public void setUp(){
        power = new BuildAgainDecorator(new BasicPower());
        gameBoard = new Board();
    }

    @Test
    public void nextActions_Init_ShouldReturnMove(){

        power.setInitialPosition(new Cell(0,0));

        List<Action> actionList = power.nextActions(TurnState.INIT, gameBoard);

        assertEquals(1, actionList.size());
        assertTrue(actionList.contains(Action.MOVE));

    }

    @Test
    public void nextActions_Move_ShouldReturnFirstBuild(){

        power.setInitialPosition(new Cell(0,0));
        power.moved(new Cell(0,1));

        List<Action> actionList = power.nextActions(TurnState.MOVE, gameBoard);

        assertEquals(1, actionList.size());
        assertTrue(actionList.contains(Action.BUILD));

    }

    @Test
    public void nextActions_FirstBuild_ShouldReturnSecondBuild(){

        Cell start = new Cell(0,1);
        Cell firstBuild = new Cell(0, 2);
        Worker worker = new Worker();

        //place worker on the board
        gameBoard.getCell(start.getLocation()).addWorker(worker);
        power.setInitialPosition(gameBoard.getCell(start.getLocation()));
        worker.move(start.getLocation());

        //the worker does a build
        power.hasBuilt(firstBuild);
        gameBoard.getCell(firstBuild.getLocation()).getTower().incrementLevel();

        List<Action> actionList = power.nextActions(TurnState.BUILD, gameBoard);

        assertEquals(2, actionList.size());
        assertTrue(actionList.contains(Action.BUILD));
        assertTrue(actionList.contains(Action.END));

    }

    @Test
    public void nextActions_FirstBuild_SecondBuildNotPossible_ShouldReturnEnd(){

        Cell start = new Cell(0,1);
        Cell firstBuild = new Cell(0, 2);
        Worker worker1 = new Worker();
        Worker worker2 = new Worker();

        //place worker on the board
        gameBoard.getCell(start.getLocation()).addWorker(worker1);
        power.setInitialPosition(gameBoard.getCell(start.getLocation()));
        worker1.move(start.getLocation());

        //the worker does a build
        power.hasBuilt(firstBuild);
        gameBoard.getCell(firstBuild.getLocation()).getTower().incrementLevel();

        //place other player on the board
        gameBoard.getCell(new Point(1,0)).addWorker(worker2);

        //build tower levels
        gameBoard.getCell(new Point(0,0)).getTower().incrementLevel();
        gameBoard.getCell(new Point(0,0)).getTower().incrementLevel();
        gameBoard.getCell(new Point(0,0)).getTower().incrementLevel();
        gameBoard.getCell(new Point(0,0)).getTower().incrementLevel();

        gameBoard.getCell(new Point(1,1)).getTower().buildDome();
        gameBoard.getCell(new Point(1,2)).getTower().buildDome();

        List<Action> actionList = power.nextActions(TurnState.BUILD, gameBoard);

        assertEquals(1, actionList.size());
        assertTrue(actionList.contains(Action.END));

    }

    @Test
    public void nextActions_SecondBuild_ShouldReturnEnd(){

        power.setInitialPosition(new Cell(0,0));
        power.moved(new Cell(0,1));
        power.hasBuilt(new Cell(0,2));
        power.hasBuilt(new Cell(0,3));

        List<Action> actionList = power.nextActions(TurnState.BUILD, gameBoard);

        assertEquals(1, actionList.size());
        assertTrue(actionList.contains(Action.END));

    }

    @Test
    public void getPossibleBuilds1_FirstBuild_NoOtherPlayerOrBuilds(){

        Cell start = new Cell(0,0);
        Worker worker = new Worker();

        //place worker on the board
        power.setInitialPosition(start);
        gameBoard.getCell(start.getLocation()).addWorker(worker);
        worker.move(start.getLocation());

        //get possible builds
        List<Cell> cellList = power.getPossibleBuilds(gameBoard, worker);

        List<Cell> expectedCellList = new ArrayList<>(Arrays.asList(
                new Cell(1,0),
                new Cell(0,1), new Cell(1,1)
        ));

        assertEquals(cellList.size(), expectedCellList.size());
        assertTrue(cellList.containsAll(expectedCellList));
        assertTrue(expectedCellList.containsAll(cellList));

    }

    @Test
    public void getPossibleBuilds1_SecondBuild_NoOtherPlayerOrBuilds(){

        Cell start = new Cell(0,0);
        Cell first = new Cell(1,0);
        Worker worker = new Worker();

        //place worker on the board
        power.setInitialPosition(start);
        gameBoard.getCell(start.getLocation()).addWorker(worker);
        worker.move(start.getLocation());

        //the worker does a build
        power.hasBuilt(first);
        gameBoard.getCell(first.getLocation()).getTower().incrementLevel();

        //get possible builds
        List<Cell> cellList = power.getPossibleBuilds(gameBoard, worker);

        List<Cell> expectedCellList = new ArrayList<>(Arrays.asList(
                new Cell(0,1), new Cell(1,1)
        ));

        assertEquals(cellList.size(), expectedCellList.size());
        assertTrue(cellList.containsAll(expectedCellList));
        assertTrue(expectedCellList.containsAll(cellList));

    }

    @Test
    public void getPossibleBuild2_FirstBuild_NoOtherPlayerOrBuilds(){

        Cell start = new Cell(1,1);
        Worker worker = new Worker();

        //place worker on the board
        power.setInitialPosition(start);
        gameBoard.getCell(start.getLocation()).addWorker(worker);
        worker.move(start.getLocation());

        //get possible build
        List<Cell> cellList = power.getPossibleBuilds(gameBoard, worker);

        List<Cell> expectedCellList = new ArrayList<>(Arrays.asList(
                new Cell(0,0), new Cell(1,0), new Cell(2,0),
                new Cell(0,1), new Cell(2,1),
                new Cell(0,2), new Cell(1,2), new Cell(2,2)
        ));

        assertEquals(cellList.size(), expectedCellList.size());
        assertTrue(cellList.containsAll(expectedCellList));
        assertTrue(expectedCellList.containsAll(cellList));

    }

    @Test
    public void getPossibleBuild2_SecondBuild_NoOtherPlayerOrBuilds(){

        Cell start = new Cell(1,1);
        Cell first = new Cell(2,1);
        Worker worker = new Worker();

        //place worker on the board
        power.setInitialPosition(start);
        gameBoard.getCell(start.getLocation()).addWorker(worker);
        worker.move(start.getLocation());

        //the worker does a build
        power.hasBuilt(first);
        gameBoard.getCell(first.getLocation()).getTower().incrementLevel();

        //get possible builds
        List<Cell> cellList = power.getPossibleBuilds(gameBoard, worker);

        List<Cell> expectedCellList = new ArrayList<>(Arrays.asList(
                new Cell(0,0), new Cell(1,0), new Cell(2,0),
                new Cell(0,1),
                new Cell(0,2), new Cell(1,2), new Cell(2,2)
        ));

        assertEquals(cellList.size(), expectedCellList.size());
        assertTrue(cellList.containsAll(expectedCellList));
        assertTrue(expectedCellList.containsAll(cellList));

    }

    @Test
    public void getPossibleBuild3_FirstBuild_OtherPlayerOrBuilds(){

        Cell start = new Cell(1,2);
        Worker worker1 = new Worker();
        Worker worker2 = new Worker();

        //place worker on the board
        power.setInitialPosition(start);
        gameBoard.getCell(start.getLocation()).addWorker(worker1);
        worker1.move(start.getLocation());

        //place other player on the board
        gameBoard.getCell(new Point(2,1)).addWorker(worker2);

        //build tower levels
        gameBoard.getCell(new Point(1,1)).getTower().incrementLevel();

        gameBoard.getCell(new Point(3,1)).getTower().incrementLevel();
        gameBoard.getCell(new Point(3,1)).getTower().incrementLevel();

        gameBoard.getCell(new Point(2,3)).getTower().buildDome();

        //get possible builds
        List<Cell> cellList = power.getPossibleBuilds(gameBoard, worker1);

        List<Cell> expectedCellList = new ArrayList<>(Arrays.asList(
                new Cell(0,1), new Cell(1,1),
                new Cell(0,2), new Cell(2,2),
                new Cell(0,3), new Cell(1,3)
        ));

        assertEquals(cellList.size(), expectedCellList.size());
        assertTrue(cellList.containsAll(expectedCellList));
        assertTrue(expectedCellList.containsAll(cellList));

    }

    @Test
    public void getPossibleBuild3_SecondBuild_OtherPlayerOrBuilds(){

        Cell start = new Cell(1,2);
        Cell first = new Cell(2,2);
        Worker worker1 = new Worker();
        Worker worker2 = new Worker();

        //place worker on the board
        power.setInitialPosition(start);
        gameBoard.getCell(start.getLocation()).addWorker(worker1);
        worker1.move(start.getLocation());

        //the worker does a build
        power.hasBuilt(first);
        gameBoard.getCell(first.getLocation()).getTower().incrementLevel();

        //place other player on the board
        gameBoard.getCell(new Point(2,1)).addWorker(worker2);

        //build tower levels
        gameBoard.getCell(new Point(1,1)).getTower().incrementLevel();

        gameBoard.getCell(new Point(3,1)).getTower().incrementLevel();
        gameBoard.getCell(new Point(3,1)).getTower().incrementLevel();

        gameBoard.getCell(new Point(2,3)).getTower().buildDome();

        //get possible builds
        List<Cell> cellList = power.getPossibleBuilds(gameBoard, worker1);

        List<Cell> expectedCellList = new ArrayList<>(Arrays.asList(
                new Cell(0,1), new Cell(1,1),
                new Cell(0,2),
                new Cell(0,3), new Cell(1,3)
        ));

        assertEquals(cellList.size(), expectedCellList.size());
        assertTrue(cellList.containsAll(expectedCellList));
        assertTrue(expectedCellList.containsAll(cellList));

    }

    @Test
    public void getPossibleBuild4_SecondBuildNotPossible(){

        Cell start = new Cell(1,2);
        Cell first = new Cell(2,2);
        Worker worker1 = new Worker();
        Worker worker2 = new Worker();

        //place worker on the board
        power.setInitialPosition(start);
        gameBoard.getCell(start.getLocation()).addWorker(worker1);
        worker1.move(start.getLocation());

        //the worker does a build
        power.hasBuilt(first);
        gameBoard.getCell(first.getLocation()).getTower().incrementLevel();

        //place other player on the board
        gameBoard.getCell(new Point(2,1)).addWorker(worker2);

        //build tower levels
        gameBoard.getCell(new Point(1,1)).getTower().incrementLevel();
        gameBoard.getCell(new Point(1,1)).getTower().incrementLevel();
        gameBoard.getCell(new Point(1,1)).getTower().incrementLevel();
        gameBoard.getCell(new Point(1,1)).getTower().incrementLevel();

        gameBoard.getCell(new Point(3,1)).getTower().incrementLevel();
        gameBoard.getCell(new Point(3,1)).getTower().incrementLevel();

        //build domes
        gameBoard.getCell(new Point(0,1)).getTower().buildDome();
        gameBoard.getCell(new Point(0,2)).getTower().buildDome();
        gameBoard.getCell(new Point(0,3)).getTower().buildDome();
        gameBoard.getCell(new Point(1,3)).getTower().buildDome();
        gameBoard.getCell(new Point(2,3)).getTower().buildDome();

        //get possible builds
        List<Cell> cellList = power.getPossibleBuilds(gameBoard, worker1);

        assertEquals(cellList.size(), 0);

    }

}