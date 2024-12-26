package com.jkpr.chinesecheckers.server;

import java.util.*;

import com.jkpr.chinesecheckers.server.gamelogic.CCBuilder;
import com.jkpr.chinesecheckers.server.gamelogic.Game;
import com.jkpr.chinesecheckers.server.gamelogic.Player;
import com.jkpr.chinesecheckers.server.message.*;

public class GameAdapter {
    private Server server;
    private List<ClientHandler> clients = new ArrayList<>();
    private HashMap<ClientHandler, Player> clientHandlerPlayerHashMap;
    private Game game;
    public GameAdapter(ClientHandler[] players, Server server){
        game=Director.createGame(new CCBuilder(),players.length);
        clientHandlerPlayerHashMap=new HashMap<>();
        this.clients=Arrays.asList(players);

        this.server=server;
        for(ClientHandler clientHandler:players)
        {
            addPlayer(clientHandler);
            clientHandler.assignGameAdapter(this);
        }

    }
    public void addPlayer(ClientHandler clientHandler)
    {
        clientHandlerPlayerHashMap.put(clientHandler,game.join());
    }
    public UpdateMessage processMove(MoveMessage move,ClientHandler clientHandler){
        return game.nextMove(move,clientHandlerPlayerHashMap.get(clientHandler));
    }
    public void brodcastMessage (MoveMessage move, ClientHandler clientHandler){
        UpdateMessage updateMessage=game.nextMove(move,clientHandlerPlayerHashMap.get(clientHandler));
        for(ClientHandler player:clients){
            //System.out.println("Sending message to "+player.getPlayerId());
            player.sendMessage(updateMessage);
        }
    }
    public int getPlayerId(ClientHandler clientHandler)
    {
        return clientHandlerPlayerHashMap.get(clientHandler).getId();
    }
}
