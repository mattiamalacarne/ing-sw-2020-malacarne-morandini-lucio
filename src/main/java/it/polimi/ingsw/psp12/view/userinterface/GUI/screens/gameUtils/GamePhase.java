package it.polimi.ingsw.psp12.view.userinterface.GUI.screens.gameUtils;

public enum GamePhase
{
    SETUP, /** If in this the game will send the start position of the worker **/
    CHOOSE_ACTION, /** In this phase the player choose a worker and the GUI aks what action will do **/
    ACTION, /** The game ask the player a cell where perform the action **/
    NOT_MY_TURN /** If in this phase, GUI ignore the user selection **/
}
