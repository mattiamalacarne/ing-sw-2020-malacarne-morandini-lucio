package it.polimi.ingsw.psp12.model;

import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.model.power.BasicPower;
import it.polimi.ingsw.psp12.model.power.Power;
import it.polimi.ingsw.psp12.utils.Color;

/**
 * <p><b>Class</b> that represent the player</p>
 * <p>This class store the information for the game state</p>
 *
 * @author Mattia Malacarne
 */
public class Player
{
    private int id;
    private Worker workers[];
    private int currentWorker;
    private String name;
    private Power power;
    private boolean initialized;


    public Player(int id, String name) {
        this.id = id;
        this.name = name;
        this.initialized = false;

        workers = new Worker[2];
        for (int i = 0; i < 2; i++) {
            workers[i] = new Worker();
        }
    }

    /**
     * Get the selected worker
     * @param index the index in the list of the current player's worker
     * @return the corresponding worker in the list
     */
    public Worker getWorkerByIndex(int index) {
        return workers[index];
    }

    /**
     * Get all the workers of the player
     * @return the list of the workers
     */
    public Worker[] getWorkers() {
        return workers;
    }

    /**
     * update the position of the moved worker
     * @param pos new worker position in the board
     * @return old position of the worker
     */
    public Point updateWorkerPosition(Point pos) {
        // store the old position that will be returned after the update
        Point oldPoint = workers[currentWorker].getPosition();

        workers[currentWorker].move(pos);
        return oldPoint;
    }

    /**
     * Select the worker that will be used during the current turn
     * @param index index of the worker
     */
    public void selectCurrentWorker(int index) {
        currentWorker = index;
    }

    /**
     * Return the worker that is used during the current turn
     * @return current worker
     */
    public Worker getCurrentWorker() {
        return workers[currentWorker];
    }

    /**
     * get the selected player name
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * get the player id
     * @return the player id
     */
    public int getId() {
        return id;
    }

    /**
     * set the player power depending of his card
     * @param pow the new power of the player
     */
    public void setPower(Power pow) {
        this.power = pow;
    }

    public Power getPower() {
        return this.power;
    }


    /**
     * Initialize player color and workers positions
     * @param color color of the workers
     * @param cells positions of the workers
     */
    public void initialize(Color color, Cell cells[]) {
        for (int i = 0; i < 2; i++) {
            // set worker color
            workers[i].setColor(color);

            // place worker on the board
            workers[i].move(cells[i].getLocation());
            cells[i].addWorker(workers[i]);
        }

        // set default power
        setPower(new BasicPower());

        initialized = true;
    }

    /**
     * Determine if the player has been initialized
     * @return true if player has been initialized
     */
    public boolean isInitialized() {
        return initialized;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id == player.id;
    }
}
