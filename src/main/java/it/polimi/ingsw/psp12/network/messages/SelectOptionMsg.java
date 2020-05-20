package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.model.enumeration.BuildOption;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;

/**
 * Class used to send to the server the selected build option
 * @author Luca Morandini
 */
public class SelectOptionMsg extends Message {
    /**
     * Selected build option
     */
    BuildOption option;

    /**
     * Create the message with the build option selected by the user
     * @param option selected build option
     */
    public SelectOptionMsg(BuildOption option) {
        super(MsgCommand.SELECTED_OPTION);

        this.option = option;
    }

    public BuildOption getOption() {
        return option;
    }
}
