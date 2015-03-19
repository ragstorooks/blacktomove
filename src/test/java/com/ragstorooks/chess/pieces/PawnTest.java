package com.ragstorooks.chess.pieces;

import com.ragstorooks.chess.blocks.Colour;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PawnTest {
    @Test
    public void shouldBeAbleToMoveOneStepForwardAsWhite() {
        // setup
        AbstractPiece pawn = new Pawn(Colour.White);

        // act & assert
        assertTrue(pawn.canMoveTo("a2", "a3", false, square -> null));
    }

    @Test
    public void shouldBeAbleToMoveOneStepForwardAsBlack() {
        // setup
        AbstractPiece pawn = new Pawn(Colour.Black);

        // act & assert
        assertTrue(pawn.canMoveTo("a3", "a2", false, square -> null));
    }

    @Test
    public void shouldNotBeAbleToMoveToADifferentFileIfNotCapture() {
        // setup
        AbstractPiece pawn = new Pawn(Colour.White);

        // act & assert
        assertFalse(pawn.canMoveTo("a2", "b3", false, square -> null));
    }

    @Test
    public void shouldNotMoveBackwardsAsWhite() {
        // setup
        AbstractPiece pawn = new Pawn(Colour.White);

        // act & assert
        assertFalse(pawn.canMoveTo("a3", "a2", false, square -> null));
    }

    @Test
    public void shouldNotMoveBackwardsAsBlack() {
        // setup
        AbstractPiece pawn = new Pawn(Colour.Black);

        // act & assert
        assertFalse(pawn.canMoveTo("a2", "a3", false, square -> null));
    }

    @Test
    public void shouldBeAbleToMoveTwoStepsForwardAsWhiteFromOriginalSquare() {
        // setup
        AbstractPiece pawn = new Pawn(Colour.White);

        // act & assert
        assertTrue(pawn.canMoveTo("a2", "a4", false, square -> null));
    }

    @Test
    public void shouldBeAbleToMoveTwoStepsForwardAsBlack() {
        // setup
        AbstractPiece pawn = new Pawn(Colour.Black);

        // act & assert
        assertTrue(pawn.canMoveTo("a7", "a5", false, square -> null));
    }

    @Test
    public void shouldNotBeAbleToMoveTwoStepsForwardAsWhiteFromRandomSquare() {
        // setup
        AbstractPiece pawn = new Pawn(Colour.White);

        // act & assert
        assertFalse(pawn.canMoveTo("a4", "a6", false, square -> null));
    }

    @Test
    public void shouldNotBeAbleToMoveTwoStepsForwardAsBlackFromRandomSquare() {
        // setup
        AbstractPiece pawn = new Pawn(Colour.Black);

        // act & assert
        assertFalse(pawn.canMoveTo("a5", "a3", false, square -> null));
    }

    @Test
    public void shouldNotBeAbleToMoveThreeStepsForward() {
        // setup
        AbstractPiece pawn = new Pawn(Colour.White);

        // act & assert
        assertFalse(pawn.canMoveTo("a2", "a5", false, square -> null));
    }

    @Test
    public void shouldBeAbleToMoveToADifferentFileIfCapture() {
        // setup
        AbstractPiece pawn = new Pawn(Colour.White);

        // act & assert
        assertTrue(pawn.canMoveTo("a2", "b3", true, square -> new Pawn(Colour.Black)));
    }

    @Test
    public void shouldNotBeAbleToCaptureOwnPiece() {
        // setup
        AbstractPiece pawn = new Pawn(Colour.White);

        // act & assert
        assertFalse(pawn.canMoveTo("a2", "b3", true, square -> new Pawn(Colour.White)));
    }

    @Test
    public void shouldNotBeAbleToMoveToTwoFilesForCapture() {
        // setup
        AbstractPiece pawn = new Pawn(Colour.White);

        // act & assert
        assertFalse(pawn.canMoveTo("a2", "c3", true, square -> new Pawn(Colour.Black)));
    }

    @Test
    public void shouldNotBeAbleToMoveToTwoRanksForCapture() {
        // setup
        AbstractPiece pawn = new Pawn(Colour.White);

        // act & assert
        assertFalse(pawn.canMoveTo("a2", "b4", true, square -> new Pawn(Colour.Black)));
    }

    // TODO Tests for promotions

    // TODO Tests for en-passant
}