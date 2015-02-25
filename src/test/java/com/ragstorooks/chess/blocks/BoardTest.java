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
        String expected = "rnbqkbnr" + NEWLINE + "pppppppp" + NEWLINE + EMPTY_ROW + EMPTY_ROW + EMPTY_ROW + EMPTY_ROW +
                "PPPPPPPP" + NEWLINE + "RNBQKBNR" + NEWLINE;

        // act
        Board board = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");

        // assert
        assertThat(board.toString(), equalTo(expected));
    }

    @Test
    public void testBoardAfterConstructingWithFENFor1e4() {
        String expected = "rnbqkbnr" + NEWLINE + "pppppppp" + NEWLINE + EMPTY_ROW + EMPTY_ROW + "    P   " + NEWLINE
                + EMPTY_ROW + "PPPP PPP" + NEWLINE + "RNBQKBNR" + NEWLINE;

        // act
        Board board = new Board("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR");

        // assert
        assertThat(board.toString(), equalTo(expected));
    }

    @Test
    public void testBoardAfterConstructingWithFENFor1e4c5() {
        String expected = "rnbqkbnr" + NEWLINE + "pp ppppp" + NEWLINE + EMPTY_ROW + "  p     " + NEWLINE + "    P   "
                + NEWLINE + EMPTY_ROW + "PPPP PPP" + NEWLINE + "RNBQKBNR" + NEWLINE;

        // act
        Board board = new Board("rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR");

        // assert
        assertThat(board.toString(), equalTo(expected));
    }

    @Test
    public void testBoardAfterConstructingWithFENFor1e4c52Nf3() {
        String expected = "rnbqkbnr" + NEWLINE + "pp ppppp" + NEWLINE + EMPTY_ROW + "  p     " + NEWLINE + "    P   "
                + NEWLINE + "     N  " + NEWLINE + "PPPP PPP" + NEWLINE + "RNBQKB R" + NEWLINE;

        // act
        Board board = new Board("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R");

        // assert
        assertThat(board.toString(), equalTo(expected));
    }

    @Test
    public void testThatBoardIsReflectedAfterMakingMove1e4() {
        String expected = "rnbqkbnr" + NEWLINE + "pppppppp" + NEWLINE + EMPTY_ROW + EMPTY_ROW + "    P   " + NEWLINE
                + EMPTY_ROW + "PPPP PPP" + NEWLINE + "RNBQKBNR" + NEWLINE;
        Board board = new Board("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR");

        // act
        board.makeMove(Colour.White, "e4");

        // assert
        assertThat(board.toString(), equalTo(expected));
    }

    @Test
    public void testThatBoardIsReflectedAfterMakingMove1e4c5() {
        String expected = "rnbqkbnr" + NEWLINE + "pp ppppp" + NEWLINE + EMPTY_ROW + "  p     " + NEWLINE + "    P   "
                + NEWLINE + EMPTY_ROW + "PPPP PPP" + NEWLINE + "RNBQKBNR" + NEWLINE;
        Board board = new Board("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR");

        // act
        board.makeMove(Colour.Black, "c5");

        // assert
        assertThat(board.toString(), equalTo(expected));
    }

    @Ignore("Not implemented Knight moves yet")
    @Test
    public void testThatBoardIsReflectedAfterMakingMove1e4c52Nf3() {
        String expected = "rnbqkbnr" + NEWLINE + "pp ppppp" + NEWLINE + EMPTY_ROW + "  p     " + NEWLINE + "    P   "
                + NEWLINE + "     N  " + NEWLINE + "PPPP PPP" + NEWLINE + "RNBQKB R" + NEWLINE;
        Board board = new Board("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR");

        // act
        board.makeMove(Colour.White, "Nf3");

        // assert
        assertThat(board.toString(), equalTo(expected));
    }
}