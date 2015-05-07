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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class KingsideCastleTest {
    private Move move = new KingsideCastle(Colour.White);

    @Mock
    private PieceMover pieceMover;

    @Test
    public void shouldMoveKingFromE1ToG1() {
        // act
        move.makeMove(null, pieceMover);

        // assert
        verify(pieceMover).move("e1", null);
        verify(pieceMover).move(eq("g1"), isA(King.class));
    }

    @Test
    public void shouldMoveRookFromH1ToF1() {
        // act
        move.makeMove(null, pieceMover);

        // assert
        verify(pieceMover).move("h1", null);
        verify(pieceMover).move(eq("f1"), isA(Rook.class));
    }

    @Test
    public void shouldReceiveNullEnPassantableEventNotification() {
        // setup
        EnPassantableEventListener enPassantableEventListener = mock(EnPassantableEventListener.class);
        move.registerEnPassantableEventListener(enPassantableEventListener);

        // act
        move.makeMove(null, pieceMover);

        // assert
        verify(enPassantableEventListener).notify(null);
    }
}