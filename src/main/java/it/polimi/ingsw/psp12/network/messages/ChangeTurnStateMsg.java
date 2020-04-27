package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.model.enumeration.Action;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;

/**
 * Class for send to server a request to change the current turn state (action fr player)
 * the server will responde with a list of possibilemove o build for the state selected
 *
 * @author Mattia Malacarne
 */
public class ChangeTurnStateMsg extends Message
{

    private Action action;
    /**
     * Change the turn state and request the cell list
     * @param action define if build cell list or move cell list (Da vedere potrebbe non serive)
     */
    public ChangeTurnStateMsg(Action action)
    {
        super(MsgCommand.CHANGE_TURN_STATE);
        this.action = action;
    }


}
