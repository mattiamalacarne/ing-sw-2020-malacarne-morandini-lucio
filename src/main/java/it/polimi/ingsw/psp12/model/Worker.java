package it.polimi.ingsw.psp12.model;

import it.polimi.ingsw.psp12.model.board.Cell;

import java.awt.*;

public class Worker
{
    private Cell position;

    public Cell getPosition() {
        return position;
    }

    public void move(Cell cell) {
        position = cell;
    }
}
