package ru.spbau.mit.kazakov.TicTacToe;

import org.junit.Test;

import static org.junit.Assert.*;

public class HotSeatBoardTest {
    @Test
    public void testMakeTurnSimple() {
        Board board = new HotSeatBoard(3);
        String[][] boardState = board.move(1, 0);
        assertArrayEquals(new String[][]{{" ", " ", " "}, {Player.X.toString(), " ", " "},
                {" ", " ", " "}}, boardState);
    }

    @Test
    public void testMakeTurnTwice() {
        Board board = new HotSeatBoard(3);
        board.move(1, 1);
        String[][] boardState = board.move(2, 0);
        assertArrayEquals(new String[][]{{" ", " ", " "}, {" ", Player.X.toString(), " "},
                {Player.O.toString(), " ", " "}}, boardState);
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
        String[][] boardState = board.move(2, 1);

        assertArrayEquals(new String[][]{{Player.X.toString(), " ", Player.O.toString()},
                {Player.O.toString(), Player.O.toString(), " "},
                {Player.X.toString(), Player.X.toString(), Player.X.toString()}}, boardState);
    }
}