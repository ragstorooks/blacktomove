package com.ragstorooks.chess.moves;

import com.ragstorooks.chess.blocks.Colour;
import com.ragstorooks.chess.pieces.King;
import com.ragstorooks.chess.pieces.Rook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class QueensideCastleTest {
    private Move move = new QueensideCastle(Colour.Black);

    @Mock
    private PieceMover pieceMover;

    @Test
    public void shouldMoveKingFromE8ToG8() {
        // act
        move.makeMove(null, pieceMover);

        // assert
        verify(pieceMover).move("e8", null);
        verify(pieceMover).move(eq("c8"), isA(King.class));
    }

    @Test
    public void shouldMoveRookFromA8ToD8() {
        // act
        move.makeMove(null, pieceMover);

        // assert
        verify(pieceMover).move("a8", null);
        verify(pieceMover).move(eq("d8"), isA(Rook.class));
    }
}