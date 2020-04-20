package it.polimi.ingsw.psp12.network.messages;

public enum MsgCommand {
    /** System commands **/
    JOIN(MsgType.SYSTEM),
    DISCONNECTED(MsgType.SYSTEM),

    /** Game commands **/
    MOVE(MsgType.GAME),
    BUILD(MsgType.GAME),
    CELL_REQUEST(MsgType.GAME);

    /** Determine if this is a system or game message **/
    private MsgType type;

    public MsgType getType() {
        return type;
    }

    MsgCommand(MsgType type) {
        this.type = type;
    }
}
