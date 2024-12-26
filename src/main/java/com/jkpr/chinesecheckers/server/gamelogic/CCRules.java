package com.jkpr.chinesecheckers.server.gamelogic;

import com.jkpr.chinesecheckers.server.gamelogic.boards.AbstractBoard;
import com.jkpr.chinesecheckers.server.gamelogic.states.PlayerState;

import java.util.ArrayList;
import java.util.List;

public class CCRules extends AbstractRules{
    @Override
    public boolean isValidMove(AbstractBoard board, Player player, Move move) {
        Position start=move.getStart(),destination=move.getEnd();
        if (!player.getState().equals(PlayerState.ACTIVE))
            return false;
        if (board.getCells().containsKey(start) && board.getCells().get(start).checkPlayer(player)) {
            if(player.equals(board.getCells().get(start).getWinner())
                    && !player.equals(board.getCells().get(destination).getWinner()))
            {
                return false;
            }
            List<Position> possibilities = new ArrayList<>();
            findPossibilities(board,possibilities, player, start);
            return possibilities.contains(destination);
        }
        return false;
    }

    /**
     * Recursively finds all possible valid moves from a given starting position.
     * <p>
     * This method explores all legal moves from the start position by recursively checking all potential
     * moves in the directions defined by the {@code movements} list. It adds valid moves to the
     * {@code alreadyVisited} list.
     * </p>
     *
     * @param alreadyVisited the list of positions that have already been visited
     * @param player the player for whom the moves are being checked
     * @param start the starting position of the move
     */
    private void findPossibilities(AbstractBoard board,List<Position> alreadyVisited, Player player, Position start) {
        for (Position move : board.getMovements()) {
            Position potentialMove = new Position(start, move);
            if (isMoveLegal(board,potentialMove, player)) {
                if(!alreadyVisited.contains(potentialMove)) {
                    alreadyVisited.add(new Position(start, move));
                }
            } else {
                potentialMove = new Position(potentialMove, move);
                if (!alreadyVisited.contains(potentialMove) && isMoveLegal(board,potentialMove, player)) {
                    alreadyVisited.add(potentialMove);
                    findNetwork(board, alreadyVisited, player, potentialMove);
                }
            }
        }
    }
    private void findNetwork(AbstractBoard board,List<Position> alreadyVisited,Player player,Position position) {
        for(Position move:board.getMovements())
        {
            Position potentialMove=new Position(position,move);
            if(isMoveLegal(board,potentialMove, player))
                continue;
            potentialMove=new Position(potentialMove,move);

            if(!alreadyVisited.contains(potentialMove) && isMoveLegal(board,potentialMove, player))
            {
                alreadyVisited.add(potentialMove);
                findNetwork(board, alreadyVisited, player, potentialMove);
            }
        }
    }

    /**
     * Checks if a move to a specific position is legal for a given player.
     * <p>
     * This method checks if the destination position exists on the board, is empty, and is owned by the player.
     * </p>
     *
     * @param position the destination position
     * @param player the player making the move
     * @return {@code true} if the move is legal, {@code false} otherwise
     */
    private boolean isMoveLegal(AbstractBoard board,Position position, Player player) {
        return board.getCells().containsKey(position)
                && board.getCells().get(position).isEmpty()
                && board.getCells().get(position).getOwners().contains(player);
    }
}
