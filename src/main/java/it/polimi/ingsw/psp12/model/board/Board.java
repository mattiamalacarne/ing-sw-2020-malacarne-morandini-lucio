package it.polimi.ingsw.psp12.model.board;

public class Board
{
    private Cell boardCells[][];

    public Board()
    {
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                boardCells[x][y] = new Cell(x, y);
            }
        }
    }

    public Cell getCell(Point pos)
    {
        // TODO: Ricordrsi di sostituire il return
        return null;
    }

    public void move(Point oldPoint, Point newPoint)
    {

    }

    public void build(Point pos)
    {

    }
}
