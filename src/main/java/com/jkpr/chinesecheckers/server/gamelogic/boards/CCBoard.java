package com.jkpr.chinesecheckers.server.gamelogic.boards;

import com.jkpr.chinesecheckers.server.gamelogic.*;
import com.jkpr.chinesecheckers.server.gamelogic.rules.AbstractRules;

public class CCBoard extends AbstractBoard {

    @Override
    public void generate(AbstractRules rules)
    {
        rules.configureDistribution(this);
        // Board creation
        int cellNumber = 13;
        for (int y = -4; y <= 8; y++) {
            int x = -4;
            for (int k = 0; k < cellNumber; k++) {
                Position pos = new Position(x, y);
                if (!cells.containsKey(pos)) {
                    Cell cell = rules.configureCell(pos);
                    cells.put(pos, cell);
                }
                x++;
            }
            cellNumber--;
        }

        cellNumber = 13;
        for (int y = 4; y >= -8; y--) {
            int x = 4;
            for (int k = 0; k < cellNumber; k++) {
                Position pos = new Position(x, y);
                if (!cells.containsKey(pos)) {
                    Cell cell = rules.configureCell(pos);
                    cells.put(pos, cell);
                }
                x--;
            }
            cellNumber--;
        }
    }

    @Override
    public void setMovements() {
        movements.clear();
        // Movement possibilities
        movements.add(new Position(-1, 0));
        movements.add(new Position(1, 0));
        movements.add(new Position(0, -1));
        movements.add(new Position(0, 1));
        movements.add(new Position(1, -1));
        movements.add(new Position(-1, 1));

    }

    @Override
    public void makeMove(Move move) {
        Position start = move.getStart(), end = move.getEnd();
        cells.get(end).setPiece(cells.get(start).getPiece());
        cells.get(start).setPiece(null);
    }
}

