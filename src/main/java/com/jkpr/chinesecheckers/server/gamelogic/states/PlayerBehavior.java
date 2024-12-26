package com.jkpr.chinesecheckers.server.gamelogic.states;

public interface PlayerBehavior {
    PlayerState getState();
    PlayerBehavior setWin();
    PlayerBehavior setActive();
    PlayerBehavior setWait();

}
