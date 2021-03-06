package com.ragstorooks.blacktomove.chess.pieces;

import com.ragstorooks.blacktomove.chess.blocks.Colour;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class PawnTest {
    @Test
    public void shouldBeAbleToMoveOneStepForwardAsWhite() {
        // setup
        Pawn pawn = new Pawn(Colour.White);

        // act & assert
        assertTrue(pawn.canMoveTo("a2", "a3", false, square -> null));
    }

    @Test
    public void shouldBeAbleToMoveOneStepForwardAsBlack() {
        // setup
        Pawn pawn = new Pawn(Colour.Black);

        // act & assert
        assertTrue(pawn.canMoveTo("a3", "a2", false, square -> null));
    }

    @Test
    public void shouldNotBeAbleToMoveToTheSameSquare() {
        // setup
        Pawn pawn = new Pawn(Colour.White);

        // act & assert
        assertFalse(pawn.canMoveTo("a2", "a2", false, square -> null));
    }

    @Test
    public void shouldNotBeAbleToMoveToADifferentFileIfNotCapture() {
        // setup
        Pawn pawn = new Pawn(Colour.White);

        // act & assert
        assertFalse(pawn.canMoveTo("a2", "b3", false, square -> null));
    }

    @Test
    public void shouldNotMoveBackwardsAsWhite() {
        // setup
        Pawn pawn = new Pawn(Colour.White);

        // act & assert
        assertFalse(pawn.canMoveTo("a3", "a2", false, square -> null));
    }

    @Test
    public void shouldNotMoveBackwardsAsBlack() {
        // setup
        Pawn pawn = new Pawn(Colour.Black);

        // act & assert
        assertFalse(pawn.canMoveTo("a2", "a3", false, square -> null));
    }

    @Test
    public void shouldBeAbleToMoveTwoStepsForwardAsWhiteFromOriginalSquare() {
        // setup
        Pawn pawn = new Pawn(Colour.White);

        // act & assert
        assertTrue(pawn.canMoveTo("a2", "a4", false, square -> null));
    }

    @Test
    public void shouldBeAbleToMoveTwoStepsForwardAsBlack() {
        // setup
        Pawn pawn = new Pawn(Colour.Black);

        // act & assert
        assertTrue(pawn.canMoveTo("a7", "a5", false, square -> null));
    }

    @Test
    public void shouldNotBeAbleToMoveTwoStepsForwardAsWhiteFromRandomSquare() {
        // setup
        Pawn pawn = new Pawn(Colour.White);

        // act & assert
        assertFalse(pawn.canMoveTo("a4", "a6", false, square -> null));
    }

    @Test
    public void shouldNotBeAbleToMoveTwoStepsForwardAsBlackFromRandomSquare() {
        // setup
        Pawn pawn = new Pawn(Colour.Black);

        // act & assert
        assertFalse(pawn.canMoveTo("a5", "a3", false, square -> null));
    }

    @Test
    public void shouldNotBeAbleToMoveThreeStepsForward() {
        // setup
        Pawn pawn = new Pawn(Colour.White);

        // act & assert
        assertFalse(pawn.canMoveTo("a2", "a5", false, square -> null));
    }

    @Test
    public void shouldBeAbleToMoveToADifferentFileIfCapture() {
        // setup
        Pawn pawn = new Pawn(Colour.White);

        // act & assert
        assertTrue(pawn.canMoveTo("a2", "b3", true, square -> new Pawn(Colour.Black)));
    }

    @Test
    public void shouldNotBeAbleToCaptureOwnPiece() {
        // setup
        Pawn pawn = new Pawn(Colour.White);

        // act & assert
        assertFalse(pawn.canMoveTo("a2", "b3", true, square -> new Pawn(Colour.White)));
    }

    @Test
    public void shouldNotBeAbleToMoveToTwoFilesForCapture() {
        // setup
        Pawn pawn = new Pawn(Colour.White);

        // act & assert
        assertFalse(pawn.canMoveTo("a2", "c3", true, square -> new Pawn(Colour.Black)));
    }

    @Test
    public void shouldNotBeAbleToMoveToTwoRanksForCapture() {
        // setup
        Pawn pawn = new Pawn(Colour.White);

        // act & assert
        assertFalse(pawn.canMoveTo("a2", "b4", true, square -> new Pawn(Colour.Black)));
    }

    @Test
    public void shouldNotBeAbleToMoveTwoRanksIfSquareInMiddleIsOccupied() {
        // setup
        Pawn pawn = new Pawn(Colour.White);

        // act & assert
        assertFalse(pawn.canMoveTo("a2", "a4", false, square -> "a3".equals(square)? new Pawn(Colour.Black) : null));
    }

    @Test
    public void whitePawnShouldNotBeAbleToMoveToSecondRank() {
        // setup
        Pawn pawn = new Pawn(Colour.White);

        // act & assert
        assertFalse(pawn.canMoveTo("a1", "a2", false, square -> null));
    }

    @Test
    public void blackPawnShouldNotBeAbleToMoveToSeventhRank() {
        // setup
        Pawn pawn = new Pawn(Colour.Black);

        // act & assert
        assertFalse(pawn.canMoveTo("a8", "a7", false, square -> null));
    }

    // TODO Tests for en-passant
}