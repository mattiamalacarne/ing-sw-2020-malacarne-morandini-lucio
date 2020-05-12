package it.polimi.ingsw.psp12.controller;

import it.polimi.ingsw.psp12.model.GameState;
import it.polimi.ingsw.psp12.model.Player;
import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.enumeration.Action;
import it.polimi.ingsw.psp12.model.enumeration.TurnState;
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
            case SELECTED_ACTION:
                SelectActionMsg msg = (SelectActionMsg)message;

                // select the worker at the start of the turn
                if (model.getCurrentState() == TurnState.INIT) {
                    model.selectCurrentWorker(msg.getWorker());

                    System.out.println("player " + model.getCurrentPlayer().getId() + " selected worker " + msg.getWorker());
                }

                // manage the action selected by the user
                actionSelected(msg.getAction());
                break;
            case SELECTED_CELL:
                // update model performing the action on the cell selected by the user
                performAction((SelectCellMsg)message);
                break;
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

        System.out.println("players initialized, the game can start");

        // initialize the turn for the current player and notify the client
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

    /**
     * Send a message to the VirtualView associated with the current player
     * @param message message to be sent
     */
    void sendToCurrentPlayer(Message message) {
        sendToPlayer(model.getCurrentPlayer(), message);
    }

    /**
     * Initialize the turn for the current player
     * and send to the client the list of actions that can be performed
     */
    void beginTurn() {
        model.initTurn();

        System.out.println("player " + model.getCurrentPlayer().getId() + " started the turn");

        sendToCurrentPlayer(new ActionsListMsg(model.nextActions(), model.getCurrentPlayer().getWorkers()));
    }

    /**
     * Execute the next action
     * or end the current turn if there are no more action to be executed
     * @param action current action to be executed
     */
    void actionSelected(Action action) {
        if (action == Action.END) {
            // no more action to be executed or
            // user does not want to perform the extra action provided by the power
            endCurrentTurn();
            return;
        }

        // update the current state
        model.updateCurrentState(action);

        // execute the next action and send to the user a list of possible cells
        generateCellList();
    }

    /**
     * Generate a list of possible cells for the current action and send the list to the client
     * or determine if the current player has lost when can not perform the action
     */
    void generateCellList() {
        // get list of cells for the current action
        List<Cell> cells = null;
        Action action = null;
        switch (model.getCurrentState()) {
            case MOVE:
                action = Action.MOVE;
                cells = model.getPossibleMoves();
                break;
            case BUILD:
                action = Action.BUILD;
                cells = model.getPossibleBuilds();
                break;
        }

        System.out.printf("generated cells for %s action [%d] for player %d\n",
                model.getCurrentState().toString(), cells.size(), model.getCurrentPlayer().getId());

        // check if the current player has lost
        // player has lost if can not perform an action
        if (cells.size() == 0) {
            handleCurrentPlayerLoss();
            return;
        }

        // send list of cells to the current player
        sendToCurrentPlayer(new CellListMsg(cells, action));
    }

    /**
     * Update the model performing the action on the cell selected by the user
     * and determine if the current player has won the game
     * @param message
     */
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

        System.out.printf("player %d %s on cell %s\n",
                model.getCurrentPlayer().getId(), model.getCurrentState().toString(),
                message.getSelectedCell().getLocation().toString());

        // check if the current player has won
        if (model.checkVictory()) {
            handleCurrentPlayerVictory();
            return;
        }

        // determine what the current player can do next
        determineNextAction();
    }

    /**
     * Determine the next action that the current player can execute
     * and ask to the client what to do if there are more than one choice
     */
    void determineNextAction() {
        List<Action> actions = model.nextActions();

        System.out.printf("checking next possible actions [%d] for player %d\n",
                actions.size(), model.getCurrentPlayer().getId());

        // ask user what to do next if there are more than one action that can be executed
        if (actions.size() > 1) {
            System.out.println("more than one action possible, asking to the player " +
                    model.getCurrentPlayer().getId() + " what to do next");

            sendToCurrentPlayer(new ActionsListMsg(actions));
            return;
        }

        // execute the next action or end the turn
        actionSelected(actions.get(0));
    }

    /**
     * Notify the current player that the turn ended
     * and initialize the turn of the next player
     */
    void endCurrentTurn() {
        System.out.println("player " + model.getCurrentPlayer().getId() + " ended the turn");

        // notify the player that the turn ended
        sendToCurrentPlayer(new Message(MsgCommand.TURN_ENDED));

        // go to the next player
        model.nextTurn();

        // initialize turn for the next player
        beginTurn();
    }

    /**
     * Handle the loss of the current player
     * and end the game if only one player remains
     * or disconnect the current player and continue the game
     */
    void handleCurrentPlayerLoss() {
        System.out.println("player " + model.getCurrentPlayer().getId() + " has lost");

        // notify current player that has lost
        sendToCurrentPlayer(new Message(MsgCommand.YOU_LOST));

        // check if there the game can continue (at least two players remain)
        if ((model.getPlayersCount() - 1) >= 2) {
            // disconnect current client
            disconnectClient(model.getCurrentPlayer());

            System.out.println("removing player " + model.getCurrentPlayer().getId() + " and the workers");
            // remove current player
            model.removeCurrentPlayer();

            // notify other players that the current player has lost
            for (Player waitingPlayer : model.getPlayers()) {
                sendToPlayer(waitingPlayer, new OtherLostMsg(model.getCurrentPlayer().getName()));
            }

            // start the next turn
            beginTurn();
        }
        else {
            // notify other player that has won
            sendToPlayer(model.getWaitingPlayers()[0], new Message(MsgCommand.YOU_WON));

            // disconnect all clients and close room
            endGame();
        }
    }

    /**
     * Handle the victory of the current player
     * and end the game
     */
    void handleCurrentPlayerVictory() {
        System.out.println("player " + model.getCurrentPlayer().getId() + " has won");

        // notify the current player that has won
        sendToCurrentPlayer(new Message(MsgCommand.YOU_WON));

        // notify other players that have lost
        for (Player waitingPlayer : model.getWaitingPlayers()) {
            sendToPlayer(waitingPlayer, new Message(MsgCommand.YOU_LOST));
        }

        // disconnect all clients and close room
        endGame();
    }

    /**
     * Disconnect clients and notify the server responsible for closing the room when the game ended
     */
    public void endGame() {
        System.out.println("closing the game...");

        // disconnect clients
        for (Player player : model.getPlayers()) {
            disconnectClient(player);
        }

        // close room
        server.gameEnded();
    }

    /**
     * Disconnect client and close socket connection
     * @param player player associated to the client to disconnect
     */
    void disconnectClient(Player player) {
        Optional<VirtualView> vv = clients.stream().filter(v -> v.getPlayer().equals(player)).findFirst();

        if (!vv.isPresent()) {
            System.out.println("no virtual view associated with the requested player");
            return;
        }

        VirtualView view = vv.get();

        System.out.println("disconnecting player " + player.getId());

        // remove virtual view from the clients list
        clients.remove(view);

        // unsubscribe view from model events
        model.removeObserver(view);

        // close virtual view
        view.close();
    }
}
