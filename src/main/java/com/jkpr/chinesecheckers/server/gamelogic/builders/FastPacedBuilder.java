package com.jkpr.chinesecheckers.server.gamelogic.builders;

import com.jkpr.chinesecheckers.server.gamelogic.rules.FastPacedRules;
import com.jkpr.chinesecheckers.server.gamelogic.Game;
import com.jkpr.chinesecheckers.server.gamelogic.boards.CCBoard;

public class FastPacedBuilder extends GameBuilder {
    @Override
    public void setBoard() {
        game.setBoard(new CCBoard());
    }

    @Override
    public void setRules(int count) {
        game.setRules(new FastPacedRules(count));
    }

    @Override
    public Game getGame() {
        return game;
    }
}
