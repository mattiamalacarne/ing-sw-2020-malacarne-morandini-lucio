package it.polimi.ingsw.psp12.model;

public class Board
{
    private Cell boardCells[][];

    public Board() {}

    private static class BoardHandler
    {
        private static final Board board = new Board();
    }

    public static Board getBoard()
    {
        return BoardHandler.board;
    }

    /**
     *
     * @param p cell's coordinates
     * @return  a cell locatate at point coordinates
     */
    public Cell getCell(Point p)
    {
        return boardCells[p.getX()][p.getY()];
    }
}
