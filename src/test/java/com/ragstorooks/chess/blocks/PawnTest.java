package com.ragstorooks.chess.blocks;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PawnTest {
    @Test
    public void shouldBeAbleToMoveOneStepForwardAsWhite() {
        // setup
        Piece pawn = new Pawn(Colour.White);

        // act & assert
        assertTrue(pawn.canMoveTo("a2", "a3", false, square -> null));
    }

    @Test
    public void shouldBeAbleToMoveOneStepForwardAsBlack() {
        // setup
        Piece pawn = new Pawn(Colour.Black);

        // act & assert
        assertTrue(pawn.canMoveTo("a3", "a2", false, square -> null));
    }

    @Test
    public void shouldNotBeAbleToMoveToADifferentFileIfNotCapture() {
        // setup
        Piece pawn = new Pawn(Colour.White);

        // act & assert
        assertFalse(pawn.canMoveTo("a2", "b3", false, square -> null));
    }

    @Test
    public void shouldNotMoveBackwardsAsWhite() {
        // setup
        Piece pawn = new Pawn(Colour.White);

        // act & assert
        assertFalse(pawn.canMoveTo("a3", "a2", false, square -> null));
    }

    @Test
    public void shouldNotMoveBackwardsAsBlack() {
        // setup
        Piece pawn = new Pawn(Colour.Black);

        // act & assert
        assertFalse(pawn.canMoveTo("a2", "a3", false, square -> null));
    }

    @Test
    public void shouldBeAbleToMoveTwoStepsForwardAsWhiteFromOriginalSquare() {
        // setup
        Piece pawn = new Pawn(Colour.White);

        // act & assert
        assertTrue(pawn.canMoveTo("a2", "a4", false, square -> null));
    }

    @Test
    public void shouldBeAbleToMoveTwoStepsForwardAsBlack() {
        // setup
        Piece pawn = new Pawn(Colour.Black);

        // act & assert
        assertTrue(pawn.canMoveTo("a7", "a5", false, square -> null));
    }

    @Test
    public void shouldNotBeAbleToMoveTwoStepsForwardAsWhiteFromRandomSquare() {
        // setup
        Piece pawn = new Pawn(Colour.White);

        // act & assert
        assertFalse(pawn.canMoveTo("a4", "a6", false, square -> null));
    }

    @Test
    public void shouldNotBeAbleToMoveTwoStepsForwardAsBlackFromRandomSquare() {
        // setup
        Piece pawn = new Pawn(Colour.Black);

        // act & assert
        assertFalse(pawn.canMoveTo("a5", "a3", false, square -> null));
    }

    @Test
    public void shouldNotBeAbleToMoveThreeStepsForward() {
        // setup
        Piece pawn = new Pawn(Colour.White);

        // act & assert
        assertFalse(pawn.canMoveTo("a2", "a5", false, square -> null));
    }

    @Test
    public void shouldBeAbleToMoveToADifferentFileIfCapture() {
        // setup
        Piece pawn = new Pawn(Colour.White);

        // act & assert
        assertTrue(pawn.canMoveTo("a2", "b3", true, square -> new Pawn(Colour.Black)));
    }

    @Test
    public void shouldNotBeAbleToCaptureOwnPiece() {
        // setup
        Piece pawn = new Pawn(Colour.White);

        // act & assert
        assertFalse(pawn.canMoveTo("a2", "b3", true, square -> new Pawn(Colour.White)));
    }

    @Test
    public void shouldNotBeAbleToMoveToTwoFilesForCapture() {
        // setup
        Piece pawn = new Pawn(Colour.White);

        // act & assert
        assertFalse(pawn.canMoveTo("a2", "c3", true, square -> new Pawn(Colour.Black)));
    }

    @Test
    public void shouldNotBeAbleToMoveToTwoRanksForCapture() {
        // setup
        Piece pawn = new Pawn(Colour.White);

        // act & assert
        assertFalse(pawn.canMoveTo("a2", "b4", true, square -> new Pawn(Colour.Black)));
    }

    // TODO Tests for promotions

    // TODO Tests for en-passant
}