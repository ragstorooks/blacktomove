package com.ragstorooks.chess.pieces;

import com.ragstorooks.chess.blocks.Colour;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class QueenTest {
    @Test
    public void shouldBeAbleToMoveAlongADiagonal() {
        // setup
        Queen queen = new Queen(Colour.White);

        // act & assert
        assertTrue(queen.canMoveTo("f1", "c4", false, square -> null));
    }

    @Test
    public void shouldNotBeAbleToMoveIfNotAValidDiagonal() {
        // setup
        Queen queen = new Queen(Colour.White);

        // act & assert
        assertFalse(queen.canMoveTo("f1", "b4", false, square -> null));
    }

    @Test
    public void shouldNotBeAbleToMoveInDiagonalIfOwnPieceInTheWay() {
        // setup
        Queen queen = new Queen(Colour.White);

        // act & assert
        assertFalse(queen.canMoveTo("f1", "c4", false, square -> "d3".equals(square)? new Pawn(Colour.White) : null));
    }

    @Test
    public void shouldNotBeAbleToMoveInDiagonalIfOwnPieceAtDestination() {
        // setup
        Queen queen = new Queen(Colour.White);

        // act & assert
        assertFalse(queen.canMoveTo("f1", "c4", false, square -> "c4".equals(square)? new Pawn(Colour.White) : null));
    }

    @Test
    public void shouldNotBeAbleToMoveInDiagonalIfOppositionPieceInTheWay() {
        // setup
        Queen queen = new Queen(Colour.White);

        // act & assert
        assertFalse(queen.canMoveTo("f1", "c4", false, square -> "d3".equals(square)? new Pawn(Colour.Black) : null));
    }

    @Test
    public void shouldBeAbleToMoveInDiagonalIfCapturingOppositionPiece() {
        // setup
        Queen queen = new Queen(Colour.White);

        // act & assert
        assertTrue(queen.canMoveTo("f1", "c4", true, square -> "c4".equals(square)? new Pawn(Colour.Black) : null));
    }

    @Test
    public void shouldBeAbleToMoveAlongAFile() {
        // setup
        Queen queen = new Queen(Colour.White);

        // act & assert
        assertTrue(queen.canMoveTo("f1", "f4", false, square -> null));
    }

    @Test
    public void shouldBeAbleToMoveAlongARank() {
        // setup
        Queen queen = new Queen(Colour.White);

        // act & assert
        assertTrue(queen.canMoveTo("f1", "b1", false, square -> null));
    }

    @Test
    public void shouldNotBeAbleToMoveIfNotAValidRankOrAFile() {
        // setup
        Queen queen = new Queen(Colour.White);

        // act & assert
        assertFalse(queen.canMoveTo("f1", "c2", false, square -> null));
    }

    @Test
    public void shouldNotBeAbleToMoveOnFileIfOwnPieceInTheWay() {
        // setup
        Queen queen = new Queen(Colour.White);

        // act & assert
        assertFalse(queen.canMoveTo("f1", "f4", false, square -> "f3".equals(square)? new Pawn(Colour.White) : null));
    }

    @Test
    public void shouldNotBeAbleToMoveOnFileIfOwnPieceAtDestination() {
        // setup
        Queen queen = new Queen(Colour.White);

        // act & assert
        assertFalse(queen.canMoveTo("f1", "f4", false, square -> "f4".equals(square)? new Pawn(Colour.White) : null));
    }

    @Test
    public void shouldNotBeAbleToMoveOnFileIfOppositionPieceInTheWay() {
        // setup
        Queen queen = new Queen(Colour.White);

        // act & assert
        assertFalse(queen.canMoveTo("f1", "f4", false, square -> "f3".equals(square)? new Pawn(Colour.Black) : null));
    }

    @Test
    public void shouldBeAbleToMoveOnFileIfCapturingOppositionPiece() {
        // setup
        Queen queen = new Queen(Colour.White);

        // act & assert
        assertTrue(queen.canMoveTo("c1", "c4", true, square -> "c4".equals(square)? new Pawn(Colour.Black) : null));
    }
}