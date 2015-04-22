package com.ragstorooks.chess.moves;

import com.ragstorooks.chess.pieces.Bishop;
import com.ragstorooks.chess.pieces.Knight;
import com.ragstorooks.chess.pieces.Piece;
import com.ragstorooks.chess.pieces.PieceType;
import com.ragstorooks.chess.pieces.Queen;
import com.ragstorooks.chess.pieces.Rook;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.AbstractMap.SimpleEntry;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PromotionTest {
    private static final String SOURCE = "a7";
    private static final String DESTINATION = "a8";

    @Mock
    private PieceMover pieceMover;
    @Mock
    private Piece piece;

    @Test
    public void shouldRemovePawnFromSource() {
        // setup
        Move move = new Promotion(null, PieceType.KNIGHT, DESTINATION, false, null);

        // act
        move.makeMove(new SimpleEntry<String, Piece>(SOURCE, piece), pieceMover);

        // assert
        verify(pieceMover).move(SOURCE, null);
    }

    @Test
    public void shouldPromoteToKnightAtDestination() {
        // setup
        Move move = new Promotion(null, PieceType.KNIGHT, DESTINATION, false, null);

        // act
        move.makeMove(new SimpleEntry<String, Piece>(SOURCE, piece), pieceMover);

        // assert
        verify(pieceMover).move(eq(DESTINATION), isA(Knight.class));
    }

    @Test
    public void shouldPromoteToBishopAtDestination() {
        // setup
        Move move = new Promotion(null, PieceType.BISHOP, DESTINATION, false, null);

        // act
        move.makeMove(new SimpleEntry<String, Piece>(SOURCE, piece), pieceMover);

        // assert
        verify(pieceMover).move(eq(DESTINATION), isA(Bishop.class));
    }

    @Test
    public void shouldPromoteToRookAtDestination() {
        // setup
        Move move = new Promotion(null, PieceType.ROOK, DESTINATION, false, null);

        // act
        move.makeMove(new SimpleEntry<String, Piece>(SOURCE, piece), pieceMover);

        // assert
        verify(pieceMover).move(eq(DESTINATION), isA(Rook.class));
    }

    @Test
    public void shouldPromoteToQueenAtDestination() {
        // setup
        Move move = new Promotion(null, PieceType.QUEEN, DESTINATION, false, null);

        // act
        move.makeMove(new SimpleEntry<String, Piece>(SOURCE, piece), pieceMover);

        // assert
        verify(pieceMover).move(eq(DESTINATION), isA(Queen.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotPromoteToKingAtDestination() {
        // setup
        Move move = new Promotion(null, PieceType.KING, DESTINATION, false, null);

        // act
        move.makeMove(new SimpleEntry<String, Piece>(SOURCE, piece), pieceMover);
    }
}