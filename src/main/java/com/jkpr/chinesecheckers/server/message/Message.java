package com.jkpr.chinesecheckers.server.message;

public abstract class Message {
    private final MessageType type;
    public Message(MessageType type) {
        this.type = type;
    }
    public MessageType getType() {
        return type;
    }
    //pars na konwencje "TYP <kontent>"
    public abstract String serialize();
    //jezeli dostanie "TYP <kontent>" to zwraca obiekt z odpowiednim typem i kontentem
    public static Message fromString(String line){
        int spaceIndex = line.indexOf(' ');
        if(spaceIndex == -1){
            throw new IllegalArgumentException("nie prawidlowy format wiadomosci");
        }
        String type = line.substring(0, spaceIndex).trim();
        String content = line.substring(spaceIndex+1).trim();
        MessageType messageType;
        try{
            messageType = MessageType.valueOf(type.toUpperCase());
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("nie prawidlowy format wiadomosci");
        }

        switch (messageType){
            case MOVE:
                return MoveMessage.fromContent(content);
            case UPDATE:
                return UpdateMessage.fromContent(content);
            default:
                throw new IllegalArgumentException("nie prawidlowy format wiadomosci");
        }
    }


}
