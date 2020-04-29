package it.polimi.ingsw.psp12.model;

import it.polimi.ingsw.psp12.model.board.Cell;
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
     * @return the corrisponding worker in the list
     */
    public Worker getWorker(int index) {
        return workers[index];
    }

    /**
     * update the position of the moved worker
     * @param index the worker that is moving
     * @param pos new worler position in the board
     */
    public void updateWorkerPosition(int index, Cell pos) {
        workers[index].move(pos);
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
     * @param pow the new poer of the player
     */
    public void setPower(Power pow) {
        this.power = pow;
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
            updateWorkerPosition(i, cells[i]);
            cells[i].addWorker(workers[i]);
        }

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
