package com.jkpr.chinesecheckers.server.gamelogic.states;

public class ActiveState implements PlayerBehavior{
    @Override
    public PlayerState getState() {
        return PlayerState.ACTIVE;
    }

    @Override
    public PlayerBehavior setWin() {
        return PlayerState.WIN.getState();
    }

    @Override
    public PlayerBehavior setActive() {
        return this;
    }

    @Override
    public PlayerBehavior setWait() {
        return PlayerState.WAIT.getState();
    }
}
