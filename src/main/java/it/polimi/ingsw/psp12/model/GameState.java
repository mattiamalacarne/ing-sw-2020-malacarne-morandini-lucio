package it.polimi.ingsw.psp12.model;

import it.polimi.ingsw.psp12.model.board.Board;
import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.model.enumeration.TurnState;
import it.polimi.ingsw.psp12.network.messages.Message;
import it.polimi.ingsw.psp12.utils.Observable;

/**
 * Class that manages games instances, the board, the players and the current turn
 * @author Luca Morandini
 */
public class GameState extends Observable<Message>
{
    /**
     * The game board where the game is played
     */
    private Board gameBoard;

    /**
     * List of players of the game
     */
    private Player players[];

    /**
     * Number of players subscribed to the game
     */
    private int playersCount;

    /**
     * Index of the player that is currently playing
     */
    private int turn;

    /**
     *  State of the current turn that indicates if the player is moving or building
     *  The typical flow can be changed by the powers of the cards
     */
    private TurnState state;


    /**
     * Constructor of the class
     * @param maxPlayersCount max number of players allowed in the game
     */
    public GameState(int maxPlayersCount) {
        // TODO: throw exception if count not in [2,3]
        gameBoard = new Board();
        state = TurnState.INIT;
        players = new Player[maxPlayersCount];
        playersCount = 0;
        turn = 0;
    }

    /**
     * Getter for the board game
     * @return game board
     */
    public Board getGameBoard() {
        return gameBoard;
    }

    /**
     * Add the player to the game
     * @param name nickname of the player
     * @return created player
     */
    public Player addPlayer(String name) {
        // TODO: throw exception if players array is full
        players[playersCount] = new Player(playersCount, name);
        playersCount++;

        return players[playersCount];
    }

    /**
     * Returns the player that is currently playing
     * @return current player
     */
    public Player getCurrentPlayer() {
        return players[turn];
    }

    /**
     * Returns the player that has played in the previous turn
     * @return previous player
     */
    public Player getPreviousPlayer() {
        int index = (turn + playersCount - 1) % playersCount;
        return players[index];
    }

    /**
     * Returns the players that are not currently playing
     * @return waiting players
     */
    public Player[] getWaitingPlayers() {
        Player waitingPlayers[] = new Player[playersCount - 1];
        int j = 0;
        for (int i = 0; i < playersCount; i++) {
            if (i != turn) {
                waitingPlayers[j] = players[i];
                j++;
            }
        }
        return waitingPlayers;
    }

    /**
     * Returns all the players subscribed to the game
     * @return all the players
     */
    public Player[] getPlayers() {
        return players.clone();
    }

    /**
     * Determines if a user is already registered with the name passed
     * @param name name of the user that is trying to register
     * @return true if user is already registered
     */
    public boolean alreadyRegistered(String name) {
        for (int i = 0; i < playersCount; i++) {
            if (players[i].getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the current state of the turn
     * @return turn state
     */
    public TurnState getCurrentState() {
        return state;
    }

    /**
     * Updates the turn state
     * @param s new state to be saved
     */
    public void setCurrentState(TurnState s) {
        state = s;
    }

    /**
     * Selects the next player that can play
     */
    public void nextTurn() {
        // go back to zero
        // when the current turn overflows the number of players
        turn = (turn + 1) % playersCount;
    }

    /**
     * Moves the position of a worker on the map
     * @param oldPoint current position of the worker
     * @param newPoint new position of the worker after the move
     */
    public void move(Point oldPoint, Point newPoint) {
        gameBoard.move(oldPoint, newPoint);

        //notifyObservers(new Message());
    }

    /**
     * Increments the level of a tower on the map
     * @param pos coordinates of the tower
     */
    public void build(Point pos) {
        gameBoard.build(pos);

        //notifyObservers(new Message());
    }
}
