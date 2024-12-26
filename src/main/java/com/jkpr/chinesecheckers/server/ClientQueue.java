package com.jkpr.chinesecheckers.server;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ClientQueue {
    private final BlockingQueue<ClientHandler> waitingClients = new LinkedBlockingQueue<>();

    public void addClient(ClientHandler client) {
        waitingClients.add(client);
    }

    public ClientHandler takeClient() throws InterruptedException {
        return waitingClients.take();
    }

    public ClientHandler pollClient(long timeout, TimeUnit unit) throws InterruptedException {
        return waitingClients.poll(timeout, unit);
    }

    public boolean removeClient(ClientHandler client) {
        return waitingClients.remove(client);
    }

    public int getWaitingClientsCount() {
        return waitingClients.size();
    }
}
