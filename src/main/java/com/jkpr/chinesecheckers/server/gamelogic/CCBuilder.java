package com.jkpr.chinesecheckers.server.gamelogic;

import com.jkpr.chinesecheckers.server.GameBuilder;
import com.jkpr.chinesecheckers.server.gamelogic.boards.CCBoard;

/**
 * The {@code CCBuilder} class is a concrete implementation of the {@code GameBuilder} interface
 * for constructing a Chinese checkers game.
 * <p>
 * This class follows the builder design pattern and is responsible for setting up the game board and players,
 * returning a fully constructed {@code Game} object specifically for Chinese checkers.
 * </p>
 */
public class CCBuilder implements GameBuilder {

    /** The game instance being constructed. */
    private final Game game = new Game();

    /**
     * Sets up the board for a Chinese checkers game.
     * <p>
     * This method assigns a {@code CCBoard} to the game, which is a specialized board for Chinese checkers.
     * </p>
     */
    @Override
    public void setBoard(int count) {
        game.setBoard(new CCBoard(count));
    }

    @Override
    public void setRules(){game.setRules(new CCRules());}

    /**
     * Returns the fully constructed Chinese checkers game.
     * <p>
     * After the board and players have been set up, this method returns the completed {@code Game} object.
     * </p>
     *
     * @return the constructed {@code Game} for Chinese checkers
     */
    @Override
    public Game getGame() {
        return game;
    }
}
