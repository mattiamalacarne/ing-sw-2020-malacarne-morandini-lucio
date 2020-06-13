package it.polimi.ingsw.psp12.network.messages;

import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.enumeration.BuildOption;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;

import java.util.List;

/**
 * Class for sending to the client a list of build options that can be selected
 * @author Luca Morandini
 */
public class OptionsListMsg extends Message {
    /**
     * Options that the player can select
     */
    private final List<BuildOption> options;

    /**
     * Cell where the build will be performed
     */
    private final Cell cell;

    /**
     * Create the message with the list of build options that can be selected
     * and the cell where the build will be performed
     * @param options the list of build options
     * @param cell cell where the build will be performed
     */
    public OptionsListMsg(List<BuildOption> options, Cell cell) {
        super(MsgCommand.OPTIONS_LIST);

        this.options = options;
        this.cell = cell;
    }

    public List<BuildOption> getOptions() {
        return options;
    }

    public Cell getCell() {
        return cell;
    }
}
