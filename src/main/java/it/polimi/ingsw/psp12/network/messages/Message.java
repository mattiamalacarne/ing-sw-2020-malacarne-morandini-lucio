package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;
import it.polimi.ingsw.psp12.network.enumeration.MsgType;

import java.io.Serializable;

/**
 * Class for general message, server-client-server
 *
 * @author Mattia Malacarne
 */

public class Message implements Serializable
{
    // Message header
    /** Encapsulate the command direct to the server **/
    protected MsgCommand command;

    public Message(MsgCommand cmd)
    {
        this.command = cmd;
    }

    public MsgType getType() {
        return command.getType();
    }

    public MsgCommand getCommand() {
        return command;
    }
}
