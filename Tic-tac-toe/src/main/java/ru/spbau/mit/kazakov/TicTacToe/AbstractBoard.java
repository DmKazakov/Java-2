package ru.spbau.mit.kazakov.TicTacToe;

import org.jetbrains.annotations.NotNull;

/**
 * An abstract class with base functionality of game logic.
 */
public abstract class AbstractBoard implements Board {
    protected CellContent[][] board;
    protected State state;
    protected Player player;
    private int size;
    private int turnNumber;

    /**
     * Sets board size and creates two-dimensional array for board.
     */
    public AbstractBoard(int size) {
        this.size = size;
        reset();
    }

    /**
     * @see Board#getState()
     */
    @Override
    @NotNull
    public State getState() {
        return state;
    }

    /**
     * Checks last turn changes and updates game state.
     *
     * @param row of last turn
     * @param col of last turn
     */
    public void updateState(int row, int col) {
        turnNumber++;
        boolean horizontalWin = true;
        boolean verticalWin = true;
        boolean firstDiagonalWin = true;
        boolean secondDiagonalWin = true;
        for (int i = 0; i < size; i++) {
            verticalWin &= board[i][col].equals(player.toCellContent());
            horizontalWin &= board[row][i].equals(player.toCellContent());
            firstDiagonalWin &= board[i][i].equals(player.toCellContent());
            secondDiagonalWin &= board[i][size - 1 - i].equals(player.toCellContent());
        }
        if (horizontalWin || verticalWin || firstDiagonalWin || secondDiagonalWin) {
            state = player.getWinState();
        } else if (turnNumber == size * size) {
            state = State.DRAW;
        }
    }

    /**
     * @see Board#reset()
     */
    @Override
    public CellContent[][] reset() {
        board = new CellContent[size][size];
        for (CellContent[] row : board) {
            for (int i = 0; i < size; i++) {
                row[i] = CellContent.EMPTY;
            }
        }
        state = State.RUNNING;
        player = Player.X;
        turnNumber = 0;
        return board;
    }
}
