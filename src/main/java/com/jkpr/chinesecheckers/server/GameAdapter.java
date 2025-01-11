package com.jkpr.chinesecheckers.server;

import java.util.*;

import com.jkpr.chinesecheckers.server.UI.GameOptions;
import com.jkpr.chinesecheckers.server.gamelogic.*;
import com.jkpr.chinesecheckers.server.gamelogic.builders.CCBuilder;
import com.jkpr.chinesecheckers.server.gamelogic.builders.Director;
import com.jkpr.chinesecheckers.server.gamelogic.builders.FastPacedBuilder;
import com.jkpr.chinesecheckers.server.gamelogic.builders.YYBuilder;
import com.jkpr.chinesecheckers.server.message.*;

public class GameAdapter {
    private List<ClientHandler> clients = new ArrayList<>();
    private final HashMap<ClientHandler, Player> clientHandlerPlayerHashMap;
    private final Game game;
    public GameAdapter(ClientHandler[] players, Server server, GameOptions options){
        switch(options.getGameType())
        {
            case "Fast Paced":
                game= Director.createGame(new FastPacedBuilder(),players.length);
                break;
            case "Yin and Yang":
                game=Director.createGame(new YYBuilder(),0);
                break;
            default:
                game=Director.createGame(new CCBuilder(),players.length);
        }

        game.generate();
        clientHandlerPlayerHashMap=new HashMap<>();
        this.clients=Arrays.asList(players);

        for(ClientHandler clientHandler:players)
        {
            addPlayer(clientHandler);
            clientHandler.assignGameAdapter(this);

            int id=clientHandlerPlayerHashMap.get(clientHandler).getId();
            GenMessage message = new GenMessage(game.getGenMessage()+id);
            clientHandler.sendMessage(message);
            UpdateMessage message1=new UpdateMessage("SKIP NEXT_ID 0");
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
