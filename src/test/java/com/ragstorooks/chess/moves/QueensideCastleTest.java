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
    private Move move = new QueensideCastle(Colour.White);

    @Mock
    private PieceMover pieceMover;

    @Test
    public void shouldMoveKingFromE1ToG1() {
        // act
        move.makeMove(null, pieceMover);

        // assert
        verify(pieceMover).move("e1", null);
        verify(pieceMover).move(eq("c1"), isA(King.class));
    }

    @Test
    public void shouldMoveRookFromA1ToD1() {
        // act
        move.makeMove(null, pieceMover);

        // assert
        verify(pieceMover).move("a1", null);
        verify(pieceMover).move(eq("d1"), isA(Rook.class));
    }
}