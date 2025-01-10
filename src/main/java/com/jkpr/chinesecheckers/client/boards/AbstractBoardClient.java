package com.jkpr.chinesecheckers.client.boards;

import com.jkpr.chinesecheckers.server.gamelogic.Move;
import com.jkpr.chinesecheckers.server.gamelogic.Player;
import com.jkpr.chinesecheckers.server.gamelogic.boards.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the abstract concept of a game board.
 * <p>
 * The {@code AbstractBoard} class serves as a base for defining game boards in different variants of the game.
 * It maintains a map of cells, each identified by a {@code Position}, and a list of valid movement directions.
 * Subclasses are expected to implement specific rules for validating moves and managing the board state.
 * </p>
 */
public abstract class AbstractBoardClient {

    /** A map of all the cells on the board, where the key is the position of the cell. */
    protected Map<Position, CellClient> cells = new HashMap<Position, CellClient>();


    protected List<Player> players=new ArrayList<Player>();

    public abstract void clicked(int x,int y);
    public List<Player> getPlayers(){return players;}
    public Map<Position, CellClient> getCells(){return cells;}
    public abstract String toString();
    public abstract void makeMove(Move move);
    public Player getPlayer(int id){return players.get(id);}
    public int getNumberOfPlayers(){return players.size();}

}
