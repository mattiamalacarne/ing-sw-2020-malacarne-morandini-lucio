package it.polimi.ingsw.psp12.view.userinterface.GUI.screens;

import it.polimi.ingsw.psp12.exceptions.GUIStatusErrorException;
import it.polimi.ingsw.psp12.model.Worker;
import it.polimi.ingsw.psp12.model.board.Board;
import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.model.cards.Card;
import it.polimi.ingsw.psp12.model.enumeration.Action;
import it.polimi.ingsw.psp12.model.enumeration.BuildOption;
import it.polimi.ingsw.psp12.network.enumeration.MsgCommand;
import it.polimi.ingsw.psp12.network.messages.*;
import it.polimi.ingsw.psp12.view.userinterface.GUI.GUIStatus;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.SetUpUtils.ChooseColorPanel;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.SetUpUtils.GenericMessageScreen;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.SetUpUtils.SetupDialog;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.gameUtils.*;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.guiengine.MenuTextComponent;
import it.polimi.ingsw.psp12.view.userinterface.GUinterface;

import javax.swing.*;
import java.awt.*;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * This class containes the graphical elements for display the game terrain, worker and card
 * @author Mattia Malacarne
 */
public class GameScreen extends Screen
{

    private int terrainDimensionY;
    private int terrainDimensionX;

    // GUI Game terrain
    private JButton btnTest;

    /** Board image container**/
    private JPanel boardImageContainer;

    private int choosedColor;
    private RequestInfoMsg req;
    private boolean isSetup = false;

    private BoardTerrainContainer board;

    private GameScreen me;

    private GamePhase phase;

    private List<Cell> selectdCells;
    private List<Worker> myWorker;

    private int choosedAction;
    private List<Action> possibleActions;

    private Board actualBoard;

    private Thread undoBox;
    private SetupDialog undo;

    private Card gameCard;



    /**
     * init a game screen
     * @param gui the GUI
     * @param cardMsg draw the correct card on screen
     */
    public GameScreen(GUinterface gui, YourCardMsg cardMsg)
    {
        // Init screen size
       super(gui);
       me = this;
       req = null;
       this.gameCard = cardMsg.getCard();


       selectdCells = new ArrayList<Cell>();



       setGamePhase(GamePhase.NOT_MY_TURN);

       board = new BoardTerrainContainer(new Dimension(screenX, screenY), this, gameCard);
        this.add(board,JLayeredPane.DEFAULT_LAYER);



        this.setVisible(true);
    }

    /**
     * Set a new game phase
     * @param phase the game phase
     */
    public void setGamePhase(GamePhase phase)
    {
        this.phase = phase;
        //System.out.println("[DEBUG] Setting game phase " + phase);
        switch (phase)
        {
            case NOT_MY_TURN: { setGameInfo("Not my turn"); break; }
            case ACTION: { setGameInfo("Select where do you want to do the action"); break;}
            case SETUP: { setGameInfo("Choose your workers starting point"); break; }
        }
    }

    /**
     * Set game info message on screen
     * @param info text to display
     */
    public void setGameInfo(String info)
    {
        if (board != null) board.setInfo(info);
    }


    public GamePhase getPhase()
    {
        return this.phase;
    }


    /**
     * Set the choosed color
     * @param avColor
     */
    private void setChoosedColor(List<it.polimi.ingsw.psp12.utils.Color> avColor)
    {
        SetupDialog setup = new SetupDialog(gui, new ChooseColorPanel(me, gui, avColor), "Set a color");
    }

    public void chooseColor(int index)
    {
        this.choosedColor = index;
    }

    /**
     * Tell the gameBoard to draw worker and tower
     * @param msg the UpdateBoardMsg message
     */
    public void drawBoard(UpdateBoardMsg msg)
    {

        // Reset della lista delle celle selezionate
        //System.out.println("Aggiorno la board");
        actualBoard = null;
        actualBoard = msg.getBoard();
        // Reset clickable cell
        board.flushBoard();
        // Create board for this turn
        board.updateBoard(msg.getBoard());

    }

    /**
     * Setup the player selecting color and the start position of the worker
     * @param msg the RequestInfoMsg message
     */
    public void setUp(RequestInfoMsg msg)
    {
        setGamePhase(GamePhase.SETUP);
        req = msg;
        setChoosedColor(req.getAvailableColors());
        board.validateBoard(req.getAvailablePositions());
    }

