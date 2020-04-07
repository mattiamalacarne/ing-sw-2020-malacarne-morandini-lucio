package it.polimi.ingsw.psp12.client;


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
    private Object userInterface;
    private ServerInfo serverInfo;

    /**
     * Ask user wich interface the game has to load
     * @return reference of the selected interface
     */
    private UserInterface chooseUserInterface()  {

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
                    return new InterfaceSelector().setInterface(interfaceSelected);

                } catch (IOException e)
                {
                    System.out.println("Error: Error while building userinterface");
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void main (String[] args) throws IOException {

        ClientLauncher client = new ClientLauncher();
        UserInterface ui = client.chooseUserInterface();

    }



}
