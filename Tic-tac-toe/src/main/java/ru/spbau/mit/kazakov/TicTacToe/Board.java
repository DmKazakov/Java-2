package ru.spbau.mit.kazakov.TicTacToe;

import org.jetbrains.annotations.NotNull;

public interface Board {
    /**
     * Makes specified turn on the board and updates state of game.
     *
     * @param row of turn
     * @param col of turn
     * @return new state of board
     */
    @NotNull
    CellContent[][] move(int row, int col);

    /**
     * Returns current state of the game.
     */
    @NotNull
    State getState();

    /**
     * Sets board to starting state.
     */
    @NotNull
    CellContent[][] reset();
}
