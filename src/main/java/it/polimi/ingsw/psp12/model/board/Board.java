package it.polimi.ingsw.psp12.model.board;

import it.polimi.ingsw.psp12.model.Worker;

/**
 * Class that manages the game board and gives access to the cells
 * @author Luca Morandini
 */
public class Board
{
    /**
     * Matrix of cells that compose the game board
     */
    private Cell boardCells[][];


    /**
     * Constructor of the board
     */
    public Board() {
        boardCells = new Cell[5][5];

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                boardCells[x][y] = new Cell(x, y);
            }
        }
    }

    /**
     * Returns a cell on the map
     * @param pos coordinates of the cell
     * @return cell with the requested coordinates
     */
    public Cell getCell(Point pos) {
        // TODO: throw exception if pos is invalid
        if (!pos.isOnBoard()) {
            // throw new NameToBeDefinedException();
            return null;
        }

        return boardCells[pos.getX()][pos.getY()];
    }

    /**
     * Moves the position of a worker on the map
     * @param oldPoint current position of the worker
     * @param newPoint new position of the worker after the move
     */
    public void move(Point oldPoint, Point newPoint) {
        // TODO: throw exception if oldPoint or newPoint are invalid
        if (!oldPoint.isOnBoard() || !newPoint.isOnBoard()) {
            // throw new NameToBeDefinedException();
            return;
        }

        Worker worker = boardCells[oldPoint.getX()][oldPoint.getY()].removeWorker();

        boardCells[newPoint.getX()][newPoint.getY()].addWorker(worker);
    }

    /**
     * Increments the level of a tower on the map
     * @param pos coordinates of the tower
     */
    public void build(Point pos) {
        // TODO: throw exception if pos is invalid
        if (!pos.isOnBoard()) {
            // throw new NameToBeDefinedException();
            return;
        }

        boardCells[pos.getX()][pos.getY()].getTower().incrementLevel();
    }
}
