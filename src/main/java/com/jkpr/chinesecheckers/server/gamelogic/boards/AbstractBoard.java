package com.jkpr.chinesecheckers.server.gamelogic.boards;

import com.jkpr.chinesecheckers.server.gamelogic.*;
import com.jkpr.chinesecheckers.server.gamelogic.rules.AbstractRules;
import com.jkpr.chinesecheckers.server.gamelogic.states.PlayerState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public abstract class AbstractBoard {
    public AbstractBoard(){
        setMovements();
    }
    protected Map<Position, Cell> cells = new HashMap<Position, Cell>();

    protected List<Position> movements = new ArrayList<>();

    protected List<Player> players=new ArrayList<Player>();

    protected Player[] playerDistribution;

    private final ArrayList<Player> winners=new ArrayList<>();
    public abstract void setMovements();
    public List<Player> getPlayers(){return players;}
    public Map<Position, Cell> getCells(){return cells;}
    public List<Position> getMovements(){return movements;}
    public abstract void makeMove(Move move);
    public Player getPlayer(int id){return players.get(id);}
    public int getNumberOfPlayers(){return players.size();}
    public int setStates(List<Player> winners,Player player){
        for(Player player1:winners)
            player1.setWin();

        player.setWait();

        //choosing next player
        Player tempRef=getPlayer((player.getId()+1)%getNumberOfPlayers());
        while(!tempRef.getState().equals(PlayerState.WAIT))
        {
            tempRef=getPlayer((player.getId()+1)%getNumberOfPlayers());
            System.out.println(tempRef.getState());
        }
        tempRef.setActive();

        return tempRef.getId();
    }
    public boolean compareCell(Position position, Player player) {
        return cells.containsKey(position) && cells.get(position).checkPlayer(player);
    }
    public Player[] getDistribution(){return playerDistribution;}
    public int getWinnersNumber(){return winners.size();}
    public abstract void generate(AbstractRules rules);
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
