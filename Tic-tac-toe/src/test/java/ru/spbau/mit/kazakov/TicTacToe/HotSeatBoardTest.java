package ru.spbau.mit.kazakov.TicTacToe;

import org.junit.Test;

import static org.junit.Assert.*;

public class HotSeatBoardTest {
    @Test
    public void testMakeTurnSimple() {
        Board board = new HotSeatBoard(3);
        CellContent[][] boardState = board.move(1, 0);
        assertArrayEquals(new CellContent[][]{{CellContent.EMPTY, CellContent.EMPTY, CellContent.EMPTY}, {CellContent.X, CellContent.EMPTY, CellContent.EMPTY},
                {CellContent.EMPTY, CellContent.EMPTY, CellContent.EMPTY}}, boardState);
    }

    @Test
    public void testMakeTurnTwice() {
        Board board = new HotSeatBoard(3);
        board.move(1, 1);
        CellContent[][] boardState = board.move(2, 0);
        assertArrayEquals(new CellContent[][]{{CellContent.EMPTY, CellContent.EMPTY, CellContent.EMPTY}, {CellContent.EMPTY, CellContent.X, CellContent.EMPTY},
                {CellContent.O, CellContent.EMPTY, CellContent.EMPTY}}, boardState);
    }

    @Test
    public void testMakeTurn() {
        Board board = new HotSeatBoard(3);
        board.move(0, 0);
        board.move(1, 1);
        board.move(2, 2);
        board.move(0, 2);
        board.move(2, 0);
        board.move(1, 0);
        CellContent[][] boardState = board.move(2, 1);

        assertArrayEquals(new CellContent[][]{{CellContent.X, CellContent.EMPTY, CellContent.O},
                {CellContent.O, CellContent.O, CellContent.EMPTY},
                {CellContent.X, CellContent.X, CellContent.X}}, boardState);
    }
}