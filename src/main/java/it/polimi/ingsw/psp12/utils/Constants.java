package it.polimi.ingsw.psp12.utils;

/**
 * Server constants
 * @author Luca Morandini
 */
public class Constants {
    static public final int ACCEPTANCE_PORT = 10000;

    static public final int[] GAME_PORTS = new int[] {
            20000,
            20001,
            20002,
            20003,
            20004
    };

    static public final int SOCKET_TIMEOUT = 20 * 1000; // 20 seconds

    static public final int PING_INTERVAL = SOCKET_TIMEOUT / 2;

    static public final int UNDO_INTERVAL = 5; // 5 seconds
}
