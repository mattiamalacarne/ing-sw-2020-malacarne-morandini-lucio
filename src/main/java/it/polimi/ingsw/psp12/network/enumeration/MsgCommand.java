package it.polimi.ingsw.psp12.network.enumeration;

/**
 * Enumeration of every possible commands that servers and clients can exchange
 */
public enum MsgCommand {
    /** System commands **/
    /**@deprecated **/ LIST(MsgType.SYSTEM), // request list of available rooms
    /**@deprecated **/ ROOMS(MsgType.SYSTEM), // response with a list of available rooms
    CREATE(MsgType.SYSTEM), // create a new room
    CREATED(MsgType.SYSTEM), // confirm creation of a new room
    CREATE_FAILED(MsgType.SYSTEM), // notify user that a room has not been created
    INVALID_MAX_PLAYERS(MsgType.SYSTEM), // max players count for a new room is invalid
    JOIN(MsgType.SYSTEM), // join an existing room
    JOINED(MsgType.SYSTEM), // confirm that user has joined a game
    INVALID_NICKNAME(MsgType.SYSTEM), // nickname provided by the user is already taken
    ROOM_FULL(MsgType.SYSTEM), // user can not subscribe because the room is already full
    DISCONNECTED(MsgType.SYSTEM), // client has disconnected from the server
    NOT_YOUR_TURN(MsgType.SYSTEM), // the client sent a message not in its turn
    PING(MsgType.SYSTEM), // ping to keep the connection open and prevent socket timeout
    CLOSE(MsgType.SYSTEM), // notify the client to close the socket
    WAIT(MsgType.SYSTEM),
    REQUEST_CREATE(MsgType.SYSTEM),

    /** Game commands **/
    REQUEST_INFO(MsgType.GAME),
    PLAYER_INFO(MsgType.GAME),
    ACTIONS_LIST(MsgType.GAME),
    SELECTED_ACTION(MsgType.GAME),
    TURN_ENDED(MsgType.GAME),
    TURN_DONE(MsgType.GAME),
    CONFIRM_TURN(MsgType.GAME),
    UNDO_TURN(MsgType.GAME),
    /**@deprecated **/ MOVE(MsgType.GAME), // TODO: remove, replaced with SELECTED_CELL
    /**@deprecated **/ BUILD(MsgType.GAME), // TODO: remove, replaced with SELECTED_CELL
    CELL_LIST(MsgType.GAME),
    SELECTED_CELL(MsgType.GAME),
    BOARD_UPDATE(MsgType.GAME),
    YOU_WON(MsgType.GAME),
    YOU_LOST(MsgType.GAME),
    OTHER_LOST(MsgType.GAME),
    OPTIONS_LIST(MsgType.GAME),
    SELECTED_OPTION(MsgType.GAME),
    CARDS_LIST(MsgType.GAME),
    SELECTED_CARD(MsgType.GAME),
    /**@deprecated **/ CELL_REQUEST(MsgType.GAME); // TODO: remove

    /** Determine if this is a system or game message **/
    private MsgType type;

    public MsgType getType() {
        return type;
    }

    MsgCommand(MsgType type) {
        this.type = type;
    }
}
