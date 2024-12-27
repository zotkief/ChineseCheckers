package com.jkpr.chinesecheckers.server.gamelogic;

import com.jkpr.chinesecheckers.server.gamelogic.boards.AbstractBoard;
import com.jkpr.chinesecheckers.server.gamelogic.boards.Cell;
import com.jkpr.chinesecheckers.server.message.UpdateMessage;

import java.util.List;

public abstract class AbstractRules {
    public abstract UpdateMessage isValidMove(AbstractBoard board, Player player, Move move);
    public abstract Cell configureCell(Position position,Player[] distribution);
}
