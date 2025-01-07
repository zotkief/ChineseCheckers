package com.jkpr.chinesecheckers.server.gamelogic;

import com.jkpr.chinesecheckers.server.gamelogic.boards.AbstractBoard;
import com.jkpr.chinesecheckers.server.message.MoveMessage;
import com.jkpr.chinesecheckers.server.message.UpdateMessage;
import com.jkpr.chinesecheckers.server.gamelogic.states.PlayerState;

import java.util.ArrayList;

public class Game {
    private int playersCount=0;

    /** The game board. */
    private AbstractBoard board;
    private AbstractRules rules;
    private int winningPlayers=0;

    public UpdateMessage nextMove(MoveMessage message, Player player) {
        System.out.println("id"+player.getId()+" "+player.getState());
        if(player.getState()!=PlayerState.ACTIVE)
            return UpdateMessage.fromContent("FAIL");
        //first system checks whether player skipped or not
        //if its true then it changes states of players
        if(message.getSkip())
        {
            return UpdateMessage.fromContent("SKIP NEXT_ID "+board.setStates(new ArrayList<>(),player));
        }
        System.out.println(message.serialize());
        //if so, it proceeds to complete this move
        return rules.isValidMove(board,player,message.getMove());
    }

    public void setBoard(AbstractBoard board) {
        this.board = board;
    }

    public void setRules(AbstractRules rules){this.rules=rules;}
    public Player join(){
        return board.getPlayers().get(playersCount++);
    }
    public void generate(){
        board.generate(rules);
    }
}

