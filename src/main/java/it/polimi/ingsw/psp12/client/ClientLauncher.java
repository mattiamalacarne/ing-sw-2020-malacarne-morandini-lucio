package it.polimi.ingsw.psp12.client;


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
    private Object chooseUserInterface()
    {
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
                    return new Object(); // Return an instance of CLI
                }
                case 2:
                {
                    System.out.println("GUI");
                    return new Object(); // Return an instance of GUI
                }
                default: {System.out.println("Selection not possible, please choose one of the listed interface");}

            }
        }
        return null;
    }

    public static void main (String[] args)
    {
        ClientLauncher client = new ClientLauncher();
        Object inter = client.chooseUserInterface();
        System.out.println("Interface selected");
        //Setup the server info with the selected interface
        // userinterface.getServerInfo() ....
        // After serverInfo is setup connect to the server and the cli will listen for command (next is select if create a room or join a room)
    }



}