    /**
     * Extract the location of the selectable cells and make it readable for validate board
     * @param cells the list of the selectable cells
     */
    public void displayPossibility(List<Cell> cells)
    {
        List<Point> pos = new ArrayList<Point>();
        for (Cell c: cells)
        {
            pos.add(c.getLocation());
        }

        board.validateBoard(pos);
    }

    public void displayWorkerPos(List<Point> p)
    {
        board.validateBoard(p);
    }

    /**
     * Select a cell or a list of cell to pass to the server
     * @param p the location of the cell
     */
    public void selectCell(Point p)
    {
        if (actualBoard == null)
        {
            actualBoard = new Board();
        }

        Cell selected = actualBoard.getCell(p);
        // check if cell has not already been selected
        if (selectdCells.contains(selected)) {
            return;
        }
        selectdCells.add(selected);

        // Check the game phase
        switch (phase)
        {
            case CHOOSE_ACTION: askUserForAction(); break;// Ask user for action
            case SETUP: sendSetupWorkerToServer(); break; // Control the selected list size
            case ACTION: {gui.sendCellToServer(selectdCells.get(0)); resetSelectedCell(); break;} // Send the server the selected cell
            default: return;
        }
    }

    /**
     * Generate the list of possible action for the user
     * @param actions the list of possible action for the user
     */
    public void setPossibleActionList(List<Action> actions)
    {
        this.possibleActions = actions;
    }

    /**
     * Ask the user wich action would like to complete in the turn
     *
     */
    private void askUserForAction()
    {
        // Display an otionpanel with action
        gui.sendSelectedWorkerToServer(new SelectWorkerMsg(selectdCells.get(0).getWorker().getId()));
        resetSelectedCell();
    }

    /**
     * Display a box with possible action list
     * @param poss list of possible action
     */
    public void displayActionSelection(List<Action> poss)
    {
        SetupDialog setup = new SetupDialog(gui, new ChooseActionPanel(gui, me, poss), "Choose action");
    }

    /**
     * Choose an action from the list
     * @param action the action
     */
    public void chooseAction(int action) {

        board.setInfo("Choose where do you want to " + possibleActions.get(action));
        gui.sendActionToServer(new SelectActionMsg(possibleActions.get(action)));
        resetSelectedCell();
    }

    /**
     * Send the start position for the worker to the server
     */
    private void sendSetupWorkerToServer()
    {
        if (selectdCells.size() == 2)
        {
            gui.sendStartInfo( new PlayerInfoMsg( req.getAvailableColors().get(choosedColor),
                    selectdCells.get(0).getLocation(),
                    selectdCells.get(1).getLocation()));
            board.flushBoard();
            setGamePhase(GamePhase.NOT_MY_TURN);
            resetSelectedCell();
        } else
        {
           // System.out.println("Select other cell");
        }
    }

    /**
     * Display a box for confirm the turn
     */
    public void displayUndoSelector()
    {
        undoBox = new Thread(() ->
        {
            undo = new SetupDialog(gui, new ChooseUndoPanel(gui, me), "Confirm turn?");
        });

        undoBox.start();
    }

    /**
     * Hide the undo box
     */
    public void destroyUndoBox()
    {
        undoBox.interrupt();
    }

    /**
     * Choose if confirm the turn or repeat
     * @param cmd the command
     */
    public void chooseUndo(MsgCommand cmd)
    {

        gui.sendUndoToServer(cmd);
    }

    /**
     * Set the selected cell list ready for the next turn
     */
    public void resetSelectedCell()
    {
        selectdCells.clear();
    }

    public void displayMessageScreen(String text)
    {
        try {
            gui.loadNewStatusScreen(GUIStatus.WAIT_OTHER_PLAYER, null);
        } catch (GUIStatusErrorException e) {
            e.printStackTrace();
        }
    }

    /**
     * Display a box with possible build list
     * @param poss the list of possible build option
     */
    public void displayBuildSelection(List<BuildOption> poss)
    {
        SetupDialog setup = new SetupDialog(gui, new ChooseBuildPanel(gui, poss, me), "Choose build");
    }

    /**
     * Choose the type of build
     * @param buildOption the build option
     */
    public void chooseBuild(BuildOption buildOption)
    {
        gui.sendBuildOptionToServer(buildOption);
    }

}
