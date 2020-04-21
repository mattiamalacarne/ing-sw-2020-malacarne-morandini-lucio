package it.polimi.ingsw.psp12.server;

import it.polimi.ingsw.psp12.network.ClientHandler;
import it.polimi.ingsw.psp12.network.messages.Message;

public interface Server {
    void processCommand(Message message, ClientHandler client);
}
