package com.jkpr.chinesecheckers.server.gamelogic.builders;

import com.jkpr.chinesecheckers.server.gamelogic.rules.FastPacedRules;
import com.jkpr.chinesecheckers.server.gamelogic.Game;
import com.jkpr.chinesecheckers.server.gamelogic.boards.CCBoard;

public class FastPacedBuilder extends GameBuilder {
    @Override
    public void setBoard(int count) {
        game.setBoard(new CCBoard(count));
    }

    @Override
    public void setRules() {
        game.setRules(new FastPacedRules());
    }

    @Override
    public Game getGame() {
        return null;
    }
}
