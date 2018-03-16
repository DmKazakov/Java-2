package ru.spbau.mit.kazakov.TicTacToe;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Implements logic of game with AI.
 */
public class AIBoard extends AbstractBoard {
    private AI AI;

    /**
     * Calls parent's constructor and initializes AI.
     *
     * @param size  of board
     * @param level of AI
     */
    public AIBoard(int size, @NotNull AILevel level) {
        super(size);
        switch (level) {
            case EASY:
                AI = new EasyAI();
                break;
            case MEDIUM:
                AI = new MediumAI();
                break;
        }
    }

    /**
     * @see Board#move(int, int)
     */
    @NotNull
    @Override
    public String[][] move(int row, int col) {
        board[row][col] = player.toString();
        updateState(row, col);
        if (state != State.RUNNING) {
            return board;
        }
        player = player.next();
        AI.makeTurn();
        player = player.next();
        return board;
    }

    /**
     * AI witch makes random moves.
     */
    private class EasyAI implements AI {
        /**
         * Chooses random free cell and makes turn.
         */
        @Override
        public void makeTurn() {
            int freeCellsNumber = 0;
            for (String[] row : board) {
                for (String cell : row) {
                    if (cell.equals(" ")) {
                        freeCellsNumber++;
                    }
                }
            }
            int turn = ThreadLocalRandom.current().nextInt(0, freeCellsNumber);
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board.length; j++) {
                    if (board[i][j].equals(" ") && turn-- == 0) {
                        board[i][j] = player.toString();
                        updateState(i, j);
                        return;
                    }
                }
            }
        }
    }

    /**
     * AI witch prevents losing on the next turn and wins this turn if it's possible.
     */
    private class MediumAI implements AI {
        /**
         * Checks if specified line from the board leads to lose in one turn.
         *
         * @param turns specified line from the board
         */
        private boolean canLose(String turns) {
            return turns.matches(player.next().toString() + "*" + " " + player.next().toString() + "*");
        }

        /**
         * Checks if specified line from the board leads to victory in one turn.
         *
         * @param turns specified line from the board
         */
        private boolean canWin(String turns) {
            return turns.matches(player.toString() + "*" + " " + player.toString() + "*");
        }

        /**
         * Tries to win or not to lose this turn.
         */
        @Override
        public void makeTurn() {
            int defendRow = -1;
            int defendCol = -1;

            for (int i = 0; i < board.length; i++) {
                StringBuilder row = new StringBuilder();
                int rowFreeCell = 0, colFreeCell = 0;
                StringBuilder col = new StringBuilder();
                for (int j = 0; j < board.length; j++) {
                    if (board[i][j].equals(" ")) {
                        rowFreeCell = j;
                    }
                    row.append(board[i][j]);
                    if (board[j][i].equals(" ")) {
                        colFreeCell = j;
                    }
                    col.append(board[j][i]);
                }
                if (canWin(row.toString())) {
                    board[i][rowFreeCell] = player.toString();
                    updateState(i, rowFreeCell);
                    return;
                } else if (canWin(col.toString())) {
                    board[colFreeCell][i] = player.toString();
                    updateState(colFreeCell, i);
                    return;
                } else if (canLose(row.toString())) {
                    defendRow = i;
                    defendCol = rowFreeCell;
                } else if (canLose(col.toString())) {
                    defendRow = colFreeCell;
                    defendCol = i;
                }
            }

            int firstDiagonalFreeCell = 0, secondDiagonalFreeCell = 0;
            StringBuilder firstDiagonal = new StringBuilder();
            StringBuilder secondDiagonal = new StringBuilder();
            for (int i = 0; i < board.length; i++) {
                if (board[i][i].equals(" ")) {
                    firstDiagonalFreeCell = i;
                }
                if (board[i][board.length - 1 - i].equals(" ")) {
                    secondDiagonalFreeCell = i;
                }
                firstDiagonal.append(board[i][i]);
                secondDiagonal.append(board[i][board.length - 1 - i]);
            }
            if (canWin(firstDiagonal.toString())) {
                board[firstDiagonalFreeCell][firstDiagonalFreeCell] = player.toString();
                updateState(firstDiagonalFreeCell, firstDiagonalFreeCell);
                return;
            } else if (canWin(secondDiagonal.toString())) {
                board[secondDiagonalFreeCell][board.length - 1 - secondDiagonalFreeCell] = player.toString();
                updateState(secondDiagonalFreeCell, board.length - 1 - secondDiagonalFreeCell);
                return;
            } else if (canLose(firstDiagonal.toString())) {
                defendCol = firstDiagonalFreeCell;
                defendRow = firstDiagonalFreeCell;
            } else if (canLose(secondDiagonal.toString())) {
                defendCol = board.length - 1 - secondDiagonalFreeCell;
                defendRow = secondDiagonalFreeCell;
            }

            if (defendCol != -1) {
                board[defendRow][defendCol] = player.toString();
                updateState(defendRow, defendCol);
                return;
            }
            new EasyAI().makeTurn();
        }
    }
}
