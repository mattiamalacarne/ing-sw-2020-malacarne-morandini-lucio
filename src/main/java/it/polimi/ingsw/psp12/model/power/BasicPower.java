package it.polimi.ingsw.psp12.model.power;

/**
 * Basic implementation of power class
 * @author Michele Lucio
 */
public class BasicPower extends Power {

    /**
     * Constructor
     */
    public BasicPower() {
        reset();
    }

    /**
     * Reset all Power's attributes to basic power condition
     */
    public void reset() {

        powerId = 0;
        maxClimbLevel = 1;
        maxMoves = 1;
        maxBuildsLevel = 1;
        minDomeLevel = 4;
        movesCount = 1;
        buildsCount = 1;

    }

}
