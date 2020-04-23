package it.polimi.ingsw.psp12.server;

import it.polimi.ingsw.psp12.server.acceptance.AcceptanceServer;
import it.polimi.ingsw.psp12.server.acceptance.Constants;

import java.io.IOException;
import java.util.Scanner;

/**
 * Class that launches the acceptance server
 * @author Luca Morandini
 */
public class ServerLauncher
{
    public static void main(String[] args)
    {
        AcceptanceServer acceptanceServer;
        try {
            // create an instance of acceptance server
            acceptanceServer = new AcceptanceServer(Constants.ACCEPTANCE_PORT);
        }
        catch (IOException e) {
            System.out.println("failed to start acceptance server, shutting down...");
            e.printStackTrace();

            System.exit(1);
            return;
        }

        // launch acceptance server on a separate thread
        String threadName = "acceptance_server_" + Constants.ACCEPTANCE_PORT;
        Thread acceptanceThread = new Thread(acceptanceServer, threadName);
        acceptanceThread.start();

        System.out.println("acceptance server started on port " + Constants.ACCEPTANCE_PORT);


        // TODO: manage system administrator commands from CLI

        // listen for commands
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext())
        {
            String command = scanner.nextLine().toLowerCase();
            switch (command)
            {
                case "stop": {
                    acceptanceServer.close();
                    System.out.println("stopping server");
                    return;
                }
                default: {
                    System.out.println("command unknown");
                }
            }
        }
    }
}
