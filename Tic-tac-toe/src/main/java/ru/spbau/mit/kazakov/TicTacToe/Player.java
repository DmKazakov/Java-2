package ru.spbau.mit.kazakov.TicTacToe;

/**
 * Enum for describing current turn's owner.
 */
public enum Player {
    X, O;

    /**
     * Returns owner of the next turn.
     */
    public Player next() {
        return this == X ? O : X;
    }

    /**
     * Returns content of cell corresponding to current turn's owner.
     */
    public CellContent toCellContent() {
        return this == X ? CellContent.X : CellContent.O;
    }

    /**
     * Returns state of game corresponding to victory of current turn's owner.
     */
    public State getWinState() {
        return this == X ? State.X_WINS : State.O_WINS;
    }
}
