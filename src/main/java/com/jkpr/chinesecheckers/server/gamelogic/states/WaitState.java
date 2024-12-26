package com.jkpr.chinesecheckers.server.gamelogic.states;

public class WaitState implements PlayerBehavior{
    @Override
    public PlayerState getState() {
        return PlayerState.WAIT;
    }

    @Override
    public PlayerBehavior setWin() {
        return this;
    }

    @Override
    public PlayerBehavior setActive() {
        return PlayerState.ACTIVE.getState();
    }

    @Override
    public PlayerBehavior setWait() {
        return this;
    }
}
