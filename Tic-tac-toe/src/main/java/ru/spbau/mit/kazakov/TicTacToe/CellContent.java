package ru.spbau.mit.kazakov.TicTacToe;

/**
 * Enum for describing content of a cell.
 */
public enum CellContent {
    X, O,
    EMPTY {
        @Override
        public String toString() {
            return " ";
        }
    }
}
