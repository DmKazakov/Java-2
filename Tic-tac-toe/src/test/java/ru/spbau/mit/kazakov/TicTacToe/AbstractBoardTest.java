package ru.spbau.mit.kazakov.TicTacToe;

import org.junit.Test;

import static org.junit.Assert.*;

public class AbstractBoardTest {
    @Test
    public void testGetStateInitial() {
        AbstractBoard board = new HotSeatBoard(3);
        assertEquals(State.RUNNING, board.getState());
    }

    @Test
    public void testGetStateRunning() {
        AbstractBoard board = new HotSeatBoard(3);
        board.move(1, 1);
        board.move(2, 0);
        assertEquals(State.RUNNING, board.getState());
    }

    @Test
    public void testGetStateHorizontalWin() {
        AbstractBoard board = new HotSeatBoard(3);
        board.move(0, 0);
        board.move(1, 1);
        board.move(2, 2);
        board.move(0, 2);
        board.move(2, 0);
        board.move(1, 0);
        board.move(2, 1);
        assertEquals(State.X_WINS, board.getState());
    }

    @Test
    public void testGetStateVerticalWin() {
        AbstractBoard board = new HotSeatBoard(3);
        board.move(0, 1);
        board.move(0, 0);
        board.move(1, 2);
        board.move(2, 2);
        board.move(0, 2);
        board.move(2, 0);
        board.move(2, 1);
        board.move(1, 0);
        assertEquals(State.O_WINS, board.getState());
    }

    @Test
    public void testGetStateFirstDiagonalWin() {
        AbstractBoard board = new HotSeatBoard(3);
        board.move(0, 0);
        board.move(0, 1);
        board.move(1, 1);
        board.move(0, 2);
        board.move(2, 2);
        assertEquals(State.X_WINS, board.getState());
    }

    @Test
    public void testGetStateSecondDiagonalWin() {
        AbstractBoard board = new HotSeatBoard(3);
        board.move(0, 0);
        board.move(0, 2);
        board.move(0, 1);
        board.move(1, 1);
        board.move(2, 2);
        board.move(2, 0);
        assertEquals(State.O_WINS, board.getState());
    }

    @Test
    public void testGetStateDraw() {
        AbstractBoard board = new HotSeatBoard(3);
        board.move(0, 0);
        board.move(0, 1);
        board.move(0, 2);
        board.move(1, 1);
        board.move(1, 0);
        board.move(2, 0);
        board.move(1, 2);
        board.move(2, 2);
        board.move(2, 1);
        assertEquals(State.DRAW, board.getState());
    }

    @Test
    public void testReset() {
        AbstractBoard board = new HotSeatBoard(3);
        board.move(0, 0);
        board.move(0, 2);
        board.move(0, 1);
        board.move(1, 1);
        board.move(2, 2);
        board.move(2, 0);
        board.reset();
        assertArrayEquals(new String[][]{{Player.X.toString(), " ", " "},
                {" ", " ", " "}, {" ", " ", " "}}, board.move(0, 0));
        assertEquals(State.RUNNING, board.getState());
    }
}