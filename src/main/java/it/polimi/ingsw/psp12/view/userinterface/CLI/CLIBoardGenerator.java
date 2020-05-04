package it.polimi.ingsw.psp12.view.userinterface.CLI;

import it.polimi.ingsw.psp12.model.board.Board;
import it.polimi.ingsw.psp12.model.board.Cell;
import it.polimi.ingsw.psp12.model.board.Point;
import it.polimi.ingsw.psp12.utils.Color;

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
    static String cellFormat = "# %-20s "; // meglio █ ?
    static String testLastCellFormat = "# %-20s #";

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
        final String ANSI_WHITE   = "\u001B[37m";
        final String ANSI_RED     = "\u001B[31m";
        final String ANSI_YELLOW  = "\u001B[33m";
        final String ANSI_GREEN   = "\u001B[32m";
        final String ANSI_BLUE    = "\u001B[34m";

        switch (color){
            case RED:
                return  ANSI_RED + string + ANSI_RESET      + "              ";
            case GREEN:
                return  ANSI_GREEN + string + ANSI_RESET    + "              ";
            case YELLOW:
                return  ANSI_YELLOW + string + ANSI_RESET   + "              ";
            case BLUE:
                return  ANSI_BLUE + string + ANSI_RESET     + "              ";
            default:
                return string;
        }
    }

    //Upper_Bottom border
    private void makeBorder(StringBuilder boardString){
//        boardString.append(String.format(upperStringFormat,"#"));
        for (int c=0; c<23; c++){
            boardString.append('#'); //meglio ■ ?
        }
    }

    //INFO LEVEL: coordinate
    private void makeLine1(StringBuilder boardString, Cell cell){

        int x = cell.getLocation().getX();
        int y = cell.getLocation().getY();

        boardString.append(String.format(cellFormat,"["+ x + "," + y + "]"));
    }

    //LEVEL 3: player or cupola
    private void makeLine2(StringBuilder boardString, Cell cell){
        if ( (cell.hasWorker() && cell.getTower().getLevel()==3) || cell.getTower().hasDome() ){

            //there is a worker on level 3
            if (cell.hasWorker()){
                //FIXME: nome player
                boardString.append(String.format(cellFormat, colorString("player", cell.getWorker().getColor()) ));
            }

            //there is a dome
            else if (cell.getTower().hasDome()){
                boardString.append(String.format(cellFormat,"    ^  ")); //meglio ▲ ?
            }

        }else {
            boardString.append(String.format(cellFormat,""));
        }

    }

    //LEVEL 2: player or build
    private void makeLine3(StringBuilder boardString, Cell cell){
        if (cell.hasWorker() && cell.getTower().getLevel()==3){
            //FIXME: nome player
            boardString.append(String.format(cellFormat, colorString("player", cell.getWorker().getColor()) ));
        }else if (cell.getTower().getLevel()>1){
            boardString.append(String.format(cellFormat, "-"));
        }else {
            boardString.append(String.format(cellFormat, ""));
        }
    }

    //LEVEL 1: player or build
    private void makeLine4(StringBuilder boardString, Cell cell){
        if (cell.hasWorker() && cell.getTower().getLevel()==2){
            //FIXME: nome player
            boardString.append(String.format(cellFormat, colorString("player", cell.getWorker().getColor()) ));
        }else if (cell.getTower().getLevel()>1){
            boardString.append(String.format(cellFormat, "-"));
        }else {
            boardString.append(String.format(cellFormat, ""));
        }
    }

    //LEVEL 0: player or build
    private void makeLine5(StringBuilder boardString, Cell cell){
        if (cell.hasWorker() && cell.getTower().getLevel()==0){
            //FIXME: nome player
            boardString.append(String.format(cellFormat, colorString("player", cell.getWorker().getColor()) ));
        }else if (cell.getTower().getLevel()==1){
            boardString.append(String.format(cellFormat, "-"));
        }else {
            boardString.append(String.format(cellFormat, ""));
        }
    }

    public String toString(){

        StringBuilder stringBuilder = new StringBuilder();

        for (int row=0; row<5; row++){

            for (int column=0; column<5; column++){
                makeBorder(stringBuilder);
                if (column==4){
                    stringBuilder.append("#\n"); //meglio █ ?
                }
            }

            for (int column=0; column<5; column++){
                makeLine1(stringBuilder, board.getCell(new Point(column,row)));
                if (column==4){
                    stringBuilder.append("#\n"); //meglio █ ?
                }
            }

            for (int column=0; column<5; column++){
                makeLine2(stringBuilder, board.getCell(new Point(column,row)));
                if (column==4){
                    stringBuilder.append("#\n"); //meglio █ ?
                }
            }

            for (int column=0; column<5; column++){
                makeLine3(stringBuilder, board.getCell(new Point(column,row)));
                if (column==4){
                    stringBuilder.append("#\n"); //meglio █ ?
                }
            }

            for (int column=0; column<5; column++){
                makeLine4(stringBuilder, board.getCell(new Point(column,row)));
                if (column==4){
                    stringBuilder.append("#\n"); //meglio █ ?
                }
            }

            for (int column=0; column<5; column++){
                makeLine5(stringBuilder, board.getCell(new Point(column,row)));
                if (column==4){
                    stringBuilder.append("#\n"); //meglio █ ?
                }
            }

            if (row==4){
                for (int column=0; column<5; column++){
                    makeBorder(stringBuilder);
                    if (column==4){
                        stringBuilder.append("#\n"); //meglio ■ ?
                    }
                }
            }

        }

        return stringBuilder.toString();
    }
}
