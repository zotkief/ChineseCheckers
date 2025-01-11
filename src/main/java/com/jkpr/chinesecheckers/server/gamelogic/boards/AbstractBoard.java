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

    private final ArrayList<Player> winners=new ArrayList<>();
    public abstract void setMovements();
    public Map<Position, Cell> getCells(){return cells;}
    public List<Position> getMovements(){return movements;}
    public abstract void makeMove(Move move);
    public boolean compareCell(Position position, Player player) {
        return cells.containsKey(position) && cells.get(position).checkPlayer(player);
    }
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
