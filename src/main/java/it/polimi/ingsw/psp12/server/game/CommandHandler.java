package it.polimi.ingsw.psp12.server.game;

import it.polimi.ingsw.psp12.network.messages.Message;

public interface CommandHandler {
    void processCommand(Message message);
}
