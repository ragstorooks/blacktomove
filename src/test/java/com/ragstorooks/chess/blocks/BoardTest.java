package com.ragstorooks.chess.blocks;


import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class BoardTest {
    private static final String NEWLINE = System.getProperty("line.separator");
    private static final String EMPTY_ROW = "        " + NEWLINE;

    @Test
    public void testInitialBoardGetsCreatedWithFen() {
        String expected = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";

        // act
        Board board = new Board(expected);

        // assert
        assertThat(board.toString(), equalTo(expected));
    }

    @Test
    public void testBoardAfterConstructingWithFENFor1e4() {
        String expected = "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR";

        // act
        Board board = new Board(expected);

        // assert
        assertThat(board.toString(), equalTo(expected));
    }

    @Test
    public void testBoardAfterConstructingWithFENFor1e4c5() {
        String expected = "rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR";

        // act
        Board board = new Board(expected);

        // assert
        assertThat(board.toString(), equalTo(expected));
    }

    @Test
    public void testBoardAfterConstructingWithFENFor1e4c52Nf3() {
        String expected = "rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R";

        // act
        Board board = new Board(expected);

        // assert
        assertThat(board.toString(), equalTo(expected));
    }
}