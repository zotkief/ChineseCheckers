package com.jkpr.chinesecheckers.client;

import com.jkpr.chinesecheckers.client.boards.AbstractBoardClient;
import com.jkpr.chinesecheckers.client.boards.CCBoardClient;
import com.jkpr.chinesecheckers.client.boards.YYBoardClient;
import com.jkpr.chinesecheckers.server.gamelogic.Move;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Client {
    private Socket socket;
    private Scanner scanner;
    private PrintWriter out;
    private Scanner in;
    private AbstractBoardClient board;

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
    public void start() {
        new Thread(this::receiveMessages).start();
        handleUserInput();
    }

    private void handleUserInput () {
        while (true) {
            System.out.println("wpisz ruch (q1,r1 q2,r2)");
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                continue;
            }
            out.println("MOVE " + input);
            out.flush();
        }
    }



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
                            board=new CCBoardClient(Integer.parseInt(message[2]),Integer.parseInt(message[3]));
                            break;
                        case "YY":
                            board=new YYBoardClient(Integer.parseInt(message[2]),Integer.parseInt(message[3]));
                            break;
                    }
                    break;
                case "UPDATE":
                    switch (message[1]){
                        case "SKIP":
                            board.processNext(Integer.parseInt(message[3]));
                            break;
                        case "FAIL":
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
                    break;
            }
            board.reloadGraphic();
        }
    }
}