package com.ragstorooks.chess.moves;

import com.ragstorooks.chess.pieces.Piece;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.AbstractMap.SimpleEntry;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class BasicMoveTest {
    private static final String SOURCE = "a4";
    private static final String DESTINATION = "a8";

    private Move move = new BasicMove(null, null, DESTINATION, false, null);

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
}