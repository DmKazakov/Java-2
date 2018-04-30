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
        CellContent[][] boardState = board.move(2, 1);
        checkEnemyFirstTurn(boardState);
        assertEquals(CellContent.X, boardState[2][1]);
    }

    @Test
    public void testMakeTurnMediumAISimple() {
        Board board = new AIBoard(3, AILevel.EASY);
        CellContent[][] boardState = board.move(2, 1);
        checkEnemyFirstTurn(boardState);
        assertEquals(CellContent.X, boardState[2][1]);
    }

    @Test
    public void testMakeTurnMediumAIPreventHorizontalLose() throws IllegalAccessException {
        CellContent[][] startingState = new CellContent[][]{{CellContent.X, CellContent.EMPTY, CellContent.EMPTY},
                {CellContent.O, CellContent.EMPTY, CellContent.EMPTY},
                {CellContent.EMPTY, CellContent.EMPTY, CellContent.EMPTY}};
        CellContent[][] expectedState = new CellContent[][]{{CellContent.X, CellContent.X, CellContent.O},
                {CellContent.O, CellContent.EMPTY, CellContent.EMPTY},
                {CellContent.EMPTY, CellContent.EMPTY, CellContent.EMPTY}};
        checkStateAfterPlayerTurn(startingState, expectedState, 0, 1);
    }

    @Test
    public void testMakeTurnMediumAIPreventVerticalLose() throws IllegalAccessException {
        CellContent[][] startingState = new CellContent[][]{{CellContent.X, CellContent.EMPTY, CellContent.O},
                {CellContent.EMPTY, CellContent.EMPTY, CellContent.EMPTY},
                {CellContent.EMPTY, CellContent.EMPTY, CellContent.EMPTY}};
        CellContent[][] expectedState = new CellContent[][]{{CellContent.X, CellContent.EMPTY, CellContent.O},
                {CellContent.X, CellContent.EMPTY, CellContent.EMPTY},
                {CellContent.O, CellContent.EMPTY, CellContent.EMPTY}};
        checkStateAfterPlayerTurn(startingState, expectedState, 1, 0);
    }

    @Test
    public void testMakeTurnMediumAIPreventFirstDiagonalLose() throws IllegalAccessException {
        CellContent[][] startingState = new CellContent[][]{{CellContent.X, CellContent.EMPTY, CellContent.O},
                {CellContent.EMPTY, CellContent.EMPTY, CellContent.EMPTY},
                {CellContent.EMPTY, CellContent.EMPTY, CellContent.EMPTY}};
        CellContent[][] expectedState = new CellContent[][]{{CellContent.X, CellContent.EMPTY, CellContent.O},
                {CellContent.EMPTY, CellContent.X, CellContent.EMPTY},
                {CellContent.EMPTY, CellContent.EMPTY, CellContent.O}};
        checkStateAfterPlayerTurn(startingState, expectedState, 1, 1);
    }

    @Test
    public void testMakeTurnMediumAIPreventSecondDiagonalLose() throws IllegalAccessException {
        CellContent[][] startingState = new CellContent[][]{{CellContent.O, CellContent.EMPTY, CellContent.X},
                {CellContent.EMPTY, CellContent.EMPTY, CellContent.EMPTY},
                {CellContent.EMPTY, CellContent.EMPTY, CellContent.EMPTY}};
        CellContent[][] expectedState = new CellContent[][]{{CellContent.O, CellContent.EMPTY, CellContent.X},
                {CellContent.EMPTY, CellContent.X, CellContent.EMPTY},
                {CellContent.O, CellContent.EMPTY, CellContent.EMPTY}};
        checkStateAfterPlayerTurn(startingState, expectedState, 1, 1);
    }

    @Test
    public void testMakeTurnMediumAIHorizontalWin() throws IllegalAccessException {
        CellContent[][] startingState = new CellContent[][]{{CellContent.O, CellContent.EMPTY, CellContent.O},
                {CellContent.X, CellContent.EMPTY, CellContent.EMPTY},
                {CellContent.X, CellContent.EMPTY, CellContent.EMPTY}};
        CellContent[][] expectedState = new CellContent[][]{{CellContent.O, CellContent.O, CellContent.O},
                {CellContent.X, CellContent.X, CellContent.EMPTY},
                {CellContent.X, CellContent.EMPTY, CellContent.EMPTY}};
        checkStateAfterPlayerTurn(startingState, expectedState, 1, 1);
    }

    @Test
    public void testMakeTurnMediumAIVerticalWin() throws IllegalAccessException {
        CellContent[][] startingState = new CellContent[][]{{CellContent.O, CellContent.X, CellContent.X},
                {CellContent.O, CellContent.EMPTY, CellContent.EMPTY},
                {CellContent.EMPTY, CellContent.EMPTY, CellContent.EMPTY}};
        CellContent[][] expectedState = new CellContent[][]{{CellContent.O, CellContent.X, CellContent.X},
                {CellContent.O, CellContent.X, CellContent.EMPTY},
                {CellContent.O, CellContent.EMPTY, CellContent.EMPTY}};
        checkStateAfterPlayerTurn(startingState, expectedState, 1, 1);
    }

    @Test
    public void testMakeTurnMediumAIFirstDiagonalWin() throws IllegalAccessException {
        CellContent[][] startingState = new CellContent[][]{{CellContent.O, CellContent.X, CellContent.X},
                {CellContent.EMPTY, CellContent.EMPTY, CellContent.EMPTY},
                {CellContent.EMPTY, CellContent.EMPTY, CellContent.O}};
        CellContent[][] expectedState = new CellContent[][]{{CellContent.O, CellContent.X, CellContent.X},
                {CellContent.EMPTY, CellContent.O, CellContent.EMPTY},
                {CellContent.X, CellContent.EMPTY, CellContent.O}};
        checkStateAfterPlayerTurn(startingState, expectedState, 2, 0);
    }

    @Test
    public void testMakeTurnMediumAISecondDiagonalWin() throws IllegalAccessException {
        CellContent[][] startingState = new CellContent[][]{{CellContent.X, CellContent.X, CellContent.O},
                {CellContent.EMPTY, CellContent.EMPTY, CellContent.EMPTY},
                {CellContent.O, CellContent.EMPTY, CellContent.EMPTY}};
        CellContent[][] expectedState = new CellContent[][]{{CellContent.X, CellContent.X, CellContent.O},
                {CellContent.X, CellContent.O, CellContent.EMPTY},
                {CellContent.O, CellContent.EMPTY, CellContent.EMPTY}};
        checkStateAfterPlayerTurn(startingState, expectedState, 1, 0);
    }

    @Test
    public void testMakeTurnPlayerHorizontalWin() throws IllegalAccessException {
        CellContent[][] startingState = new CellContent[][]{{CellContent.X, CellContent.EMPTY, CellContent.X},
                {CellContent.O, CellContent.EMPTY, CellContent.O},
                {CellContent.EMPTY, CellContent.EMPTY, CellContent.EMPTY}};
        CellContent[][] expectedState = new CellContent[][]{{CellContent.X, CellContent.X, CellContent.X},
                {CellContent.O, CellContent.EMPTY, CellContent.O},
                {CellContent.EMPTY, CellContent.EMPTY, CellContent.EMPTY}};
        checkStateAfterPlayerTurn(startingState, expectedState, 0, 1);
    }

    @Test
    public void testMakeTurnPlayerVerticalWin() throws IllegalAccessException {
        CellContent[][] startingState = new CellContent[][]{{CellContent.X, CellContent.EMPTY, CellContent.O},
                {CellContent.X, CellContent.EMPTY, CellContent.O},
                {CellContent.EMPTY, CellContent.EMPTY, CellContent.EMPTY}};
        CellContent[][] expectedState = new CellContent[][]{{CellContent.X, CellContent.EMPTY, CellContent.O},
                {CellContent.X, CellContent.EMPTY, CellContent.O},
                {CellContent.X, CellContent.EMPTY, CellContent.EMPTY}};
        checkStateAfterPlayerTurn(startingState, expectedState, 2, 0);
    }

    @Test
    public void testMakeTurnPlayerFirstDiagonalWin() throws IllegalAccessException {
        CellContent[][] startingState = new CellContent[][]{{CellContent.X, CellContent.EMPTY, CellContent.O},
                {CellContent.EMPTY, CellContent.X, CellContent.O},
                {CellContent.EMPTY, CellContent.EMPTY, CellContent.EMPTY}};
        CellContent[][] expectedState = new CellContent[][]{{CellContent.X, CellContent.EMPTY, CellContent.O},
                {CellContent.EMPTY, CellContent.X, CellContent.O},
                {CellContent.EMPTY, CellContent.EMPTY, CellContent.X}};
        checkStateAfterPlayerTurn(startingState, expectedState, 2, 2);
    }

    @Test
    public void testMakeTurnPlayerSecondDiagonalWin() throws IllegalAccessException {
        CellContent[][] startingState = new CellContent[][]{{CellContent.O, CellContent.EMPTY, CellContent.X},
                {CellContent.EMPTY, CellContent.X, CellContent.O},
                {CellContent.EMPTY, CellContent.EMPTY, CellContent.EMPTY}};
        CellContent[][] expectedState = new CellContent[][]{{CellContent.O, CellContent.EMPTY, CellContent.X},
                {CellContent.EMPTY, CellContent.X, CellContent.O},
                {CellContent.X, CellContent.EMPTY, CellContent.EMPTY}};
        checkStateAfterPlayerTurn(startingState, expectedState, 2, 0);
    }

    private void checkEnemyFirstTurn(CellContent[][] boardState) {
        int diff = 0;
        for (int i = 0; i < boardState.length; i++) {
            for (int j = 0; j < boardState.length; j++) {
                if (!boardState[i][j].equals(CellContent.EMPTY) && !(i == 2 && j == 1)) {
                    assertEquals(CellContent.O, boardState[i][j]);
                    diff++;
                }
            }
        }

        assertEquals(1, diff);
    }
    
    private void checkStateAfterPlayerTurn(CellContent[][] staringState, CellContent[][] expectedState, int row, int col) throws IllegalAccessException {
        Board board = new AIBoard(3, AILevel.MEDIUM);
        boardField.set(board, staringState);
        CellContent[][] actualState = board.move(row, col);
        assertArrayEquals(expectedState, actualState);
    }
}