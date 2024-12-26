package com.jkpr.chinesecheckers.client;

import com.jkpr.chinesecheckers.client.boards.AbstractBoardClient;
import com.jkpr.chinesecheckers.client.boards.CCBoardClient;
import com.jkpr.chinesecheckers.server.gamelogic.Move;
import com.jkpr.chinesecheckers.server.gamelogic.Player;

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
            if(message[0].equals("GEN")){
                switch (message[1])
                {
                    case "CC":
                        board=new CCBoardClient(Integer.parseInt(message[2]));
                        break;
                }
            }
        }
    }
}