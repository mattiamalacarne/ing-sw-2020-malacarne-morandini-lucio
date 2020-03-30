package it.polimi.ingsw.psp12.model.power;

import it.polimi.ingsw.psp12.model.*;
import it.polimi.ingsw.psp12.model.board.Board;
import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.model.enumeration.Action;

public abstract class Power {

    int maxHeight;
    int maxMoves;
    int movesNumbers;
    int buildsNumbers;
    Point oldPosition;
    int id;

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public abstract int getNextPlayerHeight();

    public boolean checkVictory(){
        //TODO: temp return
        return false;
    };

    public void updateOldPosition(Point p){
        oldPosition = p;
    }

    public void reset(){

    }

    public Cell[] possibleMoves(Board b, Worker w){
        //TODO: temp return
        return null;
    };

    public Cell[] possibleBuilds(Board b, Worker w){
        //TODO: temp return
        return null;
    };

    public Action nextAction(GameState gameState){
        //TODO: temp return
        return null;
    };

    public int getPowerId() {
        return id;
    }

}
