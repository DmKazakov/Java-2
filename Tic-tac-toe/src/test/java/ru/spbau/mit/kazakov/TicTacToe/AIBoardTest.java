package ru.spbau.mit.kazakov.TicTacToe;

import org.junit.Test;

import static org.junit.Assert.*;

public class AIBoardTest {
    @Test
    public void testEasyLevelConstructor() {
        new AIBoard(10, AILevel.EASY);
    }

    @Test
    public void testMediumLevelConstructor() {
        new AIBoard(10, AILevel.MEDIUM);
    }

    @Test
    public void testMakeTurnEasyAI() {
        Board board = new AIBoard(3, AILevel.EASY);
        String[][] boardState = board.move(2, 1);
        int diff = 0;
        for (int i = 0; i < boardState.length; i++) {
            for (int j = 0; j < boardState.length; j++) {
                if (!boardState[i][j].equals(" ") && !(i == 2 && j == 1)) {
                    assertEquals("O", boardState[i][j]);
                    diff++;
                }
            }
        }
        assertEquals(1, diff);
        assertEquals("X", boardState[2][1]);
    }

    @Test
    public void testMakeTurnMediumAI() {
        Board board = new AIBoard(3, AILevel.EASY);
        String[][] boardState = board.move(2, 1);
        int diff = 0;
        for (int i = 0; i < boardState.length; i++) {
            for (int j = 0; j < boardState.length; j++) {
                if (!boardState[i][j].equals(" ") && !(i == 2 && j == 1)) {
                    assertEquals("O", boardState[i][j]);
                    diff++;
                }
            }
        }
/*
        for (int i = 0; i < boardState.length; i++) {
            for (int j = 0; j < boardState.length; j++) {
                System.out.print(boardState[i][j] + "/");
            }
            System.out.println();
        }
*/
        assertEquals(1, diff);
        assertEquals("X", boardState[2][1]);
    }

    @Test
    public void testMakeTurnMediumAIPreventLose() {
        Board board = new AIBoard(3, AILevel.MEDIUM);
        String[][] boardState = board.move(0, 0);
        if(boardState[0][1].equals(" ") && boardState[0][2].equals(" ")) {
            boardState = board.move(0, 1);
            assertEquals("O", boardState[0][2]);
        } else {
            boardState = board.move(1, 0);
            assertEquals("O", boardState[2][0]);
        }
    }

    @Test
    public void testMakeTurnMediumAIWin() {
        Board board = new AIBoard(3, AILevel.MEDIUM);
        board.move(0, 0);
        board.move(0, 1);
        String[][] boardState = board.move(1, 1);
        if(boardState[0][1].equals(" ")) {
            boardState = board.move(0, 1);

        } else {
            boardState = board.move(1, 0);

        }
    }
}