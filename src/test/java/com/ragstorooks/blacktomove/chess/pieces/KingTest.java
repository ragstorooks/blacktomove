package com.ragstorooks.blacktomove.chess.pieces;

import com.ragstorooks.blacktomove.chess.blocks.Colour;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class KingTest {
    @Test
    public void shouldBeAbleToMoveOneStepAlongADiagonal() {
        // setup
        King king = new King(Colour.White);

        // act & assert
        assertTrue(king.canMoveTo("f1", "g2", false, square -> null));
    }

    @Test
    public void shouldNotBeAbleToMoveMoreThanOneStepOnADiagonal() {
        // setup
        King king = new King(Colour.White);

        // act & assert
        assertFalse(king.canMoveTo("f1", "h3", false, square -> null));
    }

    @Test
    public void shouldNotBeAbleToMoveInDiagonalIfOwnPieceAtDestination() {
        // setup
        King king = new King(Colour.White);

        // act & assert
        assertFalse(king.canMoveTo("f1", "g2", false, square -> "g2".equals(square) ? new Pawn(Colour.White) : null));
    }

    @Test
    public void shouldBeAbleToMoveInDiagonalIfCapturingOppositionPiece() {
        // setup
        King king = new King(Colour.White);

        // act & assert
        assertTrue(king.canMoveTo("f1", "g2", true, square -> "g2".equals(square) ? new Pawn(Colour.Black) : null));
    }

    @Test
    public void shouldBeAbleToMoveAlongAFile() {
        // setup
        King king = new King(Colour.White);

        // act & assert
        assertTrue(king.canMoveTo("f1", "f2", false, square -> null));
    }

    @Test
    public void shouldBeAbleToMoveAlongARank() {
        // setup
        King king = new King(Colour.White);

        // act & assert
        assertTrue(king.canMoveTo("f1", "g1", false, square -> null));
    }

    @Test
    public void shouldNotBeAbleToMoveIfMoreThanOneStepOnARankOrAFile() {
        // setup
        King king = new King(Colour.White);

        // act & assert
        assertFalse(king.canMoveTo("f1", "f3", false, square -> null));
    }

    @Test
    public void shouldNotBeAbleToMoveIfMoreThanOneStepInRandomDirection() {
        // setup
        King king = new King(Colour.White);

        // act & assert
        assertFalse(king.canMoveTo("f1", "g3", false, square -> null));
    }

    @Test
    public void shouldNotBeAbleToMoveOnFileIfOwnPieceAtDestination() {
        // setup
        King king = new King(Colour.White);

        // act & assert
        assertFalse(king.canMoveTo("f1", "f2", false, square -> "f2".equals(square)? new Pawn(Colour.White) : null));
    }

    @Test
    public void shouldBeAbleToMoveOnFileIfCapturingOppositionPiece() {
        // setup
        King king = new King(Colour.White);

        // act & assert
        assertTrue(king.canMoveTo("c1", "c2", true, square -> "c2".equals(square)? new Pawn(Colour.Black) : null));
    }
}