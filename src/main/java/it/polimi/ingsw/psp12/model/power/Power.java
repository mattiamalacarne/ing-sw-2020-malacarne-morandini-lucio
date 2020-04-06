package it.polimi.ingsw.psp12.model.power;

import it.polimi.ingsw.psp12.model.*;
import it.polimi.ingsw.psp12.model.board.Board;
import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.enumeration.Action;
import it.polimi.ingsw.psp12.model.enumeration.TurnState;

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

    /**
     * max number of possible movement distance in cells
     */
    int maxMoves;

    /**
     * max possible building levels to build
     */
    int maxBuildsLevel;

    /**
     * min level where it's possible to build a dome
     */
    int minDomeLevel;

    /**
     * max number of possible movements action
     */
    int movesCount;

    /**
     * max number of possible build action
     */
    int buildsCount;

    /**
     * cell of the last occupied position
     */
    Cell lastPosition;

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
     * Getter of the max number of possible movement distance in cells
     * @return max number of possible movement distance in cells
     */
    public int getMaxMoves() {
        return maxMoves;
    }

    /**
     * Getter of the max possible building levels to build
     * @return max possible building levels to build
     */
    public int getMaxBuildsLevel() {
        return maxBuildsLevel;
    }

    /**
     * Getter of the min level where it's possible to build a dome
     * @return min level where it's possible to build a dome
     */
    public int getMinDomeLevel(){
        return minDomeLevel;
    }

    /**
     * Getter of the max number of possible movements action
     * @return max number of possible movements action
     */
    public int getMovesCount() {
        return movesCount;
    }

    /**
     * Getter of the max number of possible build action
     * @return max number of possible build action
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
    };

    /**
     * Checks victory condition
     * @return true if player has won
     */
    public boolean checkVictory(){
        //FIXME: controllare condizione di vittoria
        return false;
    };

    public void moved(Cell newPosition){

    }

    public Cell[] getPossibleMoves(Board b, Worker w){
        //TODO: temp return
        return null;
    };

    public Cell[] getPossibleBuilds(Board b, Worker w){
        //TODO: temp return
        return null;
    };

    public List<Action> nextAction(TurnState turnState){
        //TODO: temp return
        return null;
    };

    public void updateLastPosition(Cell position){

    }

    public void updateLastBuild(Cell position){

    }



}
