package it.polimi.ingsw.psp12.model;

import it.polimi.ingsw.psp12.exceptions.InvalidMaxPlayersException;
import it.polimi.ingsw.psp12.model.board.Board;
import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.model.cards.Deck;
import it.polimi.ingsw.psp12.model.enumeration.Action;
import it.polimi.ingsw.psp12.model.enumeration.BuildOption;
import it.polimi.ingsw.psp12.model.enumeration.SetupState;
import it.polimi.ingsw.psp12.model.enumeration.TurnState;
import it.polimi.ingsw.psp12.model.cards.Card;
import it.polimi.ingsw.psp12.network.messages.Message;
import it.polimi.ingsw.psp12.network.messages.UpdateBoardMsg;
import it.polimi.ingsw.psp12.utils.Color;
import it.polimi.ingsw.psp12.utils.Observable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class that manages games instances, the board, the players and the current turn
 * @author Luca Morandini
 */
public class GameState extends Observable<Message>
{
    /**
     * The game board where the game is played
     */
    private final Board gameBoard;

    /**
     * List of players of the game
     */
    private Player[] players;

    /**
     * Deck of cards that provide god powers
     */
    private final Deck deck;

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
     * State of the setup process
     */
    private SetupState setup;

    /**
     * Available colors that a user can choose
     */
    private List<Color> colors;


    /**
     * Constructor of the class
     * @param maxPlayersCount max number of players allowed in the game
     * @throws InvalidMaxPlayersException if max players count is not between 2 and 3
     */
    public GameState(int maxPlayersCount) throws InvalidMaxPlayersException {
        // throw exception if max players count is invalid
        if (maxPlayersCount < 2 || maxPlayersCount > 3) throw new InvalidMaxPlayersException();

        gameBoard = new Board();
        players = new Player[maxPlayersCount];
        playersCount = 0;
        deck = new Deck(maxPlayersCount);
        setup = SetupState.CARDS_SELECTION;

        initColors();
    }

    /**
     * Getter for the board game
     * @return game board
     */
    public Board getGameBoard() {
        return gameBoard.clone();
    }

    /**
     * Return the number of players that are currently playing the game
     * @return players count
     */
    public int getPlayersCount() {
        return playersCount;
    }

    /**
     * Add the player to the game
     * @param name nickname of the player
     * @return created player
     */
    public Player addPlayer(String name) {
        Player player = new Player(playersCount, name);
        players[playersCount] = player;
        playersCount++;
        return player;
    }

