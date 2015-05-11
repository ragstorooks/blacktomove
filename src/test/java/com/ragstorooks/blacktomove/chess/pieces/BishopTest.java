package com.ragstorooks.blacktomove.chess.pieces;

import com.ragstorooks.blacktomove.chess.blocks.Colour;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BishopTest {
    @Test
    public void shouldBeAbleToMoveAlongADiagonal() {
        // setup
        Bishop bishop = new Bishop(Colour.White);

        // act & assert
        assertTrue(bishop.canMoveTo("f1", "c4", false, square -> null));
    }

    @Test
    public void shouldNotBeAbleToMoveIfNotADiagonal() {
        // setup
        Bishop bishop = new Bishop(Colour.White);

        // act & assert
        assertFalse(bishop.canMoveTo("f1", "b4", false, square -> null));
    }

    @Test
    public void shouldNotBeAbleToMoveIfOwnPieceInTheWay() {
        // setup
        Bishop bishop = new Bishop(Colour.White);

        // act & assert
        assertFalse(bishop.canMoveTo("f1", "c4", false, square -> "d3".equals(square)? new Pawn(Colour.White) : null));
    }

    @Test
    public void shouldNotBeAbleToMoveIfOwnPieceAtDestination() {
        // setup
        Bishop bishop = new Bishop(Colour.White);

        // act & assert
//        assertFalse(bishop.canMoveTo("f1", "c4", false, square -> square.equals("c4")? new Pawn(Colour.White) : null));
        assertFalse(bishop.canMoveTo("f1", "c4", false, square -> "c4".equals(square)? new Pawn(Colour.White) : null));
    }

    @Test
    public void shouldNotBeAbleToMoveIfOppositionPieceInTheWay() {
        // setup
        Bishop bishop = new Bishop(Colour.White);

        // act & assert
        assertFalse(bishop.canMoveTo("f1", "c4", false, square -> "d3".equals(square)? new Pawn(Colour.Black) : null));
    }

    @Test
    public void shouldBeAbleToMoveIfCapturingOppositionPiece() {
        // setup
        Bishop bishop = new Bishop(Colour.White);

        // act & assert
        assertTrue(bishop.canMoveTo("f1", "c4", true, square -> "c4".equals(square)? new Pawn(Colour.Black) : null));
    }
}