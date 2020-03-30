package it.polimi.ingsw.psp12.model.board;

public class Point
{
    private int x;
    private int y;

    /**
     *
     * @param x x position
     * @param y y position
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isOnBoard() {
        return (x >= 0 && x <= 4 && y >= 0 && y <= 4);
    }
}
