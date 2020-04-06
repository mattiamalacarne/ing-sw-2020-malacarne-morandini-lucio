package it.polimi.ingsw.psp12.client;


import it.polimi.ingsw.psp12.view.userinterface.InterfaceSelector;
import it.polimi.ingsw.psp12.view.userinterface.UserInterface;

import java.io.IOException;
import java.util.Scanner;

/**
 * <p><b>Class</b> responsable for launch the game<p/>
 *
 * @author mattia
 */
public class ClientLauncher
{
    // TODO: Cambiare il tipo una volta che michele ha settato il factory per le interfacce
    private Object userInterface;
    private ServerInfo serverInfo;

    /**
     * Ask user wich interface the game has to load
     * @return reference of the selected interface
     */
    private UserInterface chooseUserInterface() throws IOException {

        System.out.println("Select the interface you want to use");
        System.out.println("1) CLI");
        System.out.println("2) GUI");

        boolean isCorrectlySelected = false;
        Scanner choosedInterface = new Scanner(System.in);

        while (choosedInterface.hasNextInt())
        {
            int interfaceSelected = choosedInterface.nextInt();
            switch (interfaceSelected)
            {
                case 1:
                {
                    System.out.println("CLI");
                    return new InterfaceSelector().setInterface(interfaceSelected); // Return an instance of CLI
                }
                case 2:
                {
                    System.out.println("GUI");
                    return new InterfaceSelector().setInterface(interfaceSelected);// Return an instance of GUI
                }
                default: {System.out.println("Selection not possible, please choose one of the listed interface");}

            }
        }
        return null;
    }

    public static void main (String[] args) throws IOException {

        ClientLauncher client = new ClientLauncher();
        UserInterface ui = client.chooseUserInterface();

    }



}
