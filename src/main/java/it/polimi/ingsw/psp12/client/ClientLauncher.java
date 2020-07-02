package it.polimi.ingsw.psp12.client;

import it.polimi.ingsw.psp12.view.userinterface.CLInterface;
import it.polimi.ingsw.psp12.view.userinterface.GUinterface;

import java.io.IOException;

/**
 * <p><b>Class</b> responsible for launch the game</p>
 *
 * @author Mattia Malacarne
 */
public class ClientLauncher
{
    public static void main (String[] args) throws IOException {

        // launch cli if there is the specific parameter
        // launch gui as default interface
        if (args.length > 0 && (args[0].contains("cli") || args[0].contains("c"))) {

            // If it's used a terminal which doesn't support ansi code
            // it's used a different version of the printed board on cli (without colors)
            if ( args.length > 1 &&
                    ( args[1].toLowerCase().contains("nocolor") || args[1].toLowerCase().contains("no") )
            ){
                new CLInterface(false);
            }
            //Colored version
            else {
                new CLInterface(true);
            }

        }
        else {
            new GUinterface();
        }

    }
}
