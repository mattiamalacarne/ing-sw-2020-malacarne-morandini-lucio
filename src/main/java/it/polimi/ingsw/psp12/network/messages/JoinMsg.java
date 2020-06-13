package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;

/**
 * Class used by the client to join a game providing the selected name
 * @author Luca Morandini
 */
public class JoinMsg extends Message {

    /**
     * Name of the client
     */
    private final String userName;

    /**
     * Construct the class
     * @param userName name selected by the client
     */
    public JoinMsg(String userName) {
        super(MsgCommand.JOIN);
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }
}
