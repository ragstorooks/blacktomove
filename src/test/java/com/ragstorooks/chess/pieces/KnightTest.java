package com.ragstorooks.chess.pieces;


import com.ragstorooks.chess.blocks.Colour;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class KnightTest {
    @Test
    public void shouldBeAbleToMoveTwoRanksAndAFile() {
        // setup
        Knight knight = new Knight(Colour.Black);

        // act & assert
        assertTrue(knight.canMoveTo("b1", "c3", false, square -> null));
    }

    @Test
    public void shouldBeAbleToMoveTwoFilesAndARank() {
        // setup
        Knight knight = new Knight(Colour.Black);

        // act & assert
        assertTrue(knight.canMoveTo("b1", "d2", false, square -> null));
    }

    @Test
    public void shouldBeAbleToCaptureOppositionPiece() {
        // setup
        Knight knight = new Knight(Colour.Black);

        // act & assert
        assertTrue(knight.canMoveTo("b1", "d2", true, square -> new Pawn(Colour.White)));
    }

    @Test
    public void shouldNotBeAbleToCaptureOwnPiece() {
        // setup
        Knight knight = new Knight(Colour.Black);

        // act & assert
        assertFalse(knight.canMoveTo("b1", "d2", true, square -> new Rook(Colour.Black)));
    }

    @Test
    public void shouldNotBeAbleToMove3Ranks() {
        // setup
        Knight knight = new Knight(Colour.White);

        // act & assert
        assertFalse(knight.canMoveTo("b1", "b4", false, square -> null));
    }

    @Test
    public void shouldNotBeAbleToMove3Files() {
        // setup
        Knight knight = new Knight(Colour.White);

        // act & assert
        assertFalse(knight.canMoveTo("b1", "e1", false, square -> null));
    }
}