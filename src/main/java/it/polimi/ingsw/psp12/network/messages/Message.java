package it.polimi.ingsw.psp12.network.messages;

import java.io.Serializable;

/**
 * Class for general message, server->client->server
 *
 * @author Mattia Malacarne
 */

public abstract class Message implements Serializable
{
    // Message header
    /** Encapsulate the command direct to the server **/
    protected MsgType type;

    public Message(MsgType type)
    {
        this.type = type;
    }

    public MsgType getType() {
        return type;
    }
}
