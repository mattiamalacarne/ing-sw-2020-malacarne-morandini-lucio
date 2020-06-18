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

import static org.junit.Assert.*;

public class MoveAgainDecoratorTest {

    Power power = null;
    Board gameBoard = null;

    @Before
    public void setUp(){
        power = new MoveAgainDecorator( new BasicPower() );
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
    public void nextActions_FirstMove_ShouldReturnSecondMove(){

        Cell start = new Cell(0,1);
        Cell firstMove = new Cell(0, 2);
        Worker worker = new Worker();

        //place worker on the board
        power.setInitialPosition(gameBoard.getCell(start.getLocation()));
        worker.move(start.getLocation());

        //move the worker
        power.moved(gameBoard.getCell(firstMove.getLocation()));
        gameBoard.getCell(firstMove.getLocation()).addWorker(worker);

        List<Action> actionList = power.nextActions(TurnState.MOVE, gameBoard);
        assertEquals(2, actionList.size());
        assertTrue(actionList.contains(Action.MOVE));
        assertTrue(actionList.contains(Action.BUILD));

    }

    @Test
    public void nextActions_FirstMove_SecondMoveNotPossible_ShouldReturnEnd(){

        Cell start = new Cell(0,0);
        Cell firstMove = new Cell(0, 1);
        Worker worker1 = new Worker();
        Worker worker2 = new Worker();

        //place worker on the board
        power.setInitialPosition(gameBoard.getCell(start.getLocation()));
        worker1.move(start.getLocation());

        //move the worker
        power.moved(gameBoard.getCell(firstMove.getLocation()));
        worker1.move(firstMove.getLocation());
        gameBoard.getCell(firstMove.getLocation()).addWorker(worker1);


        //place other player on the board
        gameBoard.getCell(new Point(1,0)).addWorker(worker2);

        //build tower levels
        gameBoard.getCell(new Point(0,2)).getTower().incrementLevel();
        gameBoard.getCell(new Point(0,2)).getTower().incrementLevel();
        gameBoard.getCell(new Point(0,2)).getTower().incrementLevel();
        gameBoard.getCell(new Point(0,2)).getTower().incrementLevel();

        gameBoard.getCell(new Point(1,1)).getTower().buildDome();
        gameBoard.getCell(new Point(1,2)).getTower().buildDome();

        List<Action> actionList = power.nextActions(TurnState.MOVE, gameBoard);

        assertEquals(1, actionList.size());
        assertTrue(actionList.contains(Action.BUILD));
    }

    @Test
    public void nextActions_SecondMove_ShouldReturnBuild(){

        power.setInitialPosition(new Cell(0,0));
        power.moved(new Cell(0,1));
        power.moved(new Cell(0,2));

        List<Action> actionList = power.nextActions(TurnState.MOVE, gameBoard);

        assertEquals(1, actionList.size());
        assertTrue(actionList.contains(Action.BUILD));

    }

    @Test
    public void nextActions_Build_ShouldReturnEnd(){

        power.setInitialPosition(new Cell(0,0));
        power.moved(new Cell(0,1));
        power.moved(new Cell(0,2));
        power.hasBuilt(new Cell(0,3));

        List<Action> actionList = power.nextActions(TurnState.BUILD, gameBoard);

        assertEquals(1, actionList.size());
        assertTrue(actionList.contains(Action.END));

    }

    @Test
    public void getPossibleMoves1_FirstMove_NoOtherPlayerOrBuilds(){

        Cell start = new Cell(0,0);
        Worker worker = new Worker();

        //place worker on the board
        power.setInitialPosition(start);
        gameBoard.getCell(start.getLocation()).addWorker(worker);
        worker.move(start.getLocation());

        //get possible moves
        List<Cell> cellList = power.getPossibleMoves(gameBoard, worker);

        List<Cell> expectedCellList = new ArrayList<>(Arrays.asList(
                new Cell(1,0),
                new Cell(0,1), new Cell(1,1)
        ));

        assertEquals(cellList.size(), expectedCellList.size());
        assertTrue(cellList.containsAll(expectedCellList));
        assertTrue(expectedCellList.containsAll(cellList));

    }

    @Test
    public void getPossibleMoves1_SecondMove_NoOtherPlayerOrBuilds(){

        Cell start = new Cell(0,0);
        Cell first = new Cell(1,0);
        Worker worker = new Worker();

        //place worker on the board
        power.setInitialPosition(start);
        gameBoard.getCell(start.getLocation()).addWorker(worker);
        worker.move(start.getLocation());

        //move the worker
        power.moved(first);
        gameBoard.getCell(start.getLocation()).getWorker().move(first.getLocation());

        //get possible moves
        List<Cell> cellList = power.getPossibleMoves(gameBoard, worker);

        List<Cell> expectedCellList = new ArrayList<>(Arrays.asList(
                new Cell(2,0),
                new Cell(0,1), new Cell(1,1), new Cell(2,1)
        ));

        assertEquals(cellList.size(), expectedCellList.size());
        assertTrue(cellList.containsAll(expectedCellList));
        assertTrue(expectedCellList.containsAll(cellList));

    }

    @Test
    public void getPossibleMoves2_FirstMove_NoOtherPlayerOrBuilds(){

        Cell start = new Cell(1,1);
        Worker worker = new Worker();

        //place worker on the board
        power.setInitialPosition(start);
        gameBoard.getCell(start.getLocation()).addWorker(worker);
        worker.move(start.getLocation());

        //get possible moves
        List<Cell> cellList = power.getPossibleMoves(gameBoard, worker);

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
    public void getPossibleMoves2_SecondMove_NoOtherPlayerOrBuilds(){

        Cell start = new Cell(1,1);
        Cell first = new Cell(2,1);
        Worker worker = new Worker();

        //place worker on the board
        power.setInitialPosition(start);
        gameBoard.getCell(start.getLocation()).addWorker(worker);
        worker.move(start.getLocation());

        //move the worker
        power.moved(first);
        gameBoard.getCell(start.getLocation()).getWorker().move(first.getLocation());

        //get possible moves
        List<Cell> cellList = power.getPossibleMoves(gameBoard, worker);

        List<Cell> expectedCellList = new ArrayList<>(Arrays.asList(
                new Cell(1,0), new Cell(2,0), new Cell(3,0),
                new Cell(3,1),
                new Cell(1,2), new Cell(2,2), new Cell(3,2)
        ));

        assertEquals(cellList.size(), expectedCellList.size());
        assertTrue(cellList.containsAll(expectedCellList));
        assertTrue(expectedCellList.containsAll(cellList));

    }

    @Test
    public void getPossibleMoves3_FirstMove_OtherPlayerAndBuilds(){

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

        //get possible moves
        List<Cell> cellList = power.getPossibleMoves(gameBoard, worker1);

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
    public void getPossibleMoves3_SecondMove_OtherPlayerAndBuilds(){

        Cell start = new Cell(1,2);
        Cell first = new Cell(2,2);
        Worker worker1 = new Worker();
        Worker worker2 = new Worker();

        //place worker on the board
        power.setInitialPosition(start);
        gameBoard.getCell(start.getLocation()).addWorker(worker1);
        worker1.move(start.getLocation());

        //move the worker
        power.moved(first);
        gameBoard.getCell(start.getLocation()).getWorker().move(first.getLocation());

        //place other player on the board
        gameBoard.getCell(new Point(2,1)).addWorker(worker2);

        //build tower levels
        gameBoard.getCell(new Point(1,1)).getTower().incrementLevel();

        gameBoard.getCell(new Point(3,1)).getTower().incrementLevel();
        gameBoard.getCell(new Point(3,1)).getTower().incrementLevel();

        gameBoard.getCell(new Point(2,3)).getTower().buildDome();

        //get possible moves
        List<Cell> cellList = power.getPossibleMoves(gameBoard, worker1);

        List<Cell> expectedCellList = new ArrayList<>(Arrays.asList(
                new Cell(1,1),
                new Cell(3,2),
                new Cell(1,3), new Cell(3,3)
        ));

        assertEquals(cellList.size(), expectedCellList.size());
        assertTrue(cellList.containsAll(expectedCellList));
        assertTrue(expectedCellList.containsAll(cellList));

    }

    @Test
    public void getPossibleMoves4_SecondMoveNotPossible(){

        Cell start = new Cell(0,0);
        Cell firstMove = new Cell(0,1);
        Worker worker1 = new Worker();
        Worker worker2 = new Worker();

        //place worker on the board
        power.setInitialPosition(start);
        gameBoard.getCell(start.getLocation()).addWorker(worker1);
        worker1.move(start.getLocation());

        //move the worker
        power.moved(firstMove);
        gameBoard.getCell(start.getLocation()).getWorker().move(firstMove.getLocation());

        //place other player on the board
        gameBoard.getCell(new Point(1,0)).addWorker(worker2);

        //build tower levels
        gameBoard.getCell(new Point(0,2)).getTower().incrementLevel();
        gameBoard.getCell(new Point(0,2)).getTower().incrementLevel();
        gameBoard.getCell(new Point(0,2)).getTower().incrementLevel();
        gameBoard.getCell(new Point(0,2)).getTower().incrementLevel();

        //builds domes
        gameBoard.getCell(new Point(1,1)).getTower().buildDome();
        gameBoard.getCell(new Point(1,2)).getTower().buildDome();

        //get possible moves
        List<Cell> cellList = power.getPossibleMoves(gameBoard, worker1);

        assertEquals(cellList.size(), 0);
    }


}