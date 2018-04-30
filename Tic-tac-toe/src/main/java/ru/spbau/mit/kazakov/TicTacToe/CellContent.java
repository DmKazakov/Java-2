package ru.spbau.mit.kazakov.TicTacToe;

import org.jetbrains.annotations.NotNull;

/**
 * Enum for describing content of a cell.
 */
public enum CellContent {
    X, O,
    EMPTY {
        @Override
        @NotNull
        public String toString() {
            return " ";
        }
    }
}
