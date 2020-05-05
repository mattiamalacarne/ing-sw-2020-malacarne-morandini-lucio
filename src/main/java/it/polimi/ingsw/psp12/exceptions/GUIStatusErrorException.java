package it.polimi.ingsw.psp12.exceptions;

/**
 * Exception for prevent the load of a not existing screen
 * @author Mattia Malacarne
 */
public class GUIStatusErrorException extends Exception
{
    public GUIStatusErrorException() { super("Error while loading new screen, status type not exists");}
}
