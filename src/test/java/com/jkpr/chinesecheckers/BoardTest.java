package com.jkpr.chinesecheckers;

import com.jkpr.chinesecheckers.server.gamelogic.*;
import com.jkpr.chinesecheckers.server.gamelogic.boards.AbstractBoard;
import com.jkpr.chinesecheckers.server.gamelogic.boards.CCBoard;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class BoardTest extends TestCase {
    public static Test suite()
    {
        return new TestSuite( BoardTest.class );
    }
    public void testGen(){
        AbstractRules rules=new CCRules();
        //2 players
        AbstractBoard board=new CCBoard(2);
        board.generate(rules);
        Player testOwner=board.getCells().get(new Position(3,-6)).getPiece().getOwner();
        Player expected=board.getPlayer(0);
        assertEquals(expected,testOwner);

        testOwner=board.getCells().get(new Position(-3,6)).getPiece().getOwner();
        expected=board.getPlayer(1);
        assertEquals(expected,testOwner);

        //3 players
        board=new CCBoard(3);
        board.generate(rules);
        testOwner=board.getCells().get(new Position(3,-6)).getPiece().getOwner();
        expected=board.getPlayer(0);
        assertEquals(expected,testOwner);

        testOwner=board.getCells().get(new Position(3,3)).getPiece().getOwner();
        expected=board.getPlayer(1);
        assertEquals(expected,testOwner);

        testOwner=board.getCells().get(new Position(-6,3)).getPiece().getOwner();
        expected=board.getPlayer(2);
        assertEquals(expected,testOwner);

        //4 players
        board=new CCBoard(4);
        board.generate(rules);
        testOwner=board.getCells().get(new Position(3,-6)).getPiece().getOwner();
        expected=board.getPlayer(0);
        assertEquals(expected,testOwner);

        testOwner=board.getCells().get(new Position(-3,6)).getPiece().getOwner();
        expected=board.getPlayer(3);
        assertEquals(expected,testOwner);

        testOwner=board.getCells().get(new Position(6,-3)).getPiece().getOwner();
        expected=board.getPlayer(2);
        assertEquals(expected,testOwner);

        testOwner=board.getCells().get(new Position(-6,3)).getPiece().getOwner();
        expected=board.getPlayer(1);
        assertEquals(expected,testOwner);

        //6 players
        board=new CCBoard(6);
        board.generate(rules);
        testOwner=board.getCells().get(new Position(3,3)).getPiece().getOwner();
        expected=board.getPlayer(2);
        assertEquals(expected,testOwner);

        testOwner=board.getCells().get(new Position(-3,-3)).getPiece().getOwner();
        expected=board.getPlayer(5);
        assertEquals(expected,testOwner);

        testOwner=board.getCells().get(new Position(-3,6)).getPiece().getOwner();
        expected=board.getPlayer(3);
        assertEquals(expected,testOwner);

        testOwner=board.getCells().get(new Position(3,-6)).getPiece().getOwner();
        expected=board.getPlayer(0);
        assertEquals(expected,testOwner);

        testOwner=board.getCells().get(new Position(6,-3)).getPiece().getOwner();
        expected=board.getPlayer(1);
        assertEquals(expected,testOwner);

        testOwner=board.getCells().get(new Position(-6,3)).getPiece().getOwner();
        expected=board.getPlayer(4);
        assertEquals(expected,testOwner);

    }
    public void testBasicMove()
    {
        AbstractRules rules=new CCRules();
        AbstractBoard board=new CCBoard(2);
        board.generate(rules);
        assertNotSame("FAIL",rules.isValidMove(board, board.getPlayer(0), new Move(3,-5,3,-4)).getContent());
    }
    public void testInvalidMove() {
        AbstractRules rules = new CCRules();
        AbstractBoard board = new CCBoard(2);
        board.generate(rules);
        board.makeMove(new Move(3,-5,3,-4));
        assertEquals("FAIL",rules.isValidMove(board, board.getPlayer(0), new Move(3,-5,3,-4)).getContent());
    }
    public void testExtendedMove() {
        AbstractRules rules = new CCRules();
        AbstractBoard board = new CCBoard(2);
        board.generate(rules);
        board.makeMove(new Move(3,-5,3,-4));
        assertNotSame("FAIL",rules.isValidMove(board, board.getPlayer(0), new Move(4,-5,2,-3)).getContent());
    }
    public void testDoubleMove() {
        AbstractRules rules = new CCRules();
        AbstractBoard board = new CCBoard(2);
        board.generate(rules);
        board.makeMove(new Move(3,-5,3,-4));
        board.makeMove(new Move(2,-5,2,-2));
        assertNotSame("FAIL",rules.isValidMove(board, board.getPlayer(0), new Move(4,-5,2,-1)).getContent());
    }
    public void testExitingTriangle() {
        AbstractRules rules = new CCRules();
        AbstractBoard board = new CCBoard(2);
        board.generate(rules);
        board.makeMove(new Move(1,-5,-1,5));
        assertEquals("FAIL",rules.isValidMove(board, board.getPlayer(0), new Move(-1,5,-1,4)).getContent());
    }

}
