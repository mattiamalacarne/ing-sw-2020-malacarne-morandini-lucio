package it.polimi.ingsw.psp12.server;

import it.polimi.ingsw.psp12.model.Player;
import it.polimi.ingsw.psp12.network.ClientHandler;
import it.polimi.ingsw.psp12.network.Command;
import it.polimi.ingsw.psp12.utils.Observable;
import it.polimi.ingsw.psp12.utils.Observer;

/**
 * Class that manages a client hiding the network interface to the controller of the game
 * @author Luca Morandini
 */
public class VirtualView extends Observable<Command> implements Observer<Command>, CommandHandler {
    /**
     * Handler of the client
     */
    private ClientHandler clientHandler;

    /**
     * Reference to the corresponding player in the model
     */
    private Player player;

    public VirtualView(ClientHandler handler, Player p) {
        clientHandler = handler;
        player = p;

        // subscribe to incoming client handler commands
        handler.setCommandHandler(this);
    }

    /**
     * Send response command from controller
     * @param command response command generated by the controller
     */
    public void sendCommand(Command command) {
        clientHandler.send(command);
    }

    @Override
    public void processCommand(Command command) {
        // TODO: cast and process command

        notifyObservers(command);
    }

    @Override
    public void update(Command command) {
        // send message from model to client
        clientHandler.send(command);
    }
}
