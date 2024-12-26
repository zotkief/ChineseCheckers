package com.jkpr.chinesecheckers.server.gamelogic.boards;

import com.jkpr.chinesecheckers.server.gamelogic.Piece;
import com.jkpr.chinesecheckers.server.gamelogic.Player;
import com.jkpr.chinesecheckers.server.gamelogic.Position;

import java.util.List;

/**
 * Represents a single cell on the game board in Chinese checkers.
 * <p>
 * Each {@code Cell} holds a {@code Position} that identifies its location on the board,
 * a {@code Piece} that may or may not occupy the cell, and a list of {@code AbstractPlayer}s
 * who own the cell.
 * </p>
 */
public class Cell {

    /** The position of this cell on the game board. */
    private final Position position;

    /** The piece currently placed in this cell, or {@code null} if the cell is empty. */
    private Piece piece;

    /** The list of players who own this cell. */
    private List<Player> owners;
    private Player winner;

    /**
     * Constructs a {@code Cell} with a given position and a list of owners.
     * <p>
     * This initializes the cell with its position and the list of players who own the cell.
     * Initially, the cell does not contain any piece.
     * </p>
     *
     * @param position the position of the cell on the board
     * @param owners the list of players who own this cell
     */
    public Cell(Position position, List<Player> owners) {
        this.position = position;
        this.owners = owners;
    }
    public void setPiece(Piece piece)
    {
        this.piece=piece;
    }
    /**
     * Returns the list of players who own this cell.
     * <p>
     * A cell can have multiple owners, depending on the game rules.
     * </p>
     *
     * @return the list of owners of this cell
     */
    public List<Player> getOwners() {
        return owners;
    }

    /**
     * Checks if the cell is empty.
     * <p>
     * A cell is considered empty if there is no {@code Piece} placed in it.
     * </p>
     *
     * @return {@code true} if the cell is empty, {@code false} otherwise
     */
    public boolean isEmpty() {
        return piece == null;
    }

    /**
     * Checks if the specified player owns the piece currently in this cell.
     * <p>
     * This method compares the owner of the piece in this cell with the provided player.
     * If the piece belongs to the player, the method returns {@code true}.
     * </p>
     *
     * @param player the player to check ownership against
     * @return {@code true} if the player owns the piece in this cell, {@code false} otherwise
     */
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
    public void setWinner(Player player){
        winner=player;
    }

    public Player getWinner() {
        return winner;
    }
}
