package it.polimi.ingsw.psp12.model;

import it.polimi.ingsw.psp12.exceptions.InvalidMaxPlayersException;
import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.model.enumeration.Action;
import it.polimi.ingsw.psp12.model.enumeration.TurnState;
import it.polimi.ingsw.psp12.utils.Color;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class GameStateTest {
    private GameState gameState2, gameState3;

    @Before
    public void setUp() {
        try {
            gameState2 = new GameState(2);
            gameState3 = new GameState(3);
        }
        catch (InvalidMaxPlayersException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = InvalidMaxPlayersException.class)
    public void Board_InvalidCount_ShouldThrowException() throws InvalidMaxPlayersException {
        GameState gameState4 = new GameState(4);
    }

    @Test
    public void getGameBoard_ShouldReturnBoard() {
        assertNotNull(gameState2.getGameBoard());
    }

    @Test
    public void updateCurrentState_ActionMove_ShouldSetStateMove() {
        // initialize state
        gameState2.initGame();
        // check initial state
        assertEquals(TurnState.INIT, gameState2.getCurrentState());

        gameState2.updateCurrentState(Action.MOVE);

        // check final state
        assertEquals(TurnState.MOVE, gameState2.getCurrentState());
    }

    @Test
    public void updateCurrentState_ActionBuild_ShouldSetStateBuild() {
        // initialize state
        gameState2.initGame();
        // check initial state
        assertEquals(TurnState.INIT, gameState2.getCurrentState());

        gameState2.updateCurrentState(Action.BUILD);

        // check final state
        assertEquals(TurnState.BUILD, gameState2.getCurrentState());
    }

    @Test
    public void addPlayer_ArrayNotFull_ShouldAddPlayer() {
        // check initial state
        assertNull(gameState2.getPlayers()[0]);
        assertNull(gameState2.getPlayers()[1]);

        gameState2.addPlayer("P1");

        // check final state
        assertNotNull(gameState2.getPlayers()[0]);
        assertEquals(0, gameState2.getPlayers()[0].getId());
        assertEquals("P1", gameState2.getPlayers()[0].getName());
        assertNull(gameState2.getPlayers()[1]);
    }

    @Test
    public void nextTurn_NotLast_ShouldIncrementIndex() {
        gameState2.addPlayer("P1");
        gameState2.addPlayer("P2");

        // check initial state
        assertEquals("P1", gameState2.getCurrentPlayer().getName());

        gameState2.nextTurn();

        // check final state
        assertEquals("P2", gameState2.getCurrentPlayer().getName());
    }

    @Test
    public void nextTurn_Last_ShouldResetIndex() {
        gameState3.addPlayer("P1");
        gameState3.addPlayer("P2");
        gameState3.addPlayer("P3");

        // check initial state
        assertEquals("P1", gameState3.getCurrentPlayer().getName());

        gameState3.nextTurn();

        // check intermediate state
        assertEquals("P2", gameState3.getCurrentPlayer().getName());

        gameState3.nextTurn();

        // check intermediate state
        assertEquals("P3", gameState3.getCurrentPlayer().getName());

        gameState3.nextTurn();

        // check final state
        assertEquals("P1", gameState3.getCurrentPlayer().getName());
    }

    @Test
    public void getPlayers_ShouldReturnPlayers() {
        gameState2.addPlayer("P1");
        gameState2.addPlayer("P2");

        Player ps[] = gameState2.getPlayers();

        assertEquals(2, ps.length);

        assertEquals(0, ps[0].getId());
        assertEquals("P1", ps[0].getName());

        assertEquals(1, ps[1].getId());
        assertEquals("P2", ps[1].getName());
    }

    @Test
    public void getCurrentPlayer_ShouldReturnCurrentPlayer() {
        Player p1 = gameState2.addPlayer("P1");
        Player p2 = gameState2.addPlayer("P2");
        gameState2.initGame();

        assertEquals(p1, gameState2.getCurrentPlayer());

        gameState2.nextTurn();

        assertEquals(p2, gameState2.getCurrentPlayer());
    }

    @Test
    public void getWaitingPlayers_ShouldReturnWaitingPlayers() {
        gameState3.addPlayer("P1");
        gameState3.addPlayer("P2");
        gameState3.addPlayer("P3");

        // move to second player
        gameState3.nextTurn();

        // check current player
        assertEquals(1, gameState3.getCurrentPlayer().getId());
        assertEquals("P2", gameState3.getCurrentPlayer().getName());

        // check waiting players
        Player ps[] = gameState3.getWaitingPlayers();

        assertEquals(2, ps.length);

        assertEquals(0, ps[0].getId());
        assertEquals("P1", ps[0].getName());

        assertEquals(2, ps[1].getId());
        assertEquals("P3", ps[1].getName());
    }

    @Test
    public void getPreviousPlayer_NotFirst_ShouldReturnPreviousPlayer() {
        gameState2.addPlayer("P1");
        gameState2.addPlayer("P2");

        // move to second player
        gameState2.nextTurn();

        // check current player
        assertEquals(1, gameState2.getCurrentPlayer().getId());
        assertEquals("P2", gameState2.getCurrentPlayer().getName());

        // check previous player
        assertEquals(0, gameState2.getPreviousPlayer().getId());
        assertEquals("P1", gameState2.getPreviousPlayer().getName());
    }

    @Test
    public void getPreviousPlayer_First_ShouldReturnPreviousPlayer() {
        gameState3.addPlayer("P1");
        gameState3.addPlayer("P2");
        gameState3.addPlayer("P3");

        // check current player
        assertEquals(0, gameState3.getCurrentPlayer().getId());
        assertEquals("P1", gameState3.getCurrentPlayer().getName());

        // check previous player
        assertEquals(2, gameState3.getPreviousPlayer().getId());
        assertEquals("P3", gameState3.getPreviousPlayer().getName());
    }

    @Test
    public void alreadyRegistered_NewName_ShouldReturnFalse() {
        gameState2.addPlayer("Pippo");
        assertFalse(gameState2.alreadyRegistered("Pluto"));
    }

    @Test
    public void alreadyRegistered_NameUsed_ShouldReturnTrue() {
        gameState2.addPlayer("Pippo");
        assertTrue(gameState2.alreadyRegistered("Pippo"));
    }

    @Test
    public void initGame_ShouldResetTurnState() {
        gameState2.initGame();
        assertEquals(TurnState.INIT, gameState2.getCurrentState());
    }

    @Test
    public void setPlayerInfo_ShouldInitPlayer() {
        // initialize state with a player
        gameState2.addPlayer("Test");
        gameState2.initGame();

        // check initial state
        Color initialColors[] = Color.values();
        assertArrayEquals(initialColors, gameState2.getAvailableColors().toArray());

        assertNull(gameState2.getCurrentPlayer().getPower());
        assertFalse(gameState2.getCurrentPlayer().isInitialized());

        // set player info
        Point points[] = new Point[] { new Point(0, 0), new Point(2, 1) };
        gameState2.setPlayerInfo(Color.BLUE, points);

        // check final state
        Color finalColors[] = new Color[Color.values().length - 1];
        int i = 0;
        for (Color c : Color.values()) {
            if (!c.equals(Color.BLUE)) {
                finalColors[i] = c;
                i++;
            }
        }

        assertArrayEquals(finalColors, gameState2.getAvailableColors().toArray());

        assertNotNull(gameState2.getCurrentPlayer().getPower());
        assertTrue(gameState2.getCurrentPlayer().isInitialized());
        assertEquals(1, gameState2.getCurrentPlayer().getPower().getMaxClimbLevel());
        assertEquals(3, gameState2.getCurrentPlayer().getPower().getMinDomeLevel());

        Worker w1 = gameState2.getCurrentPlayer().getWorkerByIndex(0);
        assertEquals(points[0], w1.getPosition());
        assertEquals(Color.BLUE, w1.getColor());
        assertTrue(gameState2.getGameBoard().getCell(points[0]).hasWorker());

        Worker w2 = gameState2.getCurrentPlayer().getWorkerByIndex(1);
        assertEquals(points[1], w2.getPosition());
        assertEquals(Color.BLUE, w2.getColor());
        assertTrue(gameState2.getGameBoard().getCell(points[1]).hasWorker());
    }

    @Test
    public void isInitialized_ShouldReturnTrueAfterAllPlayersAreReady() {
        // initialize state
        gameState2.addPlayer("Pippo");
        gameState2.addPlayer("Pluto");
        gameState2.initGame();

        // set first player info
        Point points1[] = new Point[] { new Point(0, 0), new Point(2, 1) };
        gameState2.setPlayerInfo(Color.BLUE, points1);

        // check intermediate state
        assertFalse(gameState2.isInitialized());

        // move to second player
        gameState2.nextTurn();

        // set second player info
        Point points2[] = new Point[] { new Point(1, 3), new Point(0, 2) };
        gameState2.setPlayerInfo(Color.RED, points2);

        // check final state
        assertTrue(gameState2.isInitialized());
    }

    @Test
    public void selectCurrentWorker_ShouldReturnSelectedWorker() {
        Player p = gameState2.addPlayer("P1");
        gameState2.selectCurrentWorker(1);

        assertEquals(p.getWorkerByIndex(1), p.getCurrentWorker());
    }

    @Test
    public void initTurn_ShouldInitTurn() {
        // initialize player and game
        gameState2.addPlayer("P1");
        gameState2.addPlayer("P2");
        gameState2.initGame();
        gameState2.setPlayerInfo(Color.RED, new Point[] { new Point(0, 0), new Point(1, 2)});
        gameState2.nextTurn();
        gameState2.setPlayerInfo(Color.BLUE, new Point[] { new Point(1, 0), new Point(2, 3)});
        gameState2.initGame();

        // init turn
        gameState2.initTurn();

        // check final state
        assertEquals(TurnState.INIT, gameState2.getCurrentState());
        assertEquals(1, gameState2.getCurrentPlayer().getPower().getMaxClimbLevel());
    }

    @Test
    public void nextActions_ShouldReturnPossibleActions() {
        gameState2.addPlayer("P1");
        gameState2.setPlayerInfo(Color.RED, new Point[] { new Point(0, 0), new Point(1, 2)});
        gameState2.initGame();

        List<Action> actions = gameState2.nextActions();
        assertEquals(1, actions.size());
        assertEquals(Action.MOVE, actions.get(0));
    }

    @Test
    public void checkVictory_BuildState_ShouldReturnFalse() {
        gameState2.updateCurrentState(Action.BUILD);
        assertFalse(gameState2.checkVictory());
    }

    @Test
    public void checkVictory_MoveState_ShouldCheckVictory() {
        gameState2.addPlayer("P1");
        gameState2.setPlayerInfo(Color.RED, new Point[] { new Point(0, 0), new Point(1, 2)});
        gameState2.selectCurrentWorker(0);
        gameState2.initGame();

        gameState2.move(new Point(0, 1));
        gameState2.updateCurrentState(Action.MOVE);
        assertFalse(gameState2.checkVictory());
    }

    @Test
    public void move_ShouldMoveWorker() {
        Player p1 = gameState2.addPlayer("P1");
        gameState2.addPlayer("P2");
        gameState2.initGame();
        gameState2.setPlayerInfo(Color.RED, new Point[] { new Point(0, 0), new Point(1, 2)});
        gameState2.initGame();
        gameState2.selectCurrentWorker(0);

        Point oldP = new Point(0, 0);
        Point newP = new Point(0, 1);

        // check initial state
        assertEquals(oldP, p1.getWorkerByIndex(0).getPosition());
        assertTrue(gameState2.getGameBoard().getCell(oldP).hasWorker());
        assertFalse(gameState2.getGameBoard().getCell(newP).hasWorker());

        // move
        gameState2.move(newP);

        // check final state
        assertEquals(newP, p1.getWorkerByIndex(0).getPosition());
        assertTrue(gameState2.getGameBoard().getCell(newP).hasWorker());
        assertFalse(gameState2.getGameBoard().getCell(oldP).hasWorker());
    }

    @Test
    public void build_ShouldBuildWorker() {
        Player p1 = gameState2.addPlayer("P1");
        gameState2.addPlayer("P2");
        gameState2.initGame();
        gameState2.setPlayerInfo(Color.RED, new Point[] { new Point(0, 0), new Point(1, 2)});
        gameState2.initGame();
        gameState2.selectCurrentWorker(0);

        Point p = new Point(3, 2);

        // check initial state
        assertEquals(0, gameState2.getGameBoard().getCell(p).getTower().getLevel());

        // build
        gameState2.build(p);

        // check final state
        assertEquals(1, gameState2.getGameBoard().getCell(p).getTower().getLevel());
    }

    @Test
    public void getPossibleMoves_ShouldReturnCurrentPlayerMoves() {
        Player p1 = gameState2.addPlayer("P1");
        gameState2.addPlayer("P2");
        gameState2.setPlayerInfo(Color.RED, new Point[] { new Point(0, 0), new Point(1, 2)});
        gameState2.nextTurn();
        gameState2.setPlayerInfo(Color.BLUE, new Point[] { new Point(1, 3), new Point(3, 0)});

        gameState2.initGame();
        gameState2.selectCurrentWorker(1);

        List<Cell> moves = gameState2.getPossibleMoves();

        List<Cell> expected = p1.getPower().getPossibleMoves(gameState2.getGameBoard(), p1.getWorkerByIndex(1));

        assertEquals(expected.size(), moves.size());
        assertTrue(expected.containsAll(moves));
        assertTrue(moves.containsAll(expected));
    }

    @Test
    public void getPossibleBuilds_ShouldReturnCurrentPlayerBuilds() {
        Player p1 = gameState2.addPlayer("P1");
        gameState2.addPlayer("P2");
        gameState2.setPlayerInfo(Color.RED, new Point[] { new Point(0, 0), new Point(1, 2)});
        gameState2.nextTurn();
        gameState2.setPlayerInfo(Color.BLUE, new Point[] { new Point(1, 3), new Point(3, 0)});

        gameState2.initGame();
        gameState2.selectCurrentWorker(1);

        List<Cell> moves = gameState2.getPossibleBuilds();

        List<Cell> expected = p1.getPower().getPossibleBuilds(gameState2.getGameBoard(), p1.getWorkerByIndex(1));

        assertEquals(expected.size(), moves.size());
        assertTrue(expected.containsAll(moves));
        assertTrue(moves.containsAll(expected));
    }
}
