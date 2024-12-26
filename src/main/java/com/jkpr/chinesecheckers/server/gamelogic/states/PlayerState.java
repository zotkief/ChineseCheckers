package com.jkpr.chinesecheckers.server.gamelogic.states;

public enum PlayerState {
    WIN {
        public PlayerBehavior getState() {
            return new WinState();
        }
    },
    ACTIVE {
        public PlayerBehavior getState() {
            return new ActiveState();
        }
    },
    WAIT {
        public PlayerBehavior getState() {
            return new WaitState();
        }
    };

    public PlayerBehavior getState() {
        return null;
    }
}
