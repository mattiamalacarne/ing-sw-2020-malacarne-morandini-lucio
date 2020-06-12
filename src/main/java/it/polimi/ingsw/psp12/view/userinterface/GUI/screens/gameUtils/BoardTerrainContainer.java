package it.polimi.ingsw.psp12.view.userinterface.GUI.screens.gameUtils;

import it.polimi.ingsw.psp12.model.board.Board;
import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.model.enumeration.Action;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.GameScreen;
import it.polimi.ingsw.psp12.view.userinterface.GUI.screens.guiengine.MenuTextComponent;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Class for draw the gameterrain, bg and workers on the screen
 * @author Mattia Malacarne
 */
public class BoardTerrainContainer extends JLayeredPane
{
    /** Game terrain image **/
    private JLabel gameBoard;

    /** Game grid **/
    private JPanel gameGrid;

    /** List of cell **/
    private CellDraw[][] cells;

    /** infobox text **/
    private MenuTextComponent infoLabel;

    private GameScreen game;

    private CardGamePanel cardPanel;

    public BoardTerrainContainer(Dimension size, GameScreen game)
    {
        // Set dimension of the terrain size
        this.setPreferredSize(size);

        this.game = game;

        cardPanel = new CardGamePanel(null, size);

        infoLabel = new MenuTextComponent("Loading");
        infoLabel.setBounds(10,10, 650, 30);
        // Init the gameGrid
        gameGrid = new JPanel();
        gameGrid.setBounds(496,187, 430,438 );
        //gameGrid.setBackground(Color.RED);
        gameGrid.setOpaque(false);

        gameGrid.setLayout(new GridLayout(5,5,0,0));
        // Create the grid
        drawGameGrid(gameGrid);

        Image board = loadGameTerrain(size);
        gameBoard = new JLabel(new ImageIcon(board));
        gameBoard.setBounds(0,0, size.width, size.height);

        this.setLayout(null);

        this.add(gameBoard, JLayeredPane.DEFAULT_LAYER);
        this.add(gameGrid, JLayeredPane.DRAG_LAYER);
        this.add(infoLabel, JLayeredPane.DRAG_LAYER);
        this.add(cardPanel, JLayeredPane.DRAG_LAYER);

        this.setVisible(true);
    }

    /**
     * Load the image for the game terrain
     * @return a scaled image of the terrain
     */
    private Image loadGameTerrain(Dimension size)
    {
        ImageIcon icon = new ImageIcon(getClass().getResource("/gamescreenbg.png"));
        Image scaled = icon.getImage().getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
        return scaled;
    }

    /**
     * Redraw the gameGrid reading a BoardUpdateMsg
     */
    private void drawGameGrid(JPanel grid)
    {
        cells = new CellDraw[5][5];
        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                cells[j][i] = new CellDraw(new Point(j,i), game);
                gameGrid.add(cells[j][i]);
            }
        }
    }

    /**
     * This method remove all the border and listener to the cells
     * prepare the board for the next update
     */
    public void flushBoard()
    {
        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                cells[i][j].flushMe();
            }
        }
    }

    /**
     * Make the cells passed by parameter clickable
     * @param validCell cell to be make valid
     */
    public void validateBoard(List<Point> validCell)
    {
        //System.out.println("Inizio a validare");
        flushBoard();
        for (Point p: validCell)
        {
            //System.out.println("Valido una cella");
            cells[p.getX()][p.getY()].enablePress();
        }
        this.repaint();
        this.revalidate();
    }

    /**
     * Display the board drawing worker and tower in position
     * @param board
     */
    public void updateBoard(Board board)
    {
        // remove all the elements in the board
        clearBoard();
        // draw worker on screen
        List<Cell> cellWithWorker = board.getCellsWithWorker();
        for (Cell cell: cellWithWorker)
        {
            Point cellPos = cell.getLocation();
            cells[cellPos.getX()][cellPos.getY()].updateCell(CellIcon.WORKER, cell.getWorker().getColor(), 0);
        }

        // Draw tower on the screen
        List<Cell> cellWithTower = board.getCellsWithBuild();
        for (Cell cell: cellWithTower)
        {
            Point cellPos = cell.getLocation();
            int level = cell.getTower().getLevel();

            if (cell.getTower().hasDome()) level = 4;

            cells[cellPos.getX()][cellPos.getY()].updateCell(CellIcon.TOWER, null, level);
        }
        //this.revalidate();
    }

    /**
     * Remove all the worker and tower in the board, prepare the board for
     * draw the worker in the new position
     */
    private void clearBoard()
    {
        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                cells[i][j].setIcon(null);
            }
        }
    }

    public void setInfo(String info)
    {
        infoLabel.setText(info);
    }

    /**
     * Highlights on the board the possible selection
     * @param possibleList
     */
    public void showPossibleSelections(List<Cell> possibleList)
    {

    }
}
