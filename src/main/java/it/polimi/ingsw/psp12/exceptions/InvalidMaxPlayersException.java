package it.polimi.ingsw.psp12.exceptions;

public class InvalidMaxPlayersException extends Exception {
    public InvalidMaxPlayersException() {
        super("Invalid max players count, must be between 2 and 3");
    }
}
