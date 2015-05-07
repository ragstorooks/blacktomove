package com.ragstorooks.chess.moves;

import com.ragstorooks.chess.blocks.Colour;
import com.ragstorooks.chess.pieces.Piece;
import com.ragstorooks.chess.pieces.PieceType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.AbstractMap.SimpleEntry;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BasicMoveTest {
    private static final String SOURCE = "a4";
    private static final String DESTINATION = "a8";

    private Move move = new BasicMove(null, PieceType.ROOK, DESTINATION, false, null);

    @Mock
    private PieceMover pieceMover;
    @Mock
    private Piece piece;

    @Test
    public void shouldRemovePieceFromSource() {
        // act
        move.makeMove(new SimpleEntry<String, Piece>(SOURCE, piece), pieceMover);

        // assert
        verify(pieceMover).move(SOURCE, null);
    }

    @Test
    public void shouldMovePieceToDestination() {
        // act
        move.makeMove(new SimpleEntry<String, Piece>(SOURCE, piece), pieceMover);

        // assert
        verify(pieceMover).move(DESTINATION, piece);
    }

    @Test
    public void shouldReceiveNullEnPassantableEventNotificationForAPieceMove() {
        // setup
        EnPassantableEventListener enPassantableEventListener = mock(EnPassantableEventListener.class);
        move.registerEnPassantableEventListener(enPassantableEventListener);

        // act
        move.makeMove(new SimpleEntry<String, Piece>(SOURCE, piece), pieceMover);

        // assert
        verify(enPassantableEventListener).notify(null);
    }

    @Test
    public void shouldReceiveNullEnPassantableEventNotificationForAPawnSingleSquareMove() {
        // setup
        EnPassantableEventListener enPassantableEventListener = mock(EnPassantableEventListener.class);
        Move move = new BasicMove(null, PieceType.PAWN, "a3", false, null);
        move.registerEnPassantableEventListener(enPassantableEventListener);

        // act
        move.makeMove(new SimpleEntry<String, Piece>("a2", piece), pieceMover);

        // assert
        verify(enPassantableEventListener).notify(null);
    }

    @Test
    public void shouldReceiveNullEnPassantableEventNotificationForAPawnCaptureMove() {
        // setup
        EnPassantableEventListener enPassantableEventListener = mock(EnPassantableEventListener.class);
        Move move = new BasicMove(null, PieceType.PAWN, "b3", true, null);
        move.registerEnPassantableEventListener(enPassantableEventListener);

        // act
        move.makeMove(new SimpleEntry<String, Piece>("a2", piece), pieceMover);

        // assert
        verify(enPassantableEventListener).notify(null);
    }

    @Test
    public void shouldReceiveEnPassantableEventNotificationForAPawnDoubleSquareMove() {
        // setup
        EnPassantableEvent expectedEvent = new EnPassantableEvent("a3");
        EnPassantableEventListener enPassantableEventListener = mock(EnPassantableEventListener.class);
        Move move = new BasicMove(Colour.White, PieceType.PAWN, "a4", false, null);
        move.registerEnPassantableEventListener(enPassantableEventListener);

        // act
        move.makeMove(new SimpleEntry<String, Piece>("a2", piece), pieceMover);

        // assert
        verify(enPassantableEventListener).notify(expectedEvent);
    }
}