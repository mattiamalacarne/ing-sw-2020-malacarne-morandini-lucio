# Santorini: software engineering project
*Group id: PSP12*

## Group components
- **Michele Lucio**: implemented the CLI interface, cards management and some god powers
- **Mattia Malacarne**: has built the client structure, mainly focused on the GUI interface
- **Luca Morandini**: developed the server and game logic

## Basic features
We have implemented the set of complete rules:
- it is possible to play with 2 or 3 players
- all "simple gods" cards are implemented *(except for Hermes)*

It is also possible to play without any cards *(easy mode)* but it is not required by the specifications.

We have developed both the CLI and the GUI where the GUI is the default UI and the CLI can be launched with a specific command.

Since the game uses standard TCP sockets, it can be played on different machines connected on a network.

## Advanced features
### Undo command
After the completion of a turn, the server start a timer of 5 seconds and the client shows a messages to notify the user that the turn is completed.

The player can confirm the turn and immediately pass the turn to the next player or can undo the actions and restart from the situation at the beginning of the turn.

If the player does not send either the `undo` or `confirm` command, the server pass the turn to the next player and notifies the current player that the turn ended.

### Multi game
All the clients, when started, first connect to the Acceptance Server (AS) on the default port.
The AS checks if there is an available room or creates a new empty room and asks the client how many players can join the newly created game.

To the other clients that connect later, the AS sends a `wait` command until there is a room ready to be joined.

When there are enough clients to fill a room, the AS send the `join` command to the first clients that fulfill the game and redirect them to the dedicated Game Server (GS).

The GS is responsible to manage a single game, so every instance have a dedicated controller and model.
After all the clients have joined the GS associated to the newly created game, the game can start with the normal game flow, starting from the setup phase.

When the game ends (or after a client has disconnected) the GS disconnects all the clients, closes the game and stops the server.

## System requirements
Both the server and the client are based on the **JDK 13**.
Be sure to install the JDK 13, or later versions, before running the game.

## How to build
To simplify the launch of the client and server, we decided to create two different JARs.

To achieve this, we have created two profiles in the POM file: **client** and **server**.

Before start the building, just select the correct Maven profile and run the classic `package` Maven command.

**NOTE**: select exclusively one of the profile! If both or none are selected, we do not guarantee the correct behaviour.

## How to play
### Server
To start the server, open the terminal, move to the folder where the JAR is located and run this command from the CLI.
```bash
cd /path/to/server_jar

#launch server
java -jar Santorini-Server-1.0.jar
```

If you want to stop the server, send the `stop` command in the CLI.

The server will not accept new clients and will not create new rooms.
All the waiting clients will be disconnected immediately but the games in progress will not be terminated until the game ends.
The clients connected to these games will be disconnected normally when the game ends.

### Client
To start the client, open the terminal, move to the folder where the JAR is located and run this command from the CLI.
```bash
cd /path/to/client_jar

#launch gui (default ui)
java -jar Santorini-Client-1.0.jar
```

Both the CLI and GUI interface can be launched from the same JAR.

The default UI is the GUI.
To run the CLI, pass the argument `-cli` to the run command.

As default, the colors on the CLI are enabled. If you want to disable the colors of the CLI (classic Windows terminal does not support ANSI colors), pass the `-nocolor` argument.
```bash
#launch cli (with colors enabled)
java -jar Santorini-Client-1.0.jar -cli

#launch cli (with colors disabled)
java -jar Santorini-Client-1.0.jar -cli -nocolor
```

The client will request the address of the server to connect to and then tries to start a game session.
At the end of a game, the client returns to the starting screen to allow the user to play another game.

*Project developed and delivered in 2020.<br>
Not actively maintained since 3 July 2020.*