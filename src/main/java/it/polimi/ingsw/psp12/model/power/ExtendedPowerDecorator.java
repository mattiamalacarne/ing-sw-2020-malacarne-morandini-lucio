package it.polimi.ingsw.psp12.model.power;

import it.polimi.ingsw.psp12.model.board.Board;
import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.model.enumeration.Action;
import it.polimi.ingsw.psp12.model.GameState;
import it.polimi.ingsw.psp12.model.Worker;

public abstract class ExtendedPowerDecorator extends Power{

    protected Power power;

    @Override
    public abstract void setMaxHeight(int maxHeight);

    @Override
    public abstract int getNextPlayerHeight();

    @Override
    public abstract  boolean checkVictory();

    @Override
    public abstract  void updateOldPosition(Point p);

    @Override
    public void reset(){
        //Max height of the build
        maxHeight = 3;
        //Max number of square from current position
        maxMoves = 1;
        //Max number of possible action
        movesNumbers = 2;
        //Max number of possible build action
        buildsNumbers = 1;
    };

    @Override
    public Cell[] possibleMoves(Board b, Worker w){
        //FIXME: controller?
        return null;
    }

    @Override
    public Cell[] possibleBuilds(Board b, Worker w) {
        //FIXME: controller?
        return null;
    }

    @Override
    public abstract Action nextAction(GameState gameState);

}
