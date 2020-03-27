package it.polimi.ingsw.psp12.model;

import java.net.Inet4Address;

public class NetworkInfo
{
    private Inet4Address ip;
    private int port;

    public Inet4Address getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
