package com.jkpr.chinesecheckers.client.boards;

import com.jkpr.chinesecheckers.server.gamelogic.boards.Position;

public class YYBoardClient extends AbstractBoardClient{
    private final Integer[] playerDistribution=new Integer[6];
    public YYBoardClient(int enemy,int id) {
        super(id, 2);
        playerDistribution[0]=0;
        playerDistribution[enemy]=1;
    }
    @Override
    public void generateBoard(){
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
}
