package com.jkpr.chinesecheckers.server;

import java.io.*;
import java.net.Socket;
import com.jkpr.chinesecheckers.server.message.*;
import java.util.UUID;
import java.util.Scanner;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private PrintWriter out;
    private Scanner in;
    private GameAdapter gameAdapter;
    private String playerId;

    public ClientHandler(Socket clientSocket){
        this.clientSocket = clientSocket;
        this.playerId = UUID.randomUUID().toString();
    }

    @Override
    public void run() {
        try{
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new Scanner(clientSocket.getInputStream());
            while(true){
                System.out.println("oczekiwanie na wiadomosc od " + playerId);
                String linia = in.nextLine().trim();
                if(linia.isEmpty()){
                    //System.out.println("pusta wiadomosc od " + playerId);
                    continue;
                }
                Message message;
                try {
                    message = Message.fromString(linia);
                }catch (Exception e)
                {continue;}

                if(message.getType() == MessageType.MOVE){
                    MoveMessage msg = (MoveMessage) message;
                    //System.out.println("odebrano wiadomosc MOVE od " + playerId + ": " + msg.serialize());
                    //wyslij wiadomosc do wszystkich graczy
                    if(gameAdapter == null){
                        //System.out.println("gracz nie jest przypisany do sesji gry");
                        continue;
                    } else {
                        gameAdapter.brodcastMessage(msg, this);
                    }
                }
                else{
                    //System.out.println("nieznany typ wiadomosci");
                }
            }
        } catch (IOException e){
            System.err.println("blad odczytu wiadomosci");
            e.printStackTrace();
        } finally {
            cleanUp();
        }
    }

    public void sendMessage(Message message) {
        try{
            out.println(message.serialize());
            out.flush();
        } catch (Exception e) {
            System.err.println("blad wysylania wiadomosci");
            e.printStackTrace();
        }
    }

    public String getPlayerId() {
        return playerId;
    }

    public void assignGameAdapter(GameAdapter gameAdapter) {
        this.gameAdapter = gameAdapter;
    }
    public void closeConnection() {
        try{
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("blad zamykania gniazda klienta");
            e.printStackTrace();
        }
    }
    private void cleanUp() {
        try{
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("blad zamykania gniazda klienta");
            e.printStackTrace();
        }
    }
}