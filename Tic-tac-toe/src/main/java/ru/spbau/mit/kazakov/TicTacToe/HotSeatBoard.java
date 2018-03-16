package ru.spbau.mit.kazakov.TicTacToe;

import org.jetbrains.annotations.NotNull;

/**
 * Implements logic of game for two players.
 */
public class HotSeatBoard extends AbstractBoard {
    /**
     * @see AbstractBoard#AbstractBoard(int)
     */
    public HotSeatBoard(int size) {
        super(size);
    }

    /**
     * @see Board#move(int, int)
     */
    @NotNull
    @Override
    public String[][] move(int row, int col) {
        board[row][col] = player.toString();
        updateState(row, col);
        player = player.next();
        return board;
    }
}
