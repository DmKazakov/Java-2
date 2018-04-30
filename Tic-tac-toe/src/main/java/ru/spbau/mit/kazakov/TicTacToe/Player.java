package ru.spbau.mit.kazakov.TicTacToe;

import org.jetbrains.annotations.NotNull;

/**
 * Enum for describing current turn's owner.
 */
public enum Player {
    X, O;

    /**
     * Returns owner of the next turn.
     */
    @NotNull
    public Player next() {
        return this == X ? O : X;
    }

    /**
     * Returns content of cell corresponding to current turn's owner.
     */
    @NotNull
    public CellContent toCellContent() {
        return this == X ? CellContent.X : CellContent.O;
    }

    /**
     * Returns state of game corresponding to victory of current turn's owner.
     */
    @NotNull
    public State getWinState() {
        return this == X ? State.X_WINS : State.O_WINS;
    }
}
