package it.polimi.ingsw.psp12.model.power;

import it.polimi.ingsw.psp12.model.*;
import it.polimi.ingsw.psp12.model.board.Board;
import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.model.enumeration.Action;
import it.polimi.ingsw.psp12.model.enumeration.TurnState;

import java.util.List;

/**
 * Abstract Power class that will'll be decorated whit decorator pattern
 * @author Michele Lucio, Luca Morandini
 */
public abstract class Power {

    /**
     * max level to climb in move
     */
    int maxClimbLevel;

    /**
     * number of move actions that the player performed in the current turn
     */
    int movesCount;

    /**
     * number of build actions that the player performed in the current turn
     */
    int buildsCount;

    /**
     * cells of the current and previous positions [current, previous]
     */
    Cell[] lastPositions;

    /**
     * cell of the last build
     */
    Cell[] lastBuilds;

    /**
     * cell where the next build will be performed
     */
    Cell buildInProgress;

    /**
     * Setter of the max level to climb in move
     * @param maxClimbLevel max level to climb in move
     */
    public abstract void setMaxClimbLevel(int maxClimbLevel);

    /**
     * Getter of the min level where it's possible to build a dome
     * @return min level where it's possible to build a dome
     */
    public abstract int getMinDomeLevel();

    /**
     * Getter of the max level the next player can climb in move
     * @return max level the next player can climb in move
     */
    public abstract int getNextPlayerMaxClimb();

    /**
     * Checks victory condition
     * @return true if player has won
     */
    public abstract boolean checkVictory();

    /**
     * Generates a list of possible moves for the provided worker
     * @param b game board
     * @param w worker that has to be moved
     * @return list of cells
     */
    public abstract List<Cell> getPossibleMoves(Board b, Worker w);

    /**
     * Generates a list os possible builds for the provided worker
     * @param b game board
     * @param w worker that has to build
     * @return list of cells
     */
    public abstract List<Cell> getPossibleBuilds(Board b, Worker w);

    /**
     * Determines the list of next possible actions based on the current turn state
     * @param turnState current turn state
     * @return list of actions that can be performed next
     */
    public abstract List<Action> nextActions(TurnState turnState, Board b);

    /**
     * Determines the new position of another worker when it is forced to move by the current worker
     * @param currentPos current position of the current worker
     * @param otherPos current position of the other worker
     * @return new position of the other worker
     */
    public abstract Point getOtherWorkerMove(Point currentPos, Point otherPos);

    /**
     * Save the last move position after the worker moved
     * @param position current cell
     */
    public abstract void moved(Cell position);

    /**
     * Save the initial position of the current worker at the beginning of a turn
     * @param position initial position of the worker
     */
    public abstract void setInitialPosition(Cell position);

    /**
     * Save the last build position after the worker has built
     * @param position build cell
     */
    public abstract void hasBuilt(Cell position);

    /**
     * Set the cell where the next build will be performed
     * @param position next build cell
     */
    public abstract void setBuildInProgress(Cell position);

    /**
     * Get the cell where the next build will be performed
     * @return next build cell
     */
    public abstract Cell getBuildInProgress();

    /**
     * Reset all Power's attributes to basic power condition
     */
    public abstract void reset();
}
