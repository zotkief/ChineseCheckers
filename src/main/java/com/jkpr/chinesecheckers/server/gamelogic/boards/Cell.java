package com.jkpr.chinesecheckers.server.gamelogic.boards;

import com.jkpr.chinesecheckers.server.gamelogic.Piece;
import com.jkpr.chinesecheckers.server.gamelogic.Player;
import com.jkpr.chinesecheckers.server.gamelogic.Position;

import java.util.List;


public class Cell {

    private final Position position;

    private Piece piece;

    private List<Player> owners;

    public Cell(Position position, List<Player> owners) {
        this.position = position;
        this.owners = owners;
    }
    public void setPiece(Piece piece)
    {
        this.piece=piece;
    }

    public List<Player> getOwners() {
        return owners;
    }

    public boolean isEmpty() {
        return piece == null;
    }


    public boolean checkPlayer(Player player) {
        return piece != null && piece.getOwner().equals(player);
    }

    public Piece getPiece() {
        return piece;
    }

    @Override
    public String toString() {
        if(piece==null)
            return ".";
        return piece.toString();
    }
}
