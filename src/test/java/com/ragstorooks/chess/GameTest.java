package com.ragstorooks.chess;

import com.ragstorooks.chess.blocks.Board;
import com.ragstorooks.chess.blocks.Colour;
import com.ragstorooks.chess.blocks.PieceType;
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
        game.makeMove(new Move(Colour.White, PieceType.PAWN, "e4", false, null));

        // assert
        assertThat(game.getCurrentBoardPosition(), equalTo(expected));
    }

    @Test
    public void testThatBoardIsReflectedAfterMakingMove1e4c5() {
        String expected = "rnbqkbnr" + NEWLINE + "pp ppppp" + NEWLINE + EMPTY_ROW + "  p     " + NEWLINE + "    P   "
                + NEWLINE + EMPTY_ROW + "PPPP PPP" + NEWLINE + "RNBQKBNR" + NEWLINE;
        Game game = new Game(new Board("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR"));

        // act
        game.makeMove(new Move(Colour.Black, PieceType.PAWN, "c5", false, null));

        // assert
        assertThat(game.getCurrentBoardPosition(), equalTo(expected));
    }

    @Test
    public void testThatBoardIsReflectedAfterMakingMove1e4c52Nf3() {
        String expected = "rnbqkbnr" + NEWLINE + "pp ppppp" + NEWLINE + EMPTY_ROW + "  p     " + NEWLINE + "    P   "
                + NEWLINE + "     N  " + NEWLINE + "PPPP PPP" + NEWLINE + "RNBQKB R" + NEWLINE;
        Game game = new Game(new Board("rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR"));

        // act
        game.makeMove(new Move(Colour.White, PieceType.KNIGHT, "f3", false, null));

        // assert
        assertThat(game.getCurrentBoardPosition(), equalTo(expected));
    }
}
