package com.jkpr.chinesecheckers.server.gamelogic.builders;

import com.jkpr.chinesecheckers.server.gamelogic.Game;
import com.jkpr.chinesecheckers.server.gamelogic.rules.YYRules;
import com.jkpr.chinesecheckers.server.gamelogic.boards.CCBoard;

public class YYBuilder extends GameBuilder {
    @Override
    public void setBoard() {
        game.setBoard(new CCBoard());
    }

    @Override
    public void setRules(int count) {
        game.setRules(new YYRules());
    }

    @Override
    public Game getGame() {
        return game;
    }
}
