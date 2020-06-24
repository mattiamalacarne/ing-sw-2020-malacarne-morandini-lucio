package it.polimi.ingsw.psp12.client;


import it.polimi.ingsw.psp12.view.userinterface.CLInterface;
import it.polimi.ingsw.psp12.view.userinterface.GUinterface;
import it.polimi.ingsw.psp12.view.userinterface.InterfaceSelector;
import it.polimi.ingsw.psp12.view.userinterface.UserInterface;

import java.io.IOException;
import java.util.Scanner;

/**
 * <p><b>Class</b> responsable for launch the game<p/>
 *
 * @author Mattia Malacarne
 */
public class ClientLauncher
{
    // TODO: Cambiare il tipo una volta che michele ha settato il factory per le interfacce
    /*private Object userInterface;
    private ServerInfo serverInfo;

    /**
     * Ask user which interface the game has to load
     * @return reference of the selected interface
     */
    /*private UserInterface chooseUserInterface()  {

        System.out.println("Select the interface you want to use");
        System.out.println("1) CLI");
        System.out.println("2) GUI");

        boolean isCorrectlySelected = false;
        Scanner choosedInterface = new Scanner(System.in);

        while (choosedInterface.hasNextInt())
        {
            int interfaceSelected = choosedInterface.nextInt();

            if (interfaceSelected < 1 || interfaceSelected > 2)
            {
                System.out.println("Selection not possible, please choose one of the listed interface");
                continue;
            } else
            {
                try
                {
                    return InterfaceSelector.setInterface(interfaceSelected);

                } catch (IOException e)
                {
                    System.out.println("Error: Error while building userinterface");
                    e.printStackTrace();
                }
            }
        }
        return null;
    }*/

    public static void main (String[] args) throws IOException {

        /*
        ClientLauncher client = new ClientLauncher();
        UserInterface ui = client.chooseUserInterface();
        //TODO: Toglimi alla fine (serve solo per testare la GUI
        //UserInterface ui = InterfaceSelector.setInterface(2);
        //TODO: Fine cosa da rimuovere
        */


        if (args.length > 0 && (args[0].contains("cli") || args[0].contains("c"))) {
            new CLInterface();
        }
        else {
            new GUinterface();
        }
    }



}
