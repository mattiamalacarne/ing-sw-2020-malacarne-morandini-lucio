package it.polimi.ingsw.psp12.server;

import it.polimi.ingsw.psp12.network.Command;

public interface CommandHandler {
    void processCommand(Command command);
}
