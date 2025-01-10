package com.jkpr.chinesecheckers.server.gamelogic.rules;

import com.jkpr.chinesecheckers.server.exceptions.InvalidNumberOfPlayers;
import com.jkpr.chinesecheckers.server.gamelogic.Move;
import com.jkpr.chinesecheckers.server.gamelogic.boards.Piece;
import com.jkpr.chinesecheckers.server.gamelogic.Player;
import com.jkpr.chinesecheckers.server.gamelogic.boards.Position;
import com.jkpr.chinesecheckers.server.gamelogic.boards.AbstractBoard;
import com.jkpr.chinesecheckers.server.gamelogic.boards.Cell;
import com.jkpr.chinesecheckers.server.gamelogic.states.PlayerState;
import com.jkpr.chinesecheckers.server.message.UpdateMessage;

import java.util.ArrayList;
import java.util.List;

public class FastPacedRules extends AbstractRules {
    @Override
    public UpdateMessage isValidMove(AbstractBoard board, Player player, Move move) {
        Position start = move.getStart(), destination = move.getEnd();
        if (!player.getState().equals(PlayerState.ACTIVE))
            return UpdateMessage.fromContent("FAIL");

        if (board.getCells().containsKey(start) && board.getCells().get(start).checkPlayer(player)) {

            if(checkWinner(start,player, board.getDistribution()) && !checkWinner(destination,player, board.getDistribution()))
                return UpdateMessage.fromContent("FAIL");

            List<Position> possibilities = new ArrayList<>();
            findPossibilities(board, possibilities, player, start);


            if (possibilities.contains(destination)) {
                board.makeMove(move);
                //if player won
                List<Player> winners = controlWinners(board);
                winners=board.addPlayers(winners);

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
            }
        }
        findNetwork(board,alreadyVisited,player,start);
    }

    private void findNetwork(AbstractBoard board, List<Position> alreadyVisited, Player player, Position position) {
        for (Position move : board.getMovements()) {
            Position potentialMove = new Position(position, move);
            int jumps=1;
            while(isMoveLegal(board,potentialMove,player))
            {
                potentialMove=new Position(potentialMove,move);
                jumps++;
            }

            boolean valid=true;
            for(int i=0;i<jumps;i++)
            {
                potentialMove=new Position(potentialMove,move);
                if(!isMoveLegal(board,potentialMove,player))
                {
                    valid=false;
                    break;
                }
            }

            if(valid && !alreadyVisited.contains(potentialMove))
            {
                System.out.println(" "+!alreadyVisited.contains(potentialMove)+" "+potentialMove);
                System.out.println(isMoveLegal(board,potentialMove,player));
                alreadyVisited.add(potentialMove);
                findNetwork(board,alreadyVisited,player,potentialMove);
            }
        }
    }

    private boolean isMoveLegal(AbstractBoard board, Position position, Player player) {
        return board.getCells().containsKey(position)
                && board.getCells().get(position).isEmpty()
                && board.getCells().get(position).getOwners().contains(player);
    }

    @Override
    public Cell configureCell(Position position, Player[] distribution, List<Player> players) {
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
            if (y < -4 && board.compareCell(pos, distribution[3]))
                winningPieces[3]++;
            else if (y > 4 && board.compareCell(pos, distribution[0]))
                winningPieces[0]++;
            else if (x < -4 && board.compareCell(pos, distribution[1]))
                winningPieces[1]++;
            else if (x > 4 && board.compareCell(pos, distribution[4]))
                winningPieces[4]++;
            else if (x + y >= 5 && board.compareCell(pos, distribution[5]))
                winningPieces[5]++;
            else if (x + y <= -5 && board.compareCell(pos, distribution[2]))
                winningPieces[2]++;
        }
        for(int i=0;i<6;i++)
            if(winningPieces[i]==10)
                winners.add(distribution[i]);
        return winners;
    }
    private boolean checkWinner(Position pos,Player player,Player[] distribution){
        int y=pos.getY(),x= pos.getX();
        if (y < -4 && player.equals(distribution[3]))
            return true;
        else if (y > 4 && player.equals(distribution[0]))
            return true;
        else if (x < -4 && player.equals(distribution[1]))
            return true;
        else if (x > 4 && player.equals(distribution[4]))
            return true;
        else if (x + y >= 5 && player.equals(distribution[5]))
            return true;
        else return x + y <= -5 && player.equals(distribution[2]);
    }

    @Override
    public void configureDistribution(AbstractBoard board, List<Player> players) {
        int count=players.size();
        switch(count)
        {
            case 2:
                board.getDistribution()[0]=players.get(0);
                board.getDistribution()[3]=players.get(1);
                break;
            case 3:
                board.getDistribution()[0]=players.get(0);
                board.getDistribution()[4]=players.get(2);
                board.getDistribution()[2]=players.get(1);
                break;
            case 4:
                board.getDistribution()[0]=players.get(0);
                board.getDistribution()[4]=players.get(1);
                board.getDistribution()[1]=players.get(2);
                board.getDistribution()[3]=players.get(3);
                break;
            case 6:
                board.getDistribution()[0]=players.get(0);
                board.getDistribution()[1]=players.get(1);
                board.getDistribution()[2]=players.get(2);
                board.getDistribution()[3]=players.get(3);
                board.getDistribution()[4]=players.get(4);
                board.getDistribution()[5]=players.get(5);
                break;
            default:
                throw new InvalidNumberOfPlayers("");
        }
    }
}
