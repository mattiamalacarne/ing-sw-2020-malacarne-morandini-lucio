package it.polimi.ingsw.psp12.server;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Manager of the available ports that are assigned to the game servers
 * @author Luca Morandini
 */
public class PortsManager {
    /**
     * Class that holds an available port with its status
     */
    private static class Port {
        private final int port;
        private boolean assigned;

        private Port(int port) {
            this.port = port;
            this.assigned = false;
        }
    }

    /**
     * List of available ports for game servers
     */
    private static final List<Port> availablePorts = new ArrayList<>();

    public static void init(int[] ports) {
        for (int p : ports) {
            availablePorts.add(new Port(p));
        }
    }

    /**
     * Determines if there are available ports that can be assigned
     * @return true if there is at least one available port
     */
    public static synchronized boolean available() {
        return availablePorts.stream()
                .anyMatch(p -> !p.assigned);
    }

    /**
     * Returns a free port and mark it as assigned
     * @return assigned port
     */
    public static synchronized int assign() {
        Optional<Port> port = availablePorts.stream()
                .filter(p -> !p.assigned)
                .findFirst();

        port.ifPresent(p -> p.assigned = true);
        return port.map(p -> p.port).orElse(0);
    }

    /**
     * Releases the specified port
     * @param releasedPort port to be released
     */
    public static synchronized void release(int releasedPort) {
        Optional<Port> port = availablePorts.stream()
                .filter(p -> (p.port == releasedPort))
                .findFirst();

        port.ifPresent(p -> p.assigned = false);
    }
}
