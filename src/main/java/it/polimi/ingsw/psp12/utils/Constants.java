package it.polimi.ingsw.psp12.utils;

/**
 * Server constants
 * @author Luca Morandini
 */
public class Constants {
    static public final int ACCEPTANCE_PORT = 10000;

    static public final int MATCHES_STARTING_PORT = 20000;

    static public final int MAX_GAMES_COUNT = 10;

    static public final int SOCKET_TIMEOUT = 20 * 1000; // 20 seconds

    static public final int PING_INTERVAL = SOCKET_TIMEOUT / 2;
}
