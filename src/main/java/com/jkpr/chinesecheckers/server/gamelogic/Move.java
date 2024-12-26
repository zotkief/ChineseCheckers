package com.jkpr.chinesecheckers.server.gamelogic;

public class Move {
    private Position start;
    private Position end;

    public Move(int xStart,int yStart,int xEnd,int yEnd)
    {
        start=new Position(xStart,yStart);
        end=new Position(xEnd,yEnd);
    }
    public Move(Position start,Position end){
        this.start=start;
        this.end=end;
    }

    public Position getEnd() {
        return end;
    }

    public Position getStart() {
        return start;
    }
    @Override
    public String toString()
    {
        return start.toString()+" "+end.toString();
    }
}
