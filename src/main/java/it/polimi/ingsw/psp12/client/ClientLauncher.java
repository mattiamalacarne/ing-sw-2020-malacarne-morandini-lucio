package it.polimi.ingsw.psp12.client;

import it.polimi.ingsw.psp12.view.userinterface.CLInterface;
import it.polimi.ingsw.psp12.view.userinterface.GUinterface;

import java.io.IOException;

/**
 * <p><b>Class</b> responsible for launch the game<p/>
 *
 * @author Mattia Malacarne
 */
public class ClientLauncher
{
    public static void main (String[] args) throws IOException {
        // launch cli if there is the specific parameter
        // launch gui as default interface
        if (args.length > 0 && (args[0].contains("cli") || args[0].contains("c"))) {
            new CLInterface();
        }
        else {
            new GUinterface();
        }
    }
}
