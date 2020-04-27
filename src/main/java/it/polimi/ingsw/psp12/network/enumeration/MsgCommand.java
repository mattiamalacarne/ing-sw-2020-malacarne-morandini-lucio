package it.polimi.ingsw.psp12.network.enumeration;

/**
 * Enumeration of every possible commands that servers and clients can exchange
 */
public enum MsgCommand {
    /** System commands **/
    LIST(MsgType.SYSTEM), // request list of available rooms
    ROOMS(MsgType.SYSTEM), // response with a list of available rooms
    CREATE(MsgType.SYSTEM), // create a new room
    CREATED(MsgType.SYSTEM), // confirm creation of a new room
    JOIN(MsgType.SYSTEM), // join an existing room
    JOINED(MsgType.SYSTEM), // confirm that user has joined a game
    INVALID_NICKNAME(MsgType.SYSTEM), // nickname provided by the user is already taken
    DISCONNECTED(MsgType.SYSTEM), // client has disconnected from the server

    /** Game commands **/
    REQUEST_INFO(MsgType.GAME),
    PLAYER_INFO(MsgType.GAME),
    MOVE(MsgType.GAME),
    BUILD(MsgType.GAME),
    CELL_LIST(MsgType.GAME),
    SELECTED_CELL(MsgType.GAME),
    BOARD_UPDATE(MsgType.GAME),
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
