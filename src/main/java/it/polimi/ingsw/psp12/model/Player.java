package it.polimi.ingsw.psp12.model;

import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.power.Power;

import java.awt.*;

public class Player
{
    private int id;
    private Worker workers[];
    private String name;
    private Power power;


    public Player(int id, String name) {
        this.id = id;
        this.name = name;

        workers = new Worker[2];
        for (int i = 0; i < 2; i++) {
            workers[i] = new Worker();
        }
    }

    public Worker getWorker(int index) {
        return workers[index];
    }

    public void updateWorkerPosition(int index, Cell pos) {
        workers[index].move(pos);
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Color getColor() {
        return new Color(0, 0, 0);
    }

    public void setPower(Power pow) {
        this.power = pow;
    }
}
