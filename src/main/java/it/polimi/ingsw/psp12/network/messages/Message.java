package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.model.enumeration.MsgType;

import java.io.Serializable;

/**
 * Class for general message, server->client->server
 *
 * @author Mattia Malacarne
 */

public class Message implements Serializable
{
    // Message header
    /** Encapsulate the command direct to the server **/
    protected MsgType type;

    /** pass the cmd to the server **/
    protected String cmd;

    public Message(String cmd)
    {
        this.cmd = cmd;
    }
}
