package com.jkpr.chinesecheckers.server.UI;

public class GameOptions {
    private String gameType;
    private String playerCount;

    public void setData(String gameType, String playerCount) {
        this.gameType = gameType;
        this.playerCount = playerCount;
    }

    public String getGameType() {
        return gameType;
    }

    public String getPlayerCount() {
        return playerCount;
    }
}
