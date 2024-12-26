package com.jkpr.chinesecheckers.server.gamelogic;

import com.jkpr.chinesecheckers.server.gamelogic.boards.AbstractBoard;
import com.jkpr.chinesecheckers.server.message.MoveMessage;
import com.jkpr.chinesecheckers.server.message.UpdateMessage;
import com.jkpr.chinesecheckers.server.gamelogic.states.PlayerState;

/**
 * Represents the game of Chinese checkers.
 * <p>
 * The {@code Game} class manages the players and the board. It also processes the moves made during the game,
 * validating them against the current state of the board.
 * </p>
 * <p>
 * The class uses a {@code HashMap} to store players, where the key is an integer representing the player's ID.
 * The game board is managed by an instance of {@code AbstractBoard}.
 * </p>
 */
public class Game {

    /**
     * A map of players in the game, keyed by player ID.
     * <p>
     * The {@code HashMap} holds player objects, where the key is the player's unique ID and the value is
     * an instance of {@code AbstractPlayer} representing the player.
     * </p>
     */
    //TODO dodawać graczy do tej hashmapy
    // wydaje mi się że lepiej będzie to zrobić zaraz przy inicjacji game, a potem przypisywać tylko kolejnym
    // klientom obiekty
    private int playersCount=0;
    private int maxPlayers;

    /** The game board. */
    private AbstractBoard board;
    private AbstractRules rules;
    private int winningPlayers=0;

    /**
     * Processes the next move in the game.
     * <p>
     * This method receives a string message representing the move, and it is responsible for extracting
     * the necessary information such as the player making the move and the starting and ending positions
     * on the board. The move is then validated by calling {@code isValidMove} on the {@code AbstractBoard}.
     * </p>
     *
     * <p><b>Note:</b> The player is assumed to be the one currently active in the game.</p>
     *
     */
    public UpdateMessage nextMove(MoveMessage message, Player player) {
        //TODO można to inaczej zrobić i trochę przenieść do board, ale przemyśle jaką mam wizję i to zrobie
        // tak żeby miało to sens logiczny
        System.out.println(message.serialize());
        if(player.getState()!=PlayerState.ACTIVE)
            return UpdateMessage.fromContent("FAIL");
        //first system checks whether player skipped or not
        //if its true then it changes states of players
        if(message.getSkip())
        {
            return UpdateMessage.fromContent("SKIP NEXT_ID "+board.setStates(false,player));
        }
        //otherwise it checks whether player inserted valid move
        boolean validation=rules.isValidMove(board,player,message.getMove());
        //if so, it proceeds to complete this move
        if(validation)
        {
            board.makeMove(message.getMove());
            String winPlayer="null";
            //if player won
            boolean win=board.checkIfWon(player);
            if(win)
            {
                winPlayer=String.valueOf(player.getId());
                winningPlayers++;
            }

            //FORMAT: "ruch" NEXT_ID "id kolejnego" WIN_ID "id wygranego" (jeżeli nikt to null)
            String output=message.getMove()+" NEXT_ID "+board.setStates(win,player) +" WIN_ID "+winPlayer;
            if(winningPlayers==board.getNumberOfPlayers()-1)
            {
                output+=" END";
            }
            return UpdateMessage.fromContent(output);
        }
        else
        {
            return UpdateMessage.fromContent("FAIL");
        }
    }

    /**
     * Sets the game board.
     * <p>
     * This method assigns a new {@code AbstractBoard} to the game, which will be used to validate moves
     * and track the positions of the game pieces.
     * </p>
     *
     * @param board the {@code AbstractBoard} to set for the game
     */
    public void setBoard(AbstractBoard board) {
        this.board = board;
    }

    /**
     * Sets the players for the game.
     * <p>
     * This method assigns a {@code HashMap} of players to the game. The map should contain player IDs as keys
     * and {@code AbstractPlayer} objects as values, representing all players participating in the game.
     * </p>
     *
     */
    public void setRules(AbstractRules rules){this.rules=rules;}
    public Player join(){
        return board.getPlayers().get(playersCount++);
    }
}

