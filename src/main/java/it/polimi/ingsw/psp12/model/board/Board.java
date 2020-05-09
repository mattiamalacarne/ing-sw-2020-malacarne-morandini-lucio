package it.polimi.ingsw.psp12.model.board;

import it.polimi.ingsw.psp12.model.Worker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that manages the game board and gives access to the cells
 * @author Luca Morandini
 */
public class Board implements Serializable
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
        return boardCells[pos.getX()][pos.getY()];
    }

    /**
     * Moves the position of a worker on the map
     * @param oldPoint current position of the worker
     * @param newPoint new position of the worker after the move
     */
    public void move(Point oldPoint, Point newPoint) {
        Worker worker = boardCells[oldPoint.getX()][oldPoint.getY()].removeWorker();

        boardCells[newPoint.getX()][newPoint.getY()].addWorker(worker);
    }

    /**
     * Increments the level of a tower on the map
     * @param pos coordinates of the tower
     */
    public void build(Point pos) {
        boardCells[pos.getX()][pos.getY()].getTower().incrementLevel();
    }

    /**
     * Returns the list of available cells that are not occupied by a worker
     * @return list of cells without a worker
     */
    public List<Cell> getCellsWithoutWorker() {
        List<Cell> cells = new ArrayList<>();

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                if (!boardCells[x][y].hasWorker()) {
                    cells.add(boardCells[x][y].clone());
                }
            }
        }

        return cells;
    }

    /**
     * Returns a clone of the board
     * @return board clone
     */
    public Board clone() {
        Board b = new Board();
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                b.boardCells[x][y] = this.boardCells[x][y].clone();
            }
        }
        return b;
    }
}
