package com.ragstorooks.chess.pieces;

import com.ragstorooks.chess.blocks.Colour;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RookTest {
    @Test
    public void shouldBeAbleToMoveAlongAFile() {
        // setup
        Rook rook = new Rook(Colour.White);

        // act & assert
        assertTrue(rook.canMoveTo("f1", "f4", false, square -> null));
    }

    @Test
    public void shouldBeAbleToMoveAlongARank() {
        // setup
        Rook rook = new Rook(Colour.White);

        // act & assert
        assertTrue(rook.canMoveTo("f1", "b1", false, square -> null));
    }

    @Test
    public void shouldNotBeAbleToMoveIfNotAlongARankOrAFile() {
        // setup
        Rook rook = new Rook(Colour.White);

        // act & assert
        assertFalse(rook.canMoveTo("f1", "c4", false, square -> null));
    }

    @Test
    public void shouldNotBeAbleToMoveIfOwnPieceInTheWay() {
        // setup
        Rook rook = new Rook(Colour.White);

        // act & assert
        assertFalse(rook.canMoveTo("f1", "f4", false, square -> square.equals("f3")? new Pawn(Colour.White) : null));
    }

    @Test
    public void shouldNotBeAbleToMoveIfOwnPieceAtDestination() {
        // setup
        Rook rook = new Rook(Colour.White);

        // act & assert
        assertFalse(rook.canMoveTo("f1", "f4", false, square -> square.equals("f4")? new Pawn(Colour.White) : null));
    }

    @Test
    public void shouldNotBeAbleToMoveIfOppositionPieceInTheWay() {
        // setup
        Rook rook = new Rook(Colour.White);

        // act & assert
        assertFalse(rook.canMoveTo("f1", "f4", false, square -> square.equals("f3")? new Pawn(Colour.Black) : null));
    }

    @Test
    public void shouldBeAbleToMoveIfCapturingOppositionPiece() {
        // setup
        Rook rook = new Rook(Colour.White);

        // act & assert
        assertTrue(rook.canMoveTo("c1", "c4", true, square -> square.equals("c4")? new Pawn(Colour.Black) : null));
    }
}