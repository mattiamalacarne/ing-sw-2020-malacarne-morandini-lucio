package it.polimi.ingsw.psp12.model.power;

import it.polimi.ingsw.psp12.model.board.Board;
import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.model.enumeration.Action;
import it.polimi.ingsw.psp12.model.GameState;
import it.polimi.ingsw.psp12.model.Worker;

import java.util.List;

/**
 * Abstract decorator that will be used as interface implemented by each decorator
 * @author Michele Lucio
 */
public abstract class ExtendedPowerDecorator extends Power{

    protected Power power;

//    @Override
//    public abstract  boolean checkVictory();



    @Override
    public List<Cell> getPossibleMoves(Board b, Worker w){
        //FIXME: controller?
        return null;
    }

    @Override
    public List<Cell> getPossibleBuilds(Board b, Worker w) {
        //FIXME: controller?
        return null;
    }


}
