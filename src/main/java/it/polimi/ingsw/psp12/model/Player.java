package it.polimi.ingsw.psp12.model;

import java.awt.*;

public class Player
{
    private Worker workers[];
    private String name;
    private Color color;
    private NetworkInfo client;

    public Worker[] getWorkers() {
        return workers;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public NetworkInfo getClient() {
        return client;
    }
}
