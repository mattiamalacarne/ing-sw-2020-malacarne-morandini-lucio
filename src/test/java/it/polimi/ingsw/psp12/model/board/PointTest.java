package it.polimi.ingsw.psp12.model.board;

import org.junit.Test;

import static org.junit.Assert.*;

public class PointTest {
    @Test
    public void isOnGrid_Xlt0_ShouldReturnFalse () {
        Point p1 = new Point(-1, 3);
        assertFalse(p1.isOnBoard());
    }

    @Test
    public void isOnGrid_Ylt0_ShouldReturnFalse () {
        Point p2 = new Point(3, -1);
        assertFalse(p2.isOnBoard());
    }

    @Test
    public void isOnGrid_Xgt4_ShouldReturnFalse () {
        Point p3 = new Point(5, 2);
        assertFalse(p3.isOnBoard());
    }

    @Test
    public void isOnGrid_Ygt4_ShouldReturnFalse () {
        Point p4 = new Point(1, 5);
        assertFalse(p4.isOnBoard());
    }

    @Test
    public void isOnGrid_XYok_ShouldReturnTrue () {
        Point p5 = new Point(2, 3);
        assertTrue(p5.isOnBoard());
    }

    @Test
    public void getXgetY_ValidPoint_ShouldReturnValues() {
        Point p6 = new Point(0, 4);
        assertEquals(0, p6.getX());
        assertEquals(4, p6.getY());
    }

    @Test
    public void equals_SamePoints_ShouldReturnTrue() {
        Point p1 = new Point(1, 2);
        Point p2 = new Point(1, 2);
        assertTrue(p1.equals(p2));
    }

    @Test
    public void equals_DifferentPoints_ShouldReturnFalse() {
        Point p1 = new Point(0, 1);
        Point p2 = new Point(2, 3);
        assertFalse(p1.equals(p2));
    }

    @Test
    public void isNear_NotNear_ShouldReturnFalse() {
        Point p1 = new Point(0, 1);
        Point p2 = new Point(2, 3);
        assertFalse(p1.isNear(p2));
    }

    @Test
    public void isNear_NearOnX_ShouldReturnTrue() {
        Point p1 = new Point(2, 2);
        Point p2 = new Point(3, 2);
        assertTrue(p1.isNear(p2));
    }

    @Test
    public void isNear_NearOnY_ShouldReturnTrue() {
        Point p1 = new Point(0, 2);
        Point p2 = new Point(0, 1);
        assertTrue(p1.isNear(p2));
    }

    @Test
    public void isNear_NearOnDiagonal1_ShouldReturnTrue() {
        Point p1 = new Point(2, 2);
        Point p2 = new Point(3, 3);
        assertTrue(p1.isNear(p2));
    }

    @Test
    public void isNear_NearOnDiagonal2_ShouldReturnTrue() {
        Point p1 = new Point(2, 2);
        Point p2 = new Point(3, 1);
        assertTrue(p1.isNear(p2));
    }

    @Test
    public void isNear_NearOnDiagonal3_ShouldReturnTrue() {
        Point p1 = new Point(1, 2);
        Point p2 = new Point(0, 1);
        assertTrue(p1.isNear(p2));
    }

    @Test
    public void isNear_NearOnDiagonal4_ShouldReturnTrue() {
        Point p1 = new Point(1, 2);
        Point p2 = new Point(0, 3);
        assertTrue(p1.isNear(p2));
    }
}
