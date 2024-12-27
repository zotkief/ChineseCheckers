package com.jkpr.chinesecheckers.server.gamelogic.boards;

import com.jkpr.chinesecheckers.server.exceptions.InvalidNumberOfPlayers;
import com.jkpr.chinesecheckers.server.gamelogic.*;
import com.jkpr.chinesecheckers.server.gamelogic.states.PlayerState;

import java.util.ArrayList;
import java.util.List;

public class CCBoard extends AbstractBoard {
    private int count;
    public CCBoard(int count) {
        this.count=count;
        playerDistribution=new Player[6];
    }
    public void generate(AbstractRules rules)
    {
        //setting players
        for(int i=0;i<count;i++)
            players.add(new Player(i));
        players.get(0).setActive();
        //distribution player array
        switch(count)
        {
            case 2:
                playerDistribution[0]=players.get(0);
                playerDistribution[3]=players.get(1);
                break;
            case 3:
                playerDistribution[0]=players.get(0);
                playerDistribution[4]=players.get(2);
                playerDistribution[2]=players.get(1);
                break;
            case 4:
                playerDistribution[0]=players.get(0);
                playerDistribution[4]=players.get(1);
                playerDistribution[1]=players.get(2);
                playerDistribution[3]=players.get(3);
                break;
            case 6:
                playerDistribution[0]=players.get(0);
                playerDistribution[1]=players.get(1);
                playerDistribution[2]=players.get(2);
                playerDistribution[3]=players.get(3);
                playerDistribution[4]=players.get(4);
                playerDistribution[5]=players.get(5);
                break;
            default:
                throw new InvalidNumberOfPlayers("");
        }
        // Movement possibilities
        movements.add(new Position(-1, 0));
        movements.add(new Position(1, 0));
        movements.add(new Position(0, -1));
        movements.add(new Position(0, 1));
        movements.add(new Position(1, -1));
        movements.add(new Position(-1, 1));

        // Board creation
        int cellNumber = 13;
        for (int y = -4; y <= 8; y++) {
            int x = -4;
            for (int k = 0; k < cellNumber; k++) {
                Position pos = new Position(x, y);
                if (!cells.containsKey(pos)) {
                    Cell cell = rules.configureCell(pos,playerDistribution);
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
                    Cell cell = rules.configureCell(pos,playerDistribution);
                    cells.put(pos, cell);
                }
                x--;
            }
            cellNumber--;
        }
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

    @Override
    public int setStates(List<Player> winners,Player player){
        for(Player player1:winners)
            player1.setWin();

        //choosing next player
        Player tempRef=getPlayer((player.getId()+1)%getNumberOfPlayers());
        while(!tempRef.getState().equals(PlayerState.WAIT))
            tempRef=getPlayer((player.getId()+1)%getNumberOfPlayers());
        tempRef.setActive();

        return tempRef.getId();
    }

    @Override
    public boolean compareCell(Position position, Player player) {
        return cells.containsKey(position) && cells.get(position).checkPlayer(player);
    }
}

