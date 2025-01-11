package com.jkpr.chinesecheckers.server.message;

public class GenMessage extends Message{
    private final String content;
    public GenMessage(String content) {
        super(MessageType.GEN);
        this.content=content;
    }
    public String getContent(){return content;}
    @Override
    public String serialize() {
        return MessageType.GEN+" "+content;
    }
}