    /**
     * Remove the current player after losing the game
     */
    public void removeCurrentPlayer() {
        // remove workers from the board
        for (int w = 0; w < 2; w++) {
            Worker worker = getCurrentPlayer().getWorkerByIndex(w);
            gameBoard.getCell(worker.getPosition()).removeWorker();
        }

        // remove player
        playersCount -= 1;

        Player[] tmp = players;
        players = new Player[playersCount];

        int i = 0;
        // iterate over all players
        for (int p = 0; p <= playersCount; p++) {
            // keep only players that are not the current one
            if (p != turn) {
                players[i] = tmp[p];
                i++;
            }
        }

        // move to the next player
        turn = turn % playersCount;

        // update board on the client
        notifyObservers(new UpdateBoardMsg(getGameBoard()));
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
        Player[] waitingPlayers = new Player[playersCount - 1];
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
     * @param action action that determine the new state to be saved
     */
    public void updateCurrentState(Action action) {
        switch (action) {
            case MOVE:
                state = TurnState.MOVE;
                break;
            case BUILD:
                state = TurnState.BUILD;
                break;
            case END:
                state = TurnState.END;
                break;
        }
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
     * @param newPoint new position of the worker after the move
     */
    public void move(Point newPoint) {
        // update position of the current worker and get the old position
        Point oldPoint = getCurrentPlayer().updateWorkerPosition(newPoint);

        // check if there is another worker in the new position
        if (gameBoard.getCell(newPoint).hasWorker()) {
            // get the new position for the other worker
            Point otherNewPoint = getCurrentPlayer().getPower().getOtherWorkerMove(oldPoint, newPoint);

            // update board moving two workers
            gameBoard.move(oldPoint, newPoint, otherNewPoint);
        }
        else {
            // update board moving one worker
            gameBoard.move(oldPoint, newPoint);
        }

        // save last move in the power
        getCurrentPlayer().getPower().moved(gameBoard.getCell(newPoint));

        // update board on the client
        notifyObservers(new UpdateBoardMsg(getGameBoard()));
    }

    /**
     * Increments the level of a tower on the map
     * @param pos coordinates of the tower
     */
    public void build(Point pos) {
        build(pos, BuildOption.BLOCK);
    }

    /**
     * Increments the level of a tower on the map or builds a dome
     * @param pos coordinates of the tower
     * @param option determines if build a block or a dome
     */
    public void build(Point pos, BuildOption option) {
        // update board
        gameBoard.build(pos, option);

        // save last build in the power
        getCurrentPlayer().getPower().hasBuilt(gameBoard.getCell(pos));

        // update board on the client
        notifyObservers(new UpdateBoardMsg(getGameBoard()));
    }

    /**
     * Initialize the game
     */
    public void initGame() {
        state = TurnState.INIT;
        turn = 0;
    }

    /**
     * Determine if all the players have completed their initialization process
     * @return true if all the players have been initialized
     */
    public boolean isInitialized() {
        for (int i = 0; i < playersCount; i++) {
            if (!players[i].isInitialized()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Update the player with the selected information
     * @param color color of the workers
     * @param points positions of the workers
     */
    public void setPlayerInfo(Color color, Point[] points) {
        // get the cells associated to the points selected by the user
        Cell[] cells = new Cell[2];
        for (int i = 0; i < 2; i++) {
            cells[i] = gameBoard.getCell(points[i]);
        }

        // initialize player
        players[turn].initialize(color, cells);

        // remove color from available colors
        colors.remove(color);

        // update clients with the new board
        notifyObservers(new UpdateBoardMsg(getGameBoard()));
    }

    /**
     * Initialize the list of available colors
     */
    private void initColors() {
        colors = new ArrayList<>(Arrays.asList(Color.values()));
    }

    /**
     * Return the list of available colors that a user can select
     * @return available colors
     */
    public List<Color> getAvailableColors() {
        return new ArrayList<>(colors);
    }

    /**
     * Initialize the turn for the current player and reset power parameters
     */
    public void initTurn() {
        // reset turn state
        state = TurnState.INIT;

        // save board snapshot at the beginning of the current turn
        gameBoard.saveSnapshot();

        // reset parameters before the current turn starts
        getCurrentPlayer().getPower().reset();

        // get max climb level of the current player from other players (NextCannotMoveUpDecorator)
        int maxClimb = 1;
        for (Player p : getWaitingPlayers()) {
            // save the most restrictive condition (minimum climb value)
            maxClimb = Math.min(maxClimb, p.getPower().getNextPlayerMaxClimb());
        }

        getCurrentPlayer().getPower().setMaxClimbLevel(maxClimb);
    }

    /**
     * Select the worker that will be used during the current turn
     * @param worker index of the selected worker
     */
    public void selectCurrentWorker(int worker) {
        // activate the worker selected by the user
        getCurrentPlayer().selectCurrentWorker(worker);

        // set the initial position of the current worker in the power
        Point pos = getCurrentPlayer().getCurrentWorker().getPosition();
        getCurrentPlayer().getPower().setInitialPosition(gameBoard.getCell(pos));
    }

    /**
     * Determines the list of next possible actions based on the current turn state
     * @return list of possible actions
     */
    public List<Action> nextActions() {
        // determine next possible actions based on
        // - the current turn state
        // - the power of the player
        // - the situation on the board
        return getCurrentPlayer().getPower().nextActions(state, gameBoard);
    }

    /**
     * Generates a list of possible cells for move action
     * @return list of cells to move on
     */
    public List<Cell> getPossibleMoves() {
        Player player = getCurrentPlayer();
        return player.getPower().getPossibleMoves(gameBoard, player.getCurrentWorker());
    }

    /**
     * Generates a list of possible cells for build action
     * @return list of cells to build on
     */
    public List<Cell> getPossibleBuilds() {
        Player player = getCurrentPlayer();
        return player.getPower().getPossibleBuilds(gameBoard, player.getCurrentWorker());
    }

    /**
     * Check if the current player has won the game
     * @return true if player has won
     */
    public boolean checkVictory() {
        // check victory only after a move action
        if (state != TurnState.MOVE) {
            return false;
        }

        return getCurrentPlayer().getPower().checkVictory();
    }

    /**
     * Returns the list of available cards that the first player can select
     * @return available cards
     */
    public List<Card> getAvailableCards() {
        return deck.getAvailableCards();
    }

    /**
     * Returns the list of cards selected by the first player that other players can select
     * @return selected cards
     */
    public List<Card> getSelectedCards() {
        return deck.getSelectedCards();
    }

    /**
     * Returns the count of the cards that the first player has still to select
     * @return selection remaining count
     */
    public int getRemainingCardsCount() {
        return deck.getSelectionRemainingCount();
    }

    /**
     * Process the card selected by the current player
     * @param card selected card
     */
    public void cardSelected(Card card) {
        switch (setup)
        {
            case CARDS_SELECTION:
                processSelectedCard(card);
                break;
            case CARDS_ASSIGNMENT:
                processAssignedCard(card);
                break;
        }
    }

    /**
     * Build the list of selected cards with the card selected by the first user
     * @param card selected card
     */
    private void processSelectedCard(Card card) {
        // manage the case with no powers in game with two players
        if (card.equals(Card.getNoPowers())) {
            // skip card selection and assignment processes
            setup = SetupState.WORKERS_PLACEMENT;
            return;
        }

        // update deck
        deck.cardSelected(card);

        // if selection process has completed (no more card to be selected)
        // start the assignment process where players select their own card
        if (deck.getSelectionRemainingCount() <= 0) {
            setup = SetupState.CARDS_ASSIGNMENT;
        }
    }

    /**
     * Process the card selected by the current player updating the power
     * @param card selected card
     */
    private void processAssignedCard(Card card) {
        // update deck
        deck.cardAssigned(card);

        // set the power of the current player with the selected card
        players[turn].setPower(card.getPower());

        // if assignment process has completed (only one card remained)
        // complete the process setting the power of the first player
        if (deck.getAssignmentRemainingCount() <= 1) {
            // set the power of the first player with the last card remained
            players[0].setPower(deck.getRemainingCard().getPower());

            // complete card setup process
            setup = SetupState.WORKERS_PLACEMENT;
        }
    }

    /**
     * Returns the current state of the setup process
     * @return setup state
     */
    public SetupState getCurrentSetupState() {
        return setup;
    }

    /**
     * Undo the current turn and restore board to the state at the beginning of the turn
     */
    public void undo() {
        // restore board to the state at the beginning of the current turn
        gameBoard.restoreSnapshot();

        // update board on the client
        notifyObservers(new UpdateBoardMsg(getGameBoard()));
    }
}
