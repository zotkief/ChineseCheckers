package com.jkpr.chinesecheckers.server.message;

public class GenMessage extends Message{
    private final String content;
    public GenMessage(String type,int players,int playerId) {
        super(MessageType.GEN);
        content=type+" "+players+" "+playerId;
    }
    public String getContent(){return content;}
    @Override
    public String serialize() {
        return MessageType.GEN+" "+content;
    }
}
