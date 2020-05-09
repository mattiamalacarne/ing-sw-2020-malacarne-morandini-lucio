package it.polimi.ingsw.psp12.model;

import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.model.enumeration.Action;
import it.polimi.ingsw.psp12.model.enumeration.TurnState;
import it.polimi.ingsw.psp12.utils.Color;
import org.junit.Before;
import org.junit.Test;

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
    public void setCurrentState_ShouldUpdateState() {
        // initialize state
        gameState2.initGame();
        // check initial state
        assertEquals(TurnState.INIT, gameState2.getCurrentState());

        gameState2.updateCurrentState(Action.MOVE);

        // check final state
        assertEquals(TurnState.MOVE, gameState2.getCurrentState());
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
    public void addPlayer_ArrayFull_ShouldThrowException() {
        gameState2.addPlayer("P1");
        gameState2.addPlayer("P2");

        // TODO: search how to manage exception in tests
        fail();
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

        assertTrue(gameState2.getCurrentPlayer().isInitialized());

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
}
