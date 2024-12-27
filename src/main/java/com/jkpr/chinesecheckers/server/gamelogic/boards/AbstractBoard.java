package com.jkpr.chinesecheckers.server.gamelogic.boards;

import com.jkpr.chinesecheckers.server.gamelogic.Move;
import com.jkpr.chinesecheckers.server.gamelogic.Piece;
import com.jkpr.chinesecheckers.server.gamelogic.Player;
import com.jkpr.chinesecheckers.server.gamelogic.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class AbstractBoard {

    protected Map<Position, Cell> cells = new HashMap<Position, Cell>();

    protected List<Position> movements = new ArrayList<>();

    protected List<Player> players=new ArrayList<Player>();

    protected Player[] playerDistribution;

    private final ArrayList<Player> winners=new ArrayList<>();

    public List<Player> getPlayers(){return players;}
    public Map<Position, Cell> getCells(){return cells;}
    public List<Position> getMovements(){return movements;}
    public abstract String toString();
    public abstract void makeMove(Move move);
    public Player getPlayer(int id){return players.get(id);}
    public int getNumberOfPlayers(){return players.size();}
    public abstract int setStates(List<Player> winners,Player player);
    public abstract boolean compareCell(Position position,Player player);
    public Player[] getDistribution(){return playerDistribution;}
    public int getWinnersNumber(){return winners.size();}
    public List<Player> addPlayers(List<Player> adder){
        List<Player> result=new ArrayList<>();
        for(Player player:adder)
        {
            if(!winners.contains(player))
            {
                winners.add(player);
                result.add(player);
            }
        }
        return result;
    }
}
