package it.polimi.ingsw.psp12.model.power;

public class BasicPower extends Power {

    public BasicPower() {
        //Max height of the build
        maxHeight = 3;
        //Max number of square from current position
        maxMoves = 1;
        //Max number of possible action
        movesNumbers = 2;
        //Max number of possible build action
        buildsNumbers = 1;
    }

    @Override
    public int getNextPlayerHeight() {
        return 3;
    }


}