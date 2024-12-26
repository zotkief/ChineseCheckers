package com.jkpr.chinesecheckers.server.gamelogic;

import com.jkpr.chinesecheckers.server.gamelogic.boards.AbstractBoard;

public abstract class AbstractRules {
    public abstract boolean isValidMove(AbstractBoard board, Player player,Move move);
}
