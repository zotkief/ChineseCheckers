package com.jkpr.chinesecheckers.server.gamelogic;

import com.jkpr.chinesecheckers.server.gamelogic.boards.AbstractBoard;
import com.jkpr.chinesecheckers.server.gamelogic.boards.Cell;
import com.jkpr.chinesecheckers.server.gamelogic.states.PlayerState;
import com.jkpr.chinesecheckers.server.message.UpdateMessage;

import java.util.ArrayList;
import java.util.List;

public class CCRules extends AbstractRules {
    @Override
    public UpdateMessage isValidMove(AbstractBoard board, Player player, Move move) {
        Position start = move.getStart(), destination = move.getEnd();
        if (!player.getState().equals(PlayerState.ACTIVE))
            return UpdateMessage.fromContent("FAIL");


        if (board.getCells().containsKey(start) && board.getCells().get(start).checkPlayer(player)) {
            //todo zrobić tak żeby nie dało się wyjść z trójkąta wtedy return false
            List<Position> possibilities = new ArrayList<>();
            findPossibilities(board, possibilities, player, start);


            //if player won
            List<Player> winners = controlWinners(board);
            winners=board.addPlayers(winners);

            board.makeMove(move);

            if (possibilities.contains(destination)) {
                return getMessageStructure(move,board,winners,player);
            }
        }
        return UpdateMessage.fromContent("FAIL");
    }

    private void findPossibilities(AbstractBoard board, List<Position> alreadyVisited, Player player, Position start) {
        for (Position move : board.getMovements()) {
            Position potentialMove = new Position(start, move);
            if (isMoveLegal(board, potentialMove, player)) {
                if (!alreadyVisited.contains(potentialMove)) {
                    alreadyVisited.add(new Position(start, move));
                }
            } else {
                potentialMove = new Position(potentialMove, move);
                if (!alreadyVisited.contains(potentialMove) && isMoveLegal(board, potentialMove, player)) {
                    alreadyVisited.add(potentialMove);
                    findNetwork(board, alreadyVisited, player, potentialMove);
                }
            }
        }
    }

    private void findNetwork(AbstractBoard board, List<Position> alreadyVisited, Player player, Position position) {
        for (Position move : board.getMovements()) {
            Position potentialMove = new Position(position, move);
            if (isMoveLegal(board, potentialMove, player))
                continue;
            potentialMove = new Position(potentialMove, move);

            if (!alreadyVisited.contains(potentialMove) && isMoveLegal(board, potentialMove, player)) {
                alreadyVisited.add(potentialMove);
                findNetwork(board, alreadyVisited, player, potentialMove);
            }
        }
    }

    private boolean isMoveLegal(AbstractBoard board, Position position, Player player) {
        return board.getCells().containsKey(position)
                && board.getCells().get(position).isEmpty()
                && board.getCells().get(position).getOwners().contains(player);
    }

    @Override
    public Cell configureCell(Position position, Player[] distribution) {
        Cell cell = new Cell(position, getOwners(position.getX(), position.getY(), distribution));
        cell.setPiece(getPiece(position.getX(), position.getY(), distribution));
        return cell;
    }


    private Piece getPiece(int x, int y, Player[] distribution) {
        if (y < -4) {
            Player player = distribution[0];
            if (player == null)
                return null;
            else
                return new Piece(player);
        } else if (y > 4) {
            Player player = distribution[3];
            if (player == null)
                return null;
            else
                return new Piece(player);
        } else if (x < -4) {
            Player player = distribution[4];
            if (player == null)
                return null;
            else
                return new Piece(player);
        } else if (x > 4) {
            Player player = distribution[1];
            if (player == null)
                return null;
            else
                return new Piece(player);
        } else if (x + y >= 5) {
            Player player = distribution[2];
            if (player == null)
                return null;
            else
                return new Piece(player);
        } else if (x + y <= -5) {
            Player player = distribution[5];
            if (player == null)
                return null;
            else
                return new Piece(player);
        } else {
            return null;
        }
    }

    private List<Player> getOwners(int x, int y, Player[] distribution) {
        List<Player> list = new ArrayList<>();
        if (y < -4 || y > 4) {
            list.add(distribution[0]);
            list.add(distribution[3]);
        } else if (x < -4 || x > 4) {
            list.add(distribution[1]);
            list.add(distribution[4]);
        } else if (x + y <= -5 || x + y >= 5) {
            list.add(distribution[2]);
            list.add(distribution[5]);
        } else {
            list.add(distribution[0]);
            list.add(distribution[1]);
            list.add(distribution[2]);
            list.add(distribution[3]);
            list.add(distribution[4]);
            list.add(distribution[5]);
        }
        return list;
    }

    private UpdateMessage getMessageStructure(Move move,AbstractBoard board,List<Player> winners,Player currentPlayer) {
        //FORMAT: "ruch" NEXT_ID "id kolejnego" WIN_ID "id wygranego" (jeżeli nikt to null)
        String output = move + " NEXT_ID " + board.setStates(winners,currentPlayer) + " WIN_ID";
        for(Player player:winners)
        {
            output+=" "+player.getId();
        }
        if (board.getWinnersNumber() == board.getNumberOfPlayers() - 1) {
            output += " END";
        }
        return UpdateMessage.fromContent(output);
    }

    private List<Player> controlWinners(AbstractBoard board) {
        Player[] distribution= board.getDistribution();
        int[] winningPieces = new int[6];
        List<Player> winners=new ArrayList<>();
        for (Position pos : board.getCells().keySet()) {
            int y = pos.getY(), x = pos.getX();
            if (y < -4 && board.compareCell(pos, distribution[0]))
                winningPieces[0]++;
            else if (y > 4 && board.compareCell(pos, distribution[3]))
                winningPieces[3]++;
            else if (x < -4 && board.compareCell(pos, distribution[4]))
                winningPieces[4]++;
            else if (x > 4 && board.compareCell(pos, distribution[1]))
                winningPieces[1]++;
            else if (x + y >= 5 && board.compareCell(pos, distribution[2]))
                winningPieces[2]++;
            else if (x + y <= -5 && board.compareCell(pos, distribution[5]))
                winningPieces[5]++;
        }
        for(int i=0;i<6;i++)
            if(winningPieces[i]==10)
                winners.add(distribution[i]);
        return winners;
    }
}
