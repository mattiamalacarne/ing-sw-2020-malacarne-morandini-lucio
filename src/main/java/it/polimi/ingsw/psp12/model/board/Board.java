package it.polimi.ingsw.psp12.model.board;

import it.polimi.ingsw.psp12.model.Worker;
import it.polimi.ingsw.psp12.model.enumeration.BuildOption;

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
     * Moves the positions of two workers on the map simultaneously
     * @param oldPoint current position of the current worker
     * @param newPoint new position of the current worker / current position of the other worker
     * @param otherNewPoint new position of the other worker
     */
    public void move(Point oldPoint, Point newPoint, Point otherNewPoint) {
        // save the other worker before moving the current worker
        Worker otherWorker = boardCells[newPoint.getX()][newPoint.getY()].getWorker();

        // move the current worker
        move(oldPoint, newPoint);

        // move the other worker to the new position
        otherWorker.move(otherNewPoint);
        // place on the board the other worker
        boardCells[otherNewPoint.getX()][otherNewPoint.getY()].addWorker(otherWorker);
    }

    /**
     * Increments the level of a tower on the map or builds a dome
     * @param pos coordinates of the tower
     * @param option determines if build a block or a dome
     */
    public void build(Point pos, BuildOption option) {
        switch (option)
        {
            case BLOCK:
                boardCells[pos.getX()][pos.getY()].getTower().incrementLevel();
                break;
            case DOME:
                boardCells[pos.getX()][pos.getY()].getTower().buildDome();
                break;
        }
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
     * Returns the list of cells that are occupied by a worker
     * @return list of cells with a worker
     */
    public List<Cell> getCellsWithWorker() {
        List<Cell> cells = new ArrayList<>();

        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 5; y++) {
                if (boardCells[x][y].hasWorker()) {
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
