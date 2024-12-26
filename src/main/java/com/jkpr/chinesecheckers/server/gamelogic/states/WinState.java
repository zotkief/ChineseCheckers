package com.jkpr.chinesecheckers.server.gamelogic.states;

public class WinState implements PlayerBehavior{
    @Override
    public PlayerState getState() {
        return PlayerState.WIN;
    }

    @Override
    public PlayerBehavior setWin() {
        return this;
    }

    @Override
    public PlayerBehavior setActive() {
        return this;
    }

    @Override
    public PlayerBehavior setWait() {
        return this;
    }
}
