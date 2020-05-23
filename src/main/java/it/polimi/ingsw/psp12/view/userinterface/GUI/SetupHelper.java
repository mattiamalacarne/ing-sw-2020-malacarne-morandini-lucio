package it.polimi.ingsw.psp12.view.userinterface.GUI;

/**
 * This class help the GUI while create serverinfo and setup a room
 * @author Mattia Malacarne
 */
public class SetupHelper
{
    private String hostname;
    private String roomName;
    private String clientName;
    private int roomMaxPlayer;

    /**
     * Create the helper used for Join a room
     * @param hostname
     * @param clientName
     */
    public SetupHelper(String hostname, String clientName)
    {
        this.hostname = hostname;
        this.clientName = clientName;
    }

    /**
     * Setup an helper for create a new room
     * @param roomName
     * @param roomMaxPlayer
     */
    public SetupHelper(String roomName, int roomMaxPlayer)
    {
        this.roomMaxPlayer = roomMaxPlayer;
        this.roomName = roomName;
    }


    public String getHostname() {
        return hostname;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getClientName() {
        return clientName;
    }

    public int getRoomMaxPlayer() {
        return roomMaxPlayer;
    }
}
