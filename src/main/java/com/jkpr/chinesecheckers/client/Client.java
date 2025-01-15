package com.jkpr.chinesecheckers.client;

import com.jkpr.chinesecheckers.client.boards.AbstractBoardClient;
import com.jkpr.chinesecheckers.client.boards.factory.CCBoardFactory;
import com.jkpr.chinesecheckers.client.boards.factory.YYBoardFactory;
import com.jkpr.chinesecheckers.server.gamelogic.Move;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.function.Consumer;
/**
  * The {@code Client} class represents a client that connects to the server and sends and receives messages.
  * It is responsible for handling the communication between the server and the client.
  */
public class Client {
    /**
     * The {@code socket} field represents the socket that the client uses to connect to the server.
     */
    private Socket socket;
    private Scanner scanner;
    private PrintWriter out;
    private Scanner in;
    /**
     * The {@code board} field represents the board that the client uses to play the game.
     */
    private AbstractBoardClient board;
    /**
     * The {@code onBoardGenerated} field represents the action that is performed when the board is generated.
     */
    private Consumer<AbstractBoardClient> onBoardGenerated;
    /**
     * The {@code onBoardUpdate} field represents the action that is performed when the board is updated.
     */
    private Runnable onBoardUpdate;
    /**
     * The {@code Client} constructor initializes the client by creating a socket and connecting to the server.
     */
    public Client() {
        try {
            socket = new Socket("localhost", 12345);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new Scanner(socket.getInputStream());
            scanner = new Scanner(System.in); // do czytanie wpisu z konsoli klienta
            System.out.println("polaczono z serwerem");
        } catch (IOException e) {
            System.err.println("blad polaczenia z serwerem: " + e.getMessage());
        }
    }
    public static void main(String[] args){
        Client client = new Client();
        client.start();
    }
    /**
     * The {@code start} method starts the client by creating a new thread that receives messages from the server.
     */
    public void start() {
        new Thread(this::receiveMessages).start();
    }
    /**
     * The {@code sendMessage} method sends a message to the server.
     * @param input the message that is sent to the server
     */
    public void sendMessage(String input){
        out.println(input);
        out.flush();
    }
    /**
     * The {@code setOnBoardGenerated} method sets the action that is performed when the board is generated.
     * @param onBoardGenerated the action that is performed when the board is generated
     */
    public void setOnBoardGenerated(Consumer<AbstractBoardClient> onBoardGenerated) {
        this.onBoardGenerated = onBoardGenerated;
    }
    /**
     * The {@code setOnBoardUpdate} method sets the action that is performed when the board is updated.
     * @param onBoardUpdate the action that is performed when the board is updated
     */
    public void setOnBoardUpdate(Runnable onBoardUpdate) {
        this.onBoardUpdate = onBoardUpdate;
    }

    /**
     * The {@code receiveMessages} method receives messages from the server and processes them.
     */
    private void receiveMessages() {
        while (in.hasNextLine()) {
            String linia = in.nextLine();
            System.out.println("odebrano: " + linia);
            String[] message=linia.split(" ");
            switch (message[0]){
                case "GEN":
                    switch (message[1])
                    {
                        case "CC":
                            board=new CCBoardFactory().generate(Integer.parseInt(message[3]),Integer.parseInt(message[2]));
                            break;
                        case "YY":
                            int enemy=Integer.parseInt(message[2]);
                            int id=Integer.parseInt(message[3]);
                            board=new YYBoardFactory().generate(id,enemy);
                            break;
                    }
                    if(onBoardGenerated!=null){
                        System.out.println("wygenerowano plansze w cliencie");
                        onBoardGenerated.accept(board);
                    }
                    break;
                case "UPDATE":
                    switch (message[1]){
                        case "SKIP":
                            board.processNext(Integer.parseInt(message[3]));
                            break;
                        case "FAIL":
                            System.out.println("niepoprawny ruch");
                            break;
                        default:
                            Move move=new Move(
                                    Integer.parseInt(message[1]),
                                    Integer.parseInt(message[2]),
                                    Integer.parseInt(message[3]),
                                    Integer.parseInt(message[4]));
                            board.makeMove(move);
                            board.processNext(Integer.parseInt(message[6]));
                            int i=8;
                            while(i<message.length){
                                board.processWin(Integer.parseInt(message[i]));
                                i++;
                            }
                    }
                    if(onBoardUpdate != null){
                        onBoardUpdate.run();
                    }
                    break;
            }
        }
    }
}
