package com.jkpr.chinesecheckers.server.gamelogic.builders;

import com.jkpr.chinesecheckers.server.gamelogic.Game;
import com.jkpr.chinesecheckers.server.gamelogic.rules.YYRules;
import com.jkpr.chinesecheckers.server.gamelogic.boards.CCBoard;

public class YYBuilder extends GameBuilder {
    @Override
    public void setBoard(int count) {
        game.setBoard(new CCBoard(2));
    }

    @Override
    public void setRules() {
        game.setRules(new YYRules());
    }

    @Override
    public Game getGame() {
        return null;
    }
}
