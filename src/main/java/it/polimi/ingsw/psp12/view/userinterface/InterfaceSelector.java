package it.polimi.ingsw.psp12.view.userinterface;

import java.io.IOException;
import java.net.UnknownHostException;

public class InterfaceSelector
{
    public UserInterface setInterface(int ui) throws IOException {
        switch (ui)
        {
            case 1: return new CLInterface();
            case 2: return new GUinterface();
            default: return new CLInterface();
        }
    }
}
