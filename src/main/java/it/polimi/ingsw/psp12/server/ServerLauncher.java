package it.polimi.ingsw.psp12.server;

import it.polimi.ingsw.psp12.server.acceptance.Constants;
import it.polimi.ingsw.psp12.server.acceptance.Server;

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
        try {
            GameServer testGameServer = new GameServer(Constants.MATCHES_STARTING_PORT, 2);
            Thread testThread = new Thread(testGameServer);
            testThread.start();

            System.out.printf("test game started on port " + Constants.MATCHES_STARTING_PORT + "\n");
        } catch (IOException e) {
            System.out.println("failed to start server");
            e.printStackTrace();
        }


        /*Server acceptanceServer;
        try {
            acceptanceServer = new Server(Constants.ACCEPTANCE_PORT);
        }
        catch (IOException e) {
            System.out.println("error while starting server... aborting");
            e.printStackTrace();
            System.exit(1);
            return;
        }

        // launch acceptance server on a separate thread
        new Thread(acceptanceServer).start();

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
        }*/
    }
}
