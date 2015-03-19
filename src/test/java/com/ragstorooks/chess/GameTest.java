package com.ragstorooks.chess;

import com.ragstorooks.chess.blocks.Board;
import com.ragstorooks.chess.blocks.Colour;
import com.ragstorooks.chess.moves.BasicMove;
import com.ragstorooks.chess.pieces.PieceType;
import org.junit.Ignore;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class GameTest {
    private static final String NEWLINE = System.getProperty("line.separator");
    private static final String EMPTY_ROW = "        " + NEWLINE;

    @Test
    public void testThatBoardIsReflectedAfterMakingMove1e4() {
        String expected = "rnbqkbnr" + NEWLINE + "pppppppp" + NEWLINE + EMPTY_ROW + EMPTY_ROW + "    P   " + NEWLINE
                + EMPTY_ROW + "PPPP PPP" + NEWLINE + "RNBQKBNR" + NEWLINE;
        Game game = new Game(new Board());

        // act
        game.makeMove(new BasicMove(Colour.White, PieceType.PAWN, "e4", false, null));

        // assert
        assertThat(game.getCurrentBoardPosition(), equalTo(expected));
    }

    @Test
    public void testThatBoardIsReflectedAfterMakingMove1e4c5() {
        String expected = "rnbqkbnr" + NEWLINE + "pp ppppp" + NEWLINE + EMPTY_ROW + "  p     " + NEWLINE + "    P   "
                + NEWLINE + EMPTY_ROW + "PPPP PPP" + NEWLINE + "RNBQKBNR" + NEWLINE;
        Game game = new Game(new Board("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR"));

        // act
        game.makeMove(new BasicMove(Colour.Black, PieceType.PAWN, "c5", false, null));

        // assert
        assertThat(game.getCurrentBoardPosition(), equalTo(expected));
    }

    @Test
    public void testThatBoardIsReflectedAfterMakingMove1e4c52Nf3() {
        String expected = "rnbqkbnr" + NEWLINE + "pp ppppp" + NEWLINE + EMPTY_ROW + "  p     " + NEWLINE + "    P   "
                + NEWLINE + "     N  " + NEWLINE + "PPPP PPP" + NEWLINE + "RNBQKB R" + NEWLINE;
        Game game = new Game(new Board("rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR"));

        // act
        game.makeMove(new BasicMove(Colour.White, PieceType.KNIGHT, "f3", false, null));

        // assert
        assertThat(game.getCurrentBoardPosition(), equalTo(expected));
    }

    @Test
    public void testThatBoardIsReflectedAfterMakingMove1e4c52Nf3Nc6() {
        String expected = "r bqkbnr" + NEWLINE + "pp ppppp" + NEWLINE + "  n     " + NEWLINE + "  p     " + NEWLINE +
                "    P   "
                + NEWLINE + "     N  " + NEWLINE + "PPPP PPP" + NEWLINE + "RNBQKB R" + NEWLINE;
        Game game = new Game(new Board("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R"));

        // act
        game.makeMove(new BasicMove(Colour.Black, PieceType.KNIGHT, "c6", false, null));

        // assert
        assertThat(game.getCurrentBoardPosition(), equalTo(expected));
    }

    @Test
    public void testThatBoardIsReflectedAfterMakingMove1e4c52Nf3Nc63d4() {
        String expected = "r bqkbnr" + NEWLINE + "pp ppppp" + NEWLINE + "  n     " + NEWLINE + "  p     " + NEWLINE +
                "   PP   "
                + NEWLINE + "     N  " + NEWLINE + "PPP  PPP" + NEWLINE + "RNBQKB R" + NEWLINE;
        Game game = new Game(new Board("r1bqkbnr/pp1ppppp/2n5/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R"));

        // act
        game.makeMove(new BasicMove(Colour.White, PieceType.PAWN, "d4", false, null));

        // assert
        assertThat(game.getCurrentBoardPosition(), equalTo(expected));
    }

    @Test
    public void testThatBoardIsReflectedAfterMakingMove1e4c52Nf3Nc63d4cxd4() {
        String expected = "r bqkbnr" + NEWLINE + "pp ppppp" + NEWLINE + "  n     " + NEWLINE + "        " + NEWLINE +
                "   pP   "
                + NEWLINE + "     N  " + NEWLINE + "PPP  PPP" + NEWLINE + "RNBQKB R" + NEWLINE;
        Game game = new Game(new Board("r1bqkbnr/pp1ppppp/2n5/2p5/3PP3/5N2/PPP2PPP/RNBQKB1R"));

        // act
        game.makeMove(new BasicMove(Colour.Black, PieceType.PAWN, "d4", true, "c"));

        // assert
        assertThat(game.getCurrentBoardPosition(), equalTo(expected));
    }

    @Test
    public void testThatBoardIsReflectedAfterMakingMove1e4c52Nf3Nc63d4cxd4Nxd4() {
        String expected = "r bqkbnr" + NEWLINE + "pp ppppp" + NEWLINE + "  n     " + NEWLINE + "        " + NEWLINE +
                "   NP   "
                + NEWLINE + "        " + NEWLINE + "PPP  PPP" + NEWLINE + "RNBQKB R" + NEWLINE;
        Game game = new Game(new Board("r1bqkbnr/pp1ppppp/2n5/8/3pP3/5N2/PPP2PPP/RNBQKB1R"));

        // act
        game.makeMove(new BasicMove(Colour.White, PieceType.KNIGHT, "d4", true, null));

        // assert
        assertThat(game.getCurrentBoardPosition(), equalTo(expected));
    }

    @Ignore("Bishop moves haven't been coded yet")
    @Test
    public void testThatBoardIsReflectedAfterMakingMove1e4e52Bc4() {
        String expected = "rnbqkbnr" + NEWLINE + "pppp ppp" + NEWLINE + "        " + NEWLINE + "   p    " + NEWLINE +
                "  B P   "
                + NEWLINE + "   P    " + NEWLINE + "PPPP PPP" + NEWLINE + "RNBQK NR" + NEWLINE;
        Game game = new Game(new Board("rnbqkbnr/pppp1ppp/8/4n3/2B1P3/8/PPPP1PPP/RNBQK1NR"));

        // act
        game.makeMove(new BasicMove(Colour.White, PieceType.BISHOP, "c4", false, null));

        // assert
        assertThat(game.getCurrentBoardPosition(), equalTo(expected));
    }
}
