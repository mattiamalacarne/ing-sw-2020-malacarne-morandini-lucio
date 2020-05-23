package it.polimi.ingsw.psp12.view.userinterface.GUI.screens;

import it.polimi.ingsw.psp12.model.Worker;
import it.polimi.ingsw.psp12.model.board.Board;
import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.model.enumeration.Action;
import it.polimi.ingsw.psp12.network.messages.*;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.SetUpUtils.ChooseColorPanel;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.SetUpUtils.SetupDialog;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.gameUtils.BoardTerrainContainer;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.gameUtils.GamePhase;
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

    private Action choosedAction;

    private Board actualBoard;



    /**
     * init a gamescreen
     * @param gui
     * @param request null if gamemode, not null if setup mode
     */
    public GameScreen(GUinterface gui, Message request)
    {
        // Init screen size
       super(gui);
       me = this;
       req = null;
       //req = (RequestInfoMsg) request;


       selectdCells = new ArrayList<Cell>();

       System.out.println("Ho caricato gamescreen");

      setGamePhase(GamePhase.NOT_MY_TURN);

       board = new BoardTerrainContainer(new Dimension(screenX, screenY), this);
        this.add(board,JLayeredPane.DEFAULT_LAYER);

        this.setVisible(true);
    }

    public void setGamePhase(GamePhase phase)
    {
        this.phase = phase;
    }

    public void setMyWorker(List<Worker> workers)
    {
        List<Point> pos = new ArrayList<Point>();
        for (Worker w: workers)
        {
            pos.add(w.getPosition());
        }

        board.validateBoard(pos);
    }

    public GamePhase getPhase()
    {
        return this.phase;
    }


    private void setChoosedColor(List<it.polimi.ingsw.psp12.utils.Color> avColor)
    {
        SetupDialog setup = new SetupDialog(gui, new ChooseColorPanel(me, gui, avColor), "Set a color");
    }

    public void chooseColor(int index)
    {
        this.choosedColor = index;
    }

    public void drawBoard(UpdateBoardMsg msg)
    {

        // Reset della lista delle celle selezionate
        System.out.println("Aggiorno la board");
        actualBoard = null;
        actualBoard = msg.getBoard();
        board.updateBoard(msg.getBoard());

    }

    public void setUp(RequestInfoMsg msg)
    {
        setGamePhase(GamePhase.SETUP);
        req = msg;
        setChoosedColor(req.getAvailableColors());
        board.validateBoard(req.getAvailablePositions());
    }

    public void displayPossibility(List<Cell> cells)
    {
        List<Point> pos = new ArrayList<Point>();
        for (Cell c: cells)
        {
            pos.add(c.getLocation());
        }

        board.validateBoard(pos);
    }



    public void selectCell(Point p)
    {
        if (actualBoard == null)
        {
            actualBoard = new Board();
        }
        selectdCells.add(actualBoard.getCell(p));
        //System.out.println(phase);

        // Check the game phase
        switch (phase)
        {
            case CHOOSE_ACTION: askUserForAction(); break;// Ask user for action
            case SETUP: sendSetupWorkerToServer(); break; // Control the selected list size
            case ACTION: {gui.sendCellToServer(selectdCells.get(1)); resetSelectedCell(); break;} // Send the server the selected cell
            default: return;
        }
    }



    private void askUserForAction()
    {
        // Display an otionpanel with action
        System.out.println("Invio l'azione al server");
        gui.sendActionToServer(new SelectActionMsg(Action.MOVE, selectdCells.get(1).getWorker().getId()));
        System.out.println("Ho inviato il worker numero " + selectdCells.get(1).getWorker().getId());
        resetSelectedCell();
    }


    private void sendSetupWorkerToServer()
    {
        if (selectdCells.size() == 2)
        {
            gui.sendStartInfo( new PlayerInfoMsg( req.getAvailableColors().get(choosedColor),
                    selectdCells.get(0).getLocation(),
                    selectdCells.get(1).getLocation(),
                    req.getAvailableCards().get(0)) );
            //System.out.println("Data sended to server");
            board.flushBoard();
            setGamePhase(GamePhase.NOT_MY_TURN);
            resetSelectedCell();
            System.out.println("Dimension selection: "+selectdCells.size());
        } else
        {
            System.out.println("Select other cell");
        }
    }


    public void resetSelectedCell()
    {
        System.out.println("Rimuovo la selezione intera");
        for(int i = 0; i < selectdCells.size(); i++)
        {
            selectdCells.remove(i);
        }
    }

}
