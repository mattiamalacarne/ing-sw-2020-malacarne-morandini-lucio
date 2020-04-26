package it.polimi.ingsw.psp12.view.userinterface.CLI;

import it.polimi.ingsw.psp12.model.board.Board;
import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.board.Point;

/**
 * Class used to generate a string representation of the board for the CLI
 * @author Michele Lucio
 */
public class CLIBoardGenerator {

    /**
     * Reference to the original board, where to take info about the board to print
     */
    private Board board;

    /**
     * Strings used to format text
     */
    static String upperStringFormat = "%-24s";
    static String innerStringFormat = " %-20s ";

    public CLIBoardGenerator(Board board) {
        this.board = board;
    }

    /**
     * This method returns a string received in input colored in the color received in input
     *
     * @param string String to color
     * @param color Color of the output string
     * @return Colored string
     * */
    private String colorString(String string, Color color){

        final String ANSI_RESET   = "\u001B[0m";
        final String ANSI_RED     = "\u001B[31m";
        final String ANSI_YELLOW  = "\u001B[33m";
        final String ANSI_GREEN   = "\u001B[32m";
        final String ANSI_BLUE    = "\u001B[34m";

        switch (color){
            case RED:
                return  ANSI_RED + string + ANSI_RESET;
            case GREEN:
                return  ANSI_GREEN + string + ANSI_RESET;
            case YELLOW:
                return  ANSI_YELLOW + string + ANSI_RESET;
            case BLUE:
                return  ANSI_BLUE + string + ANSI_RESET;
            default:
                return string;
        }
    }

    //Upper_Bottom border
    private void makeBorder(StringBuilder boardString){
        boardString.append(String.format(upperStringFormat,"#"));
    }

    //INFO LEVEL: coordinate
    private void makeLine1(StringBuilder boardString, Cell cell){

        int x = cell.getLocation().getX();
        int y = cell.getLocation().getY();

        boardString.append("#");
        boardString.append(String.format(innerStringFormat,"["+ x + "," + y + "]"));
        boardString.append("#");
    }

    //LEVEL 3: player or cupola
    private void makeLine2(StringBuilder boardString, Cell cell){
        //FIXME: il worker dovrebbe avere il livello a cui si trova?
        if ( (cell.hasWorker() /*&& cell.getWorker().getLevel()*/) || cell.getTower().hasDome() ){

            //there is a worker on level 3
            if (cell.hasWorker()){
                //FIXME: il worker dovrebbe avere i parametri nome e colore ?
                boardString.append(String.format(innerStringFormat ));
            }

            //there is a dome
            else if (cell.getTower().hasDome()){
                boardString.append(String.format(innerStringFormat,"    ^  "));
            }

        }else {
            boardString.append(String.format(innerStringFormat,""));
        }
    }

    //LEVEL 2: player or build
    private void makeLine3(StringBuilder boardString, Cell cell){

    }

    //LEVEL 1: player or build
    private void makeLine4(StringBuilder boardString, Cell cell){

    }

    //LEVEL 0: player or build
    private void makeLine5(StringBuilder boardString, Cell cell){

    }

    public String toString(){

        StringBuilder stringBuilder = new StringBuilder();

        for (int y=0; y<5; y++){

            makeBorder(stringBuilder);
            stringBuilder.append("\n");

            for (int x=0; x<5; x++){
                makeLine1(stringBuilder, board.getCell(new Point(x,y)));
            }
            stringBuilder.append("\n");

            for (int x=0; x<5; x++){
                makeLine2(stringBuilder, board.getCell(new Point(x,y)));
            }
            stringBuilder.append("\n");

            for (int x=0; x<5; x++){
                makeLine3(stringBuilder, board.getCell(new Point(x,y)));
            }
            stringBuilder.append("\n");

            for (int x=0; x<5; x++){
                makeLine4(stringBuilder, board.getCell(new Point(x,y)));
            }
            stringBuilder.append("\n");

            for (int x=0; x<5; x++){
                makeLine5(stringBuilder, board.getCell(new Point(x,y)));
            }
            stringBuilder.append("\n");

            makeBorder(stringBuilder);

        }

        return stringBuilder.toString();
    }
}
