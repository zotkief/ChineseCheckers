package com.jkpr.chinesecheckers.client.boards;

import com.jkpr.chinesecheckers.server.exceptions.InvalidNumberOfPlayers;
import com.jkpr.chinesecheckers.server.gamelogic.Move;
import com.jkpr.chinesecheckers.server.gamelogic.boards.Position;


/**
 * Represents the game board for Chinese checkers.
 * <p>
 * The {@code CCBoard} class extends {@code AbstractBoard} and defines the layout and movement rules
 * for the Chinese checkers board. It includes the logic for setting up the board with cells and defining
 * valid movements for players' pieces.
 * </p>
 */
public class CCBoardClient extends AbstractBoardClient {
    private Integer[] playerDistribution=new Integer[6];
    /**
     * Constructs a {@code CCBoard} with the appropriate layout and initial state.
     * <p>
     * This constructor creates the board by populating cells based on predefined movement rules and
     * board coordinates. It also assigns owners to each cell according to the rules of the game.
     * </p>
     */
    public CCBoardClient(int count) {
        //distribution player array
        switch(count)
        {
            case 2:
                playerDistribution[0]=0;
                playerDistribution[3]=1;
                break;
            case 3:
                playerDistribution[0]=0;
                playerDistribution[4]=1;
                playerDistribution[2]=2;
                break;
            case 4:
                playerDistribution[0]=0;
                playerDistribution[4]=1;
                playerDistribution[1]=2;
                playerDistribution[3]=3;
                break;
            case 6:
                playerDistribution[0]=0;
                playerDistribution[1]=1;
                playerDistribution[2]=2;
                playerDistribution[3]=3;
                playerDistribution[4]=4;
                playerDistribution[5]=5;
                break;
            default:
                throw new InvalidNumberOfPlayers("");
        }
        // Board creation
        int cellNumber = 13;
        for (int y = -4; y <= 8; y++) {
            int x = -4;
            for (int k = 0; k < cellNumber; k++) {
                Position pos = new Position(x, y);
                if (!cells.containsKey(pos)) {
                    CellClient cell = new CellClient(pos);
                    cell.setPiece(getPiece(x,y));
                    cells.put(pos, cell);
                }
                x++;
            }
            cellNumber--;
        }

        cellNumber = 13;
        for (int y = 4; y >= -8; y--) {
            int x = 4;
            for (int k = 0; k < cellNumber; k++) {
                Position pos = new Position(x, y);
                if (!cells.containsKey(pos)) {
                    CellClient cell = new CellClient(pos);
                    cell.setPiece(getPiece(x,y));
                    cells.put(pos, cell);
                }
                x--;
            }
            cellNumber--;
        }
    }


    private PieceClient getPiece(int x, int y) {
        if (y < -4) {
            Integer player=playerDistribution[0];
            if(player==null)
                return null;
            else
                return new PieceClient(player);
        }
        else if (y > 4) {
            Integer player=playerDistribution[3];
            if(player==null)
                return null;
            else
                return new PieceClient(player);
        }
        else if (x < -4) {
            Integer player=playerDistribution[4];
            if(player==null)
                return null;
            else
                return new PieceClient(player);
        }
        else if (x > 4) {
            Integer player=playerDistribution[1];
            if(player==null)
                return null;
            else
                return new PieceClient(player);
        }
        else if ( x + y >= 5) {
            Integer player=playerDistribution[2];
            if(player==null)
                return null;
            else
                return new PieceClient(player);
        }
        else if (x + y <= -5) {
            Integer player=playerDistribution[5];
            if(player==null)
                return null;
            else
                return new PieceClient(player);
        }
        else {
            return null;
        }
    }

    @Override
    public void clicked(int x, int y) {
        //todo
    }

    @Override
    public String toString() {
        String s="";
        for(int i=-8;i<=8;i++)
        {
            for(int j=-8;j<=8;j++)
            {
                if(cells.get(new Position(i,j))==null)
                {
                    s.concat(" ");
                }
                else
                {
                    s.concat(cells.get(new Position(i,j)).toString());
                }
            }
        }
        return s;
    }

    @Override
    public void makeMove(Move move) {
        Position start=move.getStart(),end=move.getEnd();
        cells.get(end).setPiece(cells.get(start).getPiece());
        cells.get(start).setPiece(null);
    }
}

