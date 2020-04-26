package it.polimi.ingsw.psp12.controller;

import it.polimi.ingsw.psp12.model.GameState;
import it.polimi.ingsw.psp12.model.Player;
import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.network.ClientHandler;
import it.polimi.ingsw.psp12.network.messages.CellListMsg;
import it.polimi.ingsw.psp12.network.messages.Message;
import it.polimi.ingsw.psp12.server.game.VirtualView;
import it.polimi.ingsw.psp12.utils.Observer;

import java.util.ArrayList;
import java.util.List;

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

    public Controller(GameState gameState) {
        model = gameState;
        clients = new ArrayList<>();
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

    @Override
    public void update(Message message) {
        // process incoming command from client
        switch (message.getCommand())
        {
            case MOVE:
                move();
                break;
            case SELECTED_CELL:
                //TODO: update player position
                break;
            case BUILD:
                //TODO: update after build
                break;
            case CELL_REQUEST:
                break;
        }
    }

    /**
     * Generates the cells where it's possible to move
     */
    public void move(){
        ArrayList<Cell> possibleCells;
        //TODO: initialize with possible cells where to move
        possibleCells = new ArrayList<>(); //

        new CellListMsg(possibleCells);
    }

}
