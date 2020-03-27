package it.polimi.ingsw.psp12.model;

public class GameState
{
    private Board gameBoard;
    private Player players[];
    private int turn;

    public Board getGameBoard() {
        return gameBoard;
    }

    public void addPlayer(Player player) {}

    public Player getCurrentPlayer() { return new Player();}

    public void nextTurn() {}
}
