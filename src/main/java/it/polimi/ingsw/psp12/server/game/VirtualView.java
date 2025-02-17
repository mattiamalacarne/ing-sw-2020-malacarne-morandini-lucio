package it.polimi.ingsw.psp12.server.game;

import it.polimi.ingsw.psp12.model.Player;
import it.polimi.ingsw.psp12.network.ClientHandler;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;
import it.polimi.ingsw.psp12.network.messages.Message;
import it.polimi.ingsw.psp12.utils.Observable;
import it.polimi.ingsw.psp12.utils.Observer;

/**
 * Class that manages a client hiding the network interface to the controller of the game
 * @author Luca Morandini
 */
public class VirtualView extends Observable<Message> implements Observer<Message>, CommandHandler {
    /**
     * Handler of the client
     */
    private final ClientHandler clientHandler;

    /**
     * Reference to the corresponding player in the model
     */
    private final Player player;

    public VirtualView(ClientHandler handler, Player p) {
        clientHandler = handler;
        player = p;

        // subscribe to incoming client handler commands
        handler.setCommandHandler(this);
    }

    /**
     * Send response command from controller
     * @param message response command generated by the controller
     */
    public void sendCommand(Message message) {
        clientHandler.send(message);
    }

    /**
     * Get the player associated with the virtual view
     * @return player reference
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Close socket connection
     */
    public void close() {
        if (clientHandler.isRunning()) {
            clientHandler.send(new Message(MsgCommand.CLOSE));
            clientHandler.close();
        }
    }

    @Override
    public void processCommand(Message message) {
        notifyObservers(message);
    }

    @Override
    public void update(Object sender, Message message) {
        // send message from model to client
        clientHandler.send(message);
    }
}
