package ru.spbau.mit.kazakov.TicTacToe;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class AIBoardTest {
    private Field boardField;

    @Before
    public void initializeFiled() throws NoSuchFieldException {
        boardField = AbstractBoard.class.getDeclaredField("board");
        boardField.setAccessible(true);
    }

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
                    assertEquals(Player.O.toString(), boardState[i][j]);
                    diff++;
                }
            }
        }
        assertEquals(1, diff);
        assertEquals(Player.X.toString(), boardState[2][1]);
    }

    @Test
    public void testMakeTurnMediumAISimple() {
        Board board = new AIBoard(3, AILevel.EASY);
        String[][] boardState = board.move(2, 1);
        int diff = 0;
        for (int i = 0; i < boardState.length; i++) {
            for (int j = 0; j < boardState.length; j++) {
                if (!boardState[i][j].equals(" ") && !(i == 2 && j == 1)) {
                    assertEquals(Player.O.toString(), boardState[i][j]);
                    diff++;
                }
            }
        }

        assertEquals(1, diff);
        assertEquals(Player.X.toString(), boardState[2][1]);
    }

    @Test
    public void testMakeTurnMediumAIPreventHorizontalLose() throws IllegalAccessException {
        Board board = new AIBoard(3, AILevel.MEDIUM);
        String[][] boardState = new String[][]{{Player.X.toString(), " ", " "},
                {Player.O.toString(), " ", " "},
                {" ", " ", " "}};
        boardField.set(board, boardState);
        boardState = board.move(0, 1);
        assertArrayEquals(new String[][]{{Player.X.toString(), Player.X.toString(), Player.O.toString()},
                {Player.O.toString(), " ", " "}, {" ", " ", " "}}, boardState);
    }

    @Test
    public void testMakeTurnMediumAIPreventVerticalLose() throws IllegalAccessException {
        Board board = new AIBoard(3, AILevel.MEDIUM);
        String[][] boardState = new String[][]{{Player.X.toString(), " ", Player.O.toString()},
                {" ", " ", " "},
                {" ", " ", " "}};
        boardField.set(board, boardState);
        boardState = board.move(1, 0);
        assertArrayEquals(new String[][]{{Player.X.toString(), " ", Player.O.toString()},
                {Player.X.toString(), " ", " "}, {Player.O.toString(), " ", " "}}, boardState);
    }

    @Test
    public void testMakeTurnMediumAIPreventFirstDiagonalLose() throws IllegalAccessException {
        Board board = new AIBoard(3, AILevel.MEDIUM);
        String[][] boardState = new String[][]{{Player.X.toString(), " ", Player.O.toString()},
                {" ", " ", " "},
                {" ", " ", " "}};
        boardField.set(board, boardState);
        boardState = board.move(1, 1);
        assertArrayEquals(new String[][]{{Player.X.toString(), " ", Player.O.toString()},
                {" ", Player.X.toString(), " "}, {" ", " ", Player.O.toString()}}, boardState);
    }

    @Test
    public void testMakeTurnMediumAIPreventSecondDiagonalLose() throws IllegalAccessException {
        Board board = new AIBoard(3, AILevel.MEDIUM);
        String[][] boardState = new String[][]{{Player.O.toString(), " ", Player.X.toString()},
                {" ", " ", " "},
                {" ", " ", " "}};
        boardField.set(board, boardState);
        boardState = board.move(1, 1);
        assertArrayEquals(new String[][]{{Player.O.toString(), " ", Player.X.toString()},
                {" ", Player.X.toString(), " "}, {Player.O.toString(), " ", " "}}, boardState);
    }

    @Test
    public void testMakeTurnMediumAIHorizontalWin() throws IllegalAccessException {
        Board board = new AIBoard(3, AILevel.MEDIUM);
        String[][] boardState = new String[][]{{Player.O.toString(), " ", Player.O.toString()},
                {Player.X.toString(), " ", " "},
                {Player.X.toString(), " ", " "}};
        boardField.set(board, boardState);
        boardState = board.move(1, 1);
        assertArrayEquals(new String[][]{{Player.O.toString(), Player.O.toString(), Player.O.toString()},
                {Player.X.toString(), Player.X.toString(), " "}, {Player.X.toString(), " ", " "}}, boardState);
    }

    @Test
    public void testMakeTurnMediumAIVerticalWin() throws IllegalAccessException {
        Board board = new AIBoard(3, AILevel.MEDIUM);
        String[][] boardState = new String[][]{{Player.O.toString(), Player.X.toString(), Player.X.toString()},
                {Player.O.toString(), " ", " "},
                {" ", " ", " "}};
        boardField.set(board, boardState);
        boardState = board.move(1, 1);
        assertArrayEquals(new String[][]{{Player.O.toString(), Player.X.toString(), Player.X.toString()},
                {Player.O.toString(), Player.X.toString(), " "}, {Player.O.toString(), " ", " "}}, boardState);
    }

    @Test
    public void testMakeTurnMediumAIFirstDiagonalWin() throws IllegalAccessException {
        Board board = new AIBoard(3, AILevel.MEDIUM);
        String[][] boardState = new String[][]{{Player.O.toString(), Player.X.toString(), Player.X.toString()},
                {" ", " ", " "},
                {" ", " ", Player.O.toString()}};
        boardField.set(board, boardState);
        boardState = board.move(2, 0);
        assertArrayEquals(new String[][]{{Player.O.toString(), Player.X.toString(), Player.X.toString()},
                {" ", Player.O.toString(), " "}, {Player.X.toString(), " ", Player.O.toString()}}, boardState);
    }

    @Test
    public void testMakeTurnMediumAISecondDiagonalWin() throws IllegalAccessException {
        Board board = new AIBoard(3, AILevel.MEDIUM);
        String[][] boardState = new String[][]{{Player.X.toString(), Player.X.toString(), Player.O.toString()},
                {" ", " ", " "},
                {Player.O.toString(), " ", " "}};
        boardField.set(board, boardState);
        boardState = board.move(1, 0);
        assertArrayEquals(new String[][]{{Player.X.toString(), Player.X.toString(), Player.O.toString()},
                {Player.X.toString(), Player.O.toString(), " "}, {Player.O.toString(), " ", " "}}, boardState);
    }

    @Test
    public void testMakeTurnPlayerHorizontalWin() throws IllegalAccessException {
        Board board = new AIBoard(3, AILevel.MEDIUM);
        String[][] boardState = new String[][]{{Player.X.toString(), " ", Player.X.toString()},
                {Player.O.toString(), " ", Player.O.toString()},
                {" ", " ", " "}};
        boardField.set(board, boardState);
        boardState = board.move(0, 1);
        assertArrayEquals(new String[][]{{Player.X.toString(), Player.X.toString(), Player.X.toString()},
                {Player.O.toString(), " ", Player.O.toString()}, {" ", " ", " "}}, boardState);
    }

    @Test
    public void testMakeTurnPlayerVerticalWin() throws IllegalAccessException {
        Board board = new AIBoard(3, AILevel.MEDIUM);
        String[][] boardState = new String[][]{{Player.X.toString(), " ", Player.O.toString()},
                {Player.X.toString(), " ", Player.O.toString()},
                {" ", " ", " "}};
        boardField.set(board, boardState);
        boardState = board.move(2, 0);
        assertArrayEquals(new String[][]{{Player.X.toString(), " ", Player.O.toString()},
                {Player.X.toString(), " ", Player.O.toString()}, {Player.X.toString(), " ", " "}}, boardState);
    }

    @Test
    public void testMakeTurnPlayerFirstDiagonalWin() throws IllegalAccessException {
        Board board = new AIBoard(3, AILevel.MEDIUM);
        String[][] boardState = new String[][]{{Player.X.toString(), " ", Player.O.toString()},
                {" ", Player.X.toString(), Player.O.toString()},
                {" ", " ", " "}};
        boardField.set(board, boardState);
        boardState = board.move(2, 2);
        assertArrayEquals(new String[][]{{Player.X.toString(), " ", Player.O.toString()},
                {" ", Player.X.toString(), Player.O.toString()}, {" ", " ", Player.X.toString()}}, boardState);
    }

    @Test
    public void testMakeTurnPlayerSecondDiagonalWin() throws IllegalAccessException {
        Board board = new AIBoard(3, AILevel.MEDIUM);
        String[][] boardState = new String[][]{{Player.O.toString(), " ", Player.X.toString()},
                {" ", Player.X.toString(), Player.O.toString()},
                {" ", " ", " "}};
        boardField.set(board, boardState);
        boardState = board.move(2, 0);
        assertArrayEquals(new String[][]{{Player.O.toString(), " ", Player.X.toString()},
                {" ", Player.X.toString(), Player.O.toString()}, {Player.X.toString(), " ", " "}}, boardState);
    }
}