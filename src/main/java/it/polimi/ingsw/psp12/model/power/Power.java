package it.polimi.ingsw.psp12.model.power;

import it.polimi.ingsw.psp12.model.*;
import it.polimi.ingsw.psp12.model.board.Board;
import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.enumeration.Action;
import it.polimi.ingsw.psp12.model.enumeration.TurnState;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract Power class that will'll be decorated whit decorator pattern
 * @author Michele Lucio
 */
public abstract class Power {

    /**
     * identifier of the power
     */
    int powerId;

    /**
     * max level to climb in move
     */
    int maxClimbLevel;

    /*/**
     * max number of possible movement distance in cells
     */
    //int maxMoves; // TODO: can be removed? the dedicated power can work with just 'movesCount'

    /*/**
     * max possible building levels to build
     */
    //int maxBuildsLevel;

    /**
     * min level where it's possible to build a dome
     */
    int minDomeLevel;

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
    Cell lastPositions[];

    /**
     * cell of the last build
     */
    Cell lastBuild;

    /**
     * Getter of the id of the power
     * @return id of the power
     */
    public int getPowerId() {
        return powerId;
    }

    /**
     * Getter of the max level to climb in move
     * @return max level to climb in move
     */
    public int getMaxClimbLevel() {
        return maxClimbLevel;
    }

    /**
     * Setter of the max level to climb in move
     * @param maxClimbLevel max level to climb in move
     */
    public void setMaxClimbLevel(int maxClimbLevel) {
        this.maxClimbLevel = maxClimbLevel;
    }

    /*/**
     * Getter of the max number of possible movement distance in cells
     * @return max number of possible movement distance in cells
     */
    /*public int getMaxMoves() {
        return maxMoves;
    }*/

    /*/**
     * Getter of the max possible building levels to build
     * @return max possible building levels to build
     */
    /*public int getMaxBuildsLevel() {
        return maxBuildsLevel;
    }*/

    /**
     * Getter of the min level where it's possible to build a dome
     * @return min level where it's possible to build a dome
     */
    public int getMinDomeLevel(){
        return minDomeLevel;
    }

    /**
     * Getter of the move actions that the player performed in the current turn
     * @return count of move actions
     */
    public int getMovesCount() {
        return movesCount;
    }

    /**
     * Getter of the build actions that the player performed in the current turn
     * @return count of build actions
     */
    public int getBuildsCount() {
        return buildsCount;
    }

    /**
     * Getter of the max level the next player can climb in move
     * @return max level the next player can climb in move
     */
    public int getNextPlayerMaxClimb(){
        return 1;
    }

    /**
     * Checks victory condition
     * @return true if player has won
     */
    public boolean checkVictory() { return false; }

    /**
     * Generates a list of possible moves for the provided worker
     * @param b game board
     * @param w worker that has to be moved
     * @return list of cells
     */
    public List<Cell> getPossibleMoves(Board b, Worker w) { return new ArrayList<>(); }

    /**
     * Generates a list os possible builds for the provided worker
     * @param b game board
     * @param w worker that has to build
     * @return list of cells
     */
    public List<Cell> getPossibleBuilds(Board b, Worker w) { return new ArrayList<>(); }

    /**
     * Determines the list of next possible actions based on the current turn state
     * @param turnState current turn state
     * @return list of actions that can be performed next
     */
    public List<Action> nextActions(TurnState turnState) { return new ArrayList<>(); }

    /**
     * Save the last move position after the worker moved
     * @param position current cell
     */
    public void moved(Cell position) {
        // store at index 1 the previous position
        this.lastPositions[1] = this.lastPositions[0];
        // store at index 0 the current position
        this.lastPositions[0] = position;
        movesCount++;
    }

    /**
     * Save the last build position after the worker has built
     * @param position build cell
     */
    public void hasBuilt(Cell position) {
        this.lastBuild = position;
        buildsCount++;
    }

    public void reset() { }
}
