package it.polimi.ingsw.psp12.exceptions;

/**
 * this exception run when he type of message is not defined or not in the list
 * if you run this.... is a problem
 *
 * @author Mattia Malacarne
 */
public class MessageTypeNotFoundException extends Exception
{
    public MessageTypeNotFoundException()
    {
        super("Message type not found, impossible to cast the message");
    }
}
