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
     * @param size of board
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
        if(state != State.RUNNING) {
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
            for(String[] row : board) {
                for(String cell : row) {
                    if(cell.equals(" ")) {
                        freeCellsNumber++;
                    }
                }
            }
            int turn = ThreadLocalRandom.current().nextInt(0, freeCellsNumber);
            for(int i = 0; i < board.length; i++) {
                for(int j = 0; j < board.length; j++) {
                    if(board[i][j].equals(" ") && turn-- == 0) {
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
         * Checks if specified line from the board leads to losing or victory in one turn.
         * @param turns specified line from the board
         */
        private boolean canLoseOrWin(String turns) {
            return turns.matches(player.toString() + "*" + " " + player.toString() + "*") ||
                    turns.matches(player.next().toString() + "*" + " " + player.next().toString() + "*");
        }

        /**
         * Tries to win or not to lose this turn.
         */
        @Override
        public void makeTurn() {
            for(int i = 0; i < board.length; i++) {
                StringBuilder row = new StringBuilder();
                int rowFreeCell = 0, colFreeCell = 0;
                StringBuilder col = new StringBuilder();
                for(int j = 0; j < board.length; j++) {
                    if(board[i][j].equals(" ")) {
                        rowFreeCell = j;
                    }
                    row.append(board[i][j]);
                    if(board[j][i].equals(" ")) {
                        colFreeCell = j;
                    }
                    col.append(board[j][i]);
                }
                if(canLoseOrWin(row.toString())) {
                    board[i][rowFreeCell] = player.toString();
                    updateState(i, rowFreeCell);
                    return;
                }
                if(canLoseOrWin(col.toString())) {
                    board[colFreeCell][i] = player.toString();
                    updateState(colFreeCell, i);
                    return;
                }
            }

            int firstDiagonalFreeCell = 0, secondDiagonalFreeCell = 0;
            StringBuilder firstDiagonal = new StringBuilder();
            StringBuilder secondDiagonal = new StringBuilder();
            for(int i = 0; i < board.length; i++) {
                if(board[i][i].equals(" ")) {
                    firstDiagonalFreeCell = i;
                }
                if(board[i][board.length - 1 - i].equals(" ")) {
                    secondDiagonalFreeCell = i;
                }
                firstDiagonal.append(board[i][i]);
                secondDiagonal.append(board[i][board.length - 1 - i]);
            }
            if(canLoseOrWin(firstDiagonal.toString())) {
                move(firstDiagonalFreeCell, firstDiagonalFreeCell);
            }
            if(canLoseOrWin(secondDiagonal.toString())) {
                move(secondDiagonalFreeCell, board.length - 1 - secondDiagonalFreeCell);
            }
            new EasyAI().makeTurn();
        }
    }
}
