package it.polimi.ingsw.psp12.client;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * <p><b>Class</b> for storing server information, used for open a socket connetion</p>
 * <p>It will be created by the user interface storing ip and a port</p>
 *
 * @author Mattia Malacarne
 */
public class ServerInfo
{
    // default ip localhost
    public Inet4Address serverIp = (Inet4Address) Inet4Address.getByName("127.0.0.1");
    public int serverPort = 55555; // default server port

    public ServerInfo(Inet4Address customIp, int customPort) throws UnknownHostException
    {
        this.serverIp = customIp;
        this.serverPort = customPort;

        System.out.println("Custom server info saved");
    }

    public ServerInfo(Inet4Address customIp) throws UnknownHostException
    {
        this.serverIp = customIp;

        System.out.println("Custom server ip saved");
    }
}
