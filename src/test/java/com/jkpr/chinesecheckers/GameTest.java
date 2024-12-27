package com.jkpr.chinesecheckers;

import com.jkpr.chinesecheckers.server.gamelogic.*;
import com.jkpr.chinesecheckers.server.gamelogic.boards.AbstractBoard;
import com.jkpr.chinesecheckers.server.gamelogic.boards.CCBoard;
import com.jkpr.chinesecheckers.server.message.MoveMessage;
import com.jkpr.chinesecheckers.server.message.UpdateMessage;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class GameTest extends TestCase {
    public static Test suite()
    {
        return new TestSuite( GameTest.class );
    }
    public void testMovesCycle(){
        Game game=new Game();
        AbstractRules rules=new CCRules();
        AbstractBoard board=new CCBoard(3);
        game.setBoard(board);
        game.setRules(rules);

        UpdateMessage updateMessage=game.nextMove(new MoveMessage(1,-5,1,-4),board.getPlayer(0));
        assertEquals("1 -5 1 -4 NEXT_ID 1 WIN_ID null",updateMessage.getContent());

        updateMessage=game.nextMove(new MoveMessage(4,1,3,1),board.getPlayer(1));
        assertEquals("4 1 3 1 NEXT_ID 2 WIN_ID null",updateMessage.getContent());

        updateMessage=game.nextMove(new MoveMessage(-5,4,-4,4),board.getPlayer(2));
        assertEquals("-5 4 -4 4 NEXT_ID 0 WIN_ID null",updateMessage.getContent());
    }
    public void testWin(){
        Game game=new Game();
        AbstractRules rules=new CCRules();
        AbstractBoard board=new CCBoard(3);
        game.setBoard(board);
        game.setRules(rules);

        for(Position pos:board.getCells().keySet())
        {
            if(pos.getY()<-4)
                board.makeMove(new Move(pos,new Position(-1*pos.getX(),-1*pos.getY())));
        }
        board.makeMove(new Move(-1,5,-1,4));

        UpdateMessage updateMessage=game.nextMove(new MoveMessage(-1,4,-1,5),board.getPlayer(0));

        assertEquals("-1 4 -1 5 NEXT_ID 1 WIN_ID 0",updateMessage.getContent());
    }
    public void testEnd(){
        Game game=new Game();
        AbstractRules rules=new CCRules();
        AbstractBoard board=new CCBoard(3);
        game.setBoard(board);
        game.setRules(rules);

        for(Position pos:board.getCells().keySet())
        {
            if(pos.getY()<-4 || pos.getX()<-4 || pos.getX()+pos.getY()>4)
                board.makeMove(new Move(pos,new Position(-1*pos.getX(),-1*pos.getY())));
        }
        board.makeMove(new Move(-1,5,-1,4));
        board.makeMove(new Move(-1,-4,0,-4));
        board.makeMove(new Move(5,-4,4,-4));



        UpdateMessage updateMessage=game.nextMove(new MoveMessage(-1,4,-1,5),board.getPlayer(0));
        assertEquals("-1 4 -1 5 NEXT_ID 1 WIN_ID 0",updateMessage.getContent());

        updateMessage=game.nextMove(new MoveMessage(0,-4,-1,-4),board.getPlayer(1));
        assertEquals("0 -4 -1 -4 NEXT_ID 2 WIN_ID 1 END",updateMessage.getContent());
    }
    public void testSkip(){
        Game game=new Game();
        AbstractRules rules=new CCRules();
        AbstractBoard board=new CCBoard(3);
        game.setBoard(board);
        game.setRules(rules);

        UpdateMessage updateMessage=game.nextMove(new MoveMessage(),board.getPlayer(0));
        assertEquals("SKIP NEXT_ID 1",updateMessage.getContent());
        updateMessage=game.nextMove(new MoveMessage(),board.getPlayer(1));
        assertEquals("SKIP NEXT_ID 2",updateMessage.getContent());
        updateMessage=game.nextMove(new MoveMessage(),board.getPlayer(2));
        assertEquals("SKIP NEXT_ID 0",updateMessage.getContent());
        updateMessage=game.nextMove(new MoveMessage(),board.getPlayer(0));
        assertEquals("SKIP NEXT_ID 1",updateMessage.getContent());
    }
}