package com.jkpr.chinesecheckers.server.message;


public class UpdateMessage extends Message {
    public final String content;

    public UpdateMessage(String content) {
        super(MessageType.UPDATE);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String serialize() {
        return getType().name() +" " + content;
    }

    public static UpdateMessage fromContent(String content) {
        //TODO: zastanowic sie co trzeba wysylac
        // musi być: czy ktoś wygrał, kto wygrał, kto się teraz ma ruszać, czy ruch nastąpił i jeżeli tak to jaki
        return new UpdateMessage(content);
    }

}
