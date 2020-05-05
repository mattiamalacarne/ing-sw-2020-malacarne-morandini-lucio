package it.polimi.ingsw.psp12.controller;

import it.polimi.ingsw.psp12.model.GameState;
import it.polimi.ingsw.psp12.model.Player;
import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.enumeration.Action;
import it.polimi.ingsw.psp12.network.ClientHandler;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;
import it.polimi.ingsw.psp12.network.messages.*;
import it.polimi.ingsw.psp12.server.game.GameServer;
import it.polimi.ingsw.psp12.server.game.VirtualView;
import it.polimi.ingsw.psp12.utils.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Game controller that manages the commands from the clients, send responses and updates the game model
 * @author Luca Morandini
 */
public class Controller implements Observer<Message> {
    /**
     * Model of the current game
     */
    private GameState model;

    /**
     * List of subscribed clients
     */
    private List<VirtualView> clients;

    /**
     * Server that has created the game
     */
    private GameServer server;

    public Controller(GameState gameState, GameServer server) {
        this.model = gameState;
        this.server = server;
        this.clients = new ArrayList<>();
    }

    public void addClient(ClientHandler client, String name) {
        // create player with the provided name
        Player player = model.addPlayer(name);

        // create virtual view that connects client handler with the player in the model
        VirtualView view = new VirtualView(client, player);
        clients.add(view);

        // subscribe view to model events
        model.addObserver(view);

        // subscribe controller to view events
        view.addObserver(this);
    }

    /**
     * Starts the initialization process
     * In order asks users to select a color and the initial position of the workers
     */
    public void initGame() {
        // initialize game
        model.initGame();

        // send request to the first user
        requestPlayerInfo();
    }

    /**
     * Notify the server responsible for closing the room when the game ended
     */
    void endGame() {
        server.gameEnded();
    }

    @Override
    public void update(Object sender, Message message) {
        VirtualView vv = (VirtualView)sender;

        // check if the sender of the message can play
        if (!checkActivePlayer(vv)) {
            vv.sendCommand(new Message(MsgCommand.NOT_YOUR_TURN));
            return;
        }

        // process incoming command from client
        switch (message.getCommand())
        {
            case PLAYER_INFO:
                // update model with the information provided by the user
                processPlayerInfo((PlayerInfoMsg)message);
                break;
            case SELECT_ACTION:
                actionSelected((SelectActionMsg)message);
                break;
            case SELECTED_CELL:
                performAction((SelectCellMsg)message);
                break;
            //case MOVE:
            //    move();
            //    break;
            //case BUILD:
            //    //update after build
            //    break;
            //case CELL_REQUEST:
            //    break;
        }
    }

    /**
     * Check if the player that has sent a message is the current player
     * @param view virtual view of the player
     * @return true if the player is the current one
     */
    boolean checkActivePlayer(VirtualView view) {
        return view.getPlayer().equals(model.getCurrentPlayer());
    }

    /**
     * Generates the cells where it's possible to move
     */
    /*void move(){
        ArrayList<Cell> possibleCells;
        //TODO: initialize with possible cells where to move
        possibleCells = new ArrayList<>(); //

        new CellListMsg(possibleCells);
    }*/

    /**
     * Requests to the current user to select a color and the initial position of the workers
     */
    void requestPlayerInfo() {
        RequestInfoMsg request = new RequestInfoMsg();

        // add available colors
        request.setAvailableColors(model.getAvailableColors());

        // add available positions
        List<Cell> availableCells = model.getGameBoard().getCellsWithoutWorker();
        for (Cell c : availableCells) {
            request.addPosition(c.getLocation());
        }

        // send request to the current user
        sendToCurrentPlayer(request);

        System.out.println("requested info to player " + model.getCurrentPlayer().getId());
    }

    /**
     * Process the initialization information provided by the current user
     * @param msg incoming message
     */
    void processPlayerInfo(PlayerInfoMsg msg) {
        // update model with the
        model.setPlayerInfo(msg.getColor(), msg.getWorkersPositions());

        System.out.println("player " + model.getCurrentPlayer().getId() + " initialized");

        // check if all players have been initialized
        if (!model.isInitialized()) {
            // not all players are ready, switch to the next one
            model.nextTurn();

            // request to the next player to send its information
            requestPlayerInfo();
            return;
        }

        // all players are ready, the game can start
        model.initGame();

        // TODO: start playing process
        System.out.println("players initialized, the game can start");

        beginTurn();
    }

    /**
     * Send a message to the VirtualView associated with the specified player
     * @param player player to send the message
     * @param message message to be sent
     */
    void sendToPlayer(Player player, Message message) {
        Optional<VirtualView> vv = clients.stream().filter(v -> v.getPlayer().equals(player)).findFirst();

        if (!vv.isPresent()) {
            System.out.println("no virtual view associated with the requested player");
            return;
        }

        // send message
        vv.get().sendCommand(message);
    }






    void sendToCurrentPlayer(Message message) {
        sendToPlayer(model.getCurrentPlayer(), message);
    }

    void beginTurn() {
        model.initTurn();

        System.out.println("player " + model.getCurrentPlayer().getId() + " started the turn");

        sendToCurrentPlayer(new ActionsListMsg(model.nextActions()));
    }

    void actionSelected(SelectActionMsg message) {
        // activate the worker selected by the user
        model.getCurrentPlayer().selectCurrentWorker(message.getWorker());

        System.out.println("player " + model.getCurrentPlayer().getId() + " selected worker " +
                message.getWorker());

        generateCellList(message.getAction());
    }

    void generateCellList(Action action) {
        // update the current state
        model.updateCurrentState(action);

        // get list of cells for the current action
        List<Cell> cells = model.getActionCellList();

        System.out.println("generating possible moves [" + cells.size() + "] for player " +
                model.getCurrentPlayer().getId());

        // check if the current player has lost
        // player has lost if can not perform an action
        if (cells.size() == 0) {
            System.out.println("player " + model.getCurrentPlayer().getId() + " has lost");
            // TODO: manage exit of a player
        }

        // send list of cells to the current player
        sendToCurrentPlayer(new CellListMsg(cells));
    }

    void performAction(SelectCellMsg message) {
        // perform action based on current turn state
        switch (model.getCurrentState()) {
            case MOVE:
                model.move(message.getSelectedCell().getLocation());
                break;
            case BUILD:
                model.build(message.getSelectedCell().getLocation());
                break;
        }

        // check if the current player has win
        if (model.checkVictory()) {
            System.out.println("player " + model.getCurrentPlayer().getId() + " has win");
            // TODO: manage end of the game
            //endGame();
        }

        // determine what the current player can do
        determineNextAction();
    }

    void determineNextAction() {
        List<Action> actions = model.nextActions();
        System.out.println("checking next possible actions [" + actions.size() + "] for player " +
                model.getCurrentPlayer().getId());

        // check if turn ended
        if (actions.size() == 0) {
            // no more action to do
            endCurrentTurn();
        }
        else if (actions.size() == 1) {
            // process the next action
            generateCellList(actions.get(0));
        }
        else {
            // FIXME: remove after test, should never arrive here
            // if there are more than one action, ask user what to do?
            System.out.println("ehm... and now? what we have to do?");
        }
    }

    void endCurrentTurn() {
        System.out.println("player " + model.getCurrentPlayer().getId() + " ended the turn");

        // notify the player that the turn ended
        sendToCurrentPlayer(new Message(MsgCommand.TURN_ENDED));

        // go to the next turn
        model.nextTurn();

        // initialize turn for the next player
        beginTurn();
    }
}
