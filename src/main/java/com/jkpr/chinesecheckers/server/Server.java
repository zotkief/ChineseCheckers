package com.jkpr.chinesecheckers.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.jkpr.chinesecheckers.server.UI.GameOptions;
import com.jkpr.chinesecheckers.server.UI.ServerWindow;
import com.jkpr.chinesecheckers.server.message.*;

public class Server {
    private static final int PORT = 12345;          //port serwera
    private ServerSocket serverSocket;              //socket serwera
    private ExecutorService threadPool;             //pula watkow do odpalania watkow dla klientow
    private volatile boolean isRunning = true;             //flaga czy serwer dziala
    private ClientHandler[] players;
    private Scanner scanner;

    private GameOptions startUI(){
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Server Window");
        config.setWindowedMode(500, 500);
        config.setResizable(false);
        GameOptions options=new GameOptions();
        new Lwjgl3Application(new ServerWindow(options), config);
        return options;
    }
    public void startServer() {
        try {
            GameOptions options=startUI();


            serverSocket = new ServerSocket(PORT);
            threadPool = Executors.newCachedThreadPool();
            System.out.println("serwer wystartowal, zaczynam sluchac na " + PORT);
            //Tworzenie gry <-- stary kod GameCreationManager
            System.out.println("Podaj ilosc graczy: 2, 3, 4 lub 6");
            scanner = new Scanner(System.in);
            int numberOfPlayers = scanner.nextInt();
            if(numberOfPlayers <2 || numberOfPlayers > 6|| numberOfPlayers == 5){
                //System.out.println("Niepoprawna liczba graczy");
                return;
            }
            System.out.println("czekam na " + numberOfPlayers + " graczy");
            //-------------------
            //czekaj na wejscie odpowiedniej ilosci graczy
            players = new ClientHandler[numberOfPlayers];
            int connectedPlayers = 0;
            while(connectedPlayers < numberOfPlayers&& isRunning){
                Socket clientSocket = serverSocket.accept();
                System.out.println("akceptuje polaczenie od " + clientSocket.getInetAddress());

                ClientHandler handler = new ClientHandler(clientSocket);
                System.out.println("dodaje gracza " +  handler.getPlayerId() + "do kolejki");
                players[connectedPlayers] = handler;
                threadPool.execute(handler);
                connectedPlayers++;
            }
            //odpal sesje gry
            System.out.println("wszyscy gracze dolaczyli, tworze gre");
            GameAdapter gameAdapter = new GameAdapter(players, this);
            for (ClientHandler handler : players) {
                handler.assignGameAdapter(gameAdapter);
                GenMessage message = new GenMessage("CC",numberOfPlayers,gameAdapter.getPlayerId(handler));
                handler.sendMessage(message);
            }
            while(true){
            }
        } catch (IOException e) {
            System.err.println("blad serwera, nie moge wystartowac na porcie " + PORT);
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        Server server = new Server();
        server.startServer();
    }
}