package com.ragstorooks.chess.moves;

import com.ragstorooks.chess.blocks.Colour;
import com.ragstorooks.chess.pieces.PieceType;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class MoveFactoryTest {
    @Test
    public void shouldCreatePawnMove() {
        // setup
        Move expectedMove = new BasicMove(Colour.White, PieceType.PAWN, "h3", false, null);

        // act
        Move move = MoveFactory.createMove(Colour.White, "h3");

        // assert
        assertThat(move, equalTo(expectedMove));
    }

    @Test
    public void shouldCreatePawnMoveForCapture() {
        // setup
        Move expectedMove = new BasicMove(Colour.White, PieceType.PAWN, "h3", false, "g");

        // act
        Move move = MoveFactory.createMove(Colour.White, "gxh3");

        // assert
        assertThat(move, equalTo(expectedMove));
    }

    @Test
    public void shouldCreateRookMove() {
        // setup
        Move expectedMove = new BasicMove(Colour.White, PieceType.ROOK, "Rh3", false, null);

        // act
        Move move = MoveFactory.createMove(Colour.White, "Rh3");

        // assert
        assertThat(move, equalTo(expectedMove));
    }

    @Test
    public void shouldCreateRookMoveWithHintAndCapture() {
        // setup
        Move expectedMove = new BasicMove(Colour.White, PieceType.ROOK, "Rbxh3", false, "b");

        // act
        Move move = MoveFactory.createMove(Colour.White, "Rbxh3");

        // assert
        assertThat(move, equalTo(expectedMove));
    }

    @Test
    public void shouldCreateKnightMove() {
        // setup
        Move expectedMove = new BasicMove(Colour.White, PieceType.KNIGHT, "Nh3", false, null);

        // act
        Move move = MoveFactory.createMove(Colour.White, "Nh3");

        // assert
        assertThat(move, equalTo(expectedMove));
    }

    @Test
    public void shouldCreateKnightMoveWithHintAndCapture() {
        // setup
        Move expectedMove = new BasicMove(Colour.White, PieceType.KNIGHT, "Nbxh3", false, "b");

        // act
        Move move = MoveFactory.createMove(Colour.White, "Nbxh3");

        // assert
        assertThat(move, equalTo(expectedMove));
    }

    @Test
    public void shouldCreateBishopMove() {
        // setup
        Move expectedMove = new BasicMove(Colour.White, PieceType.BISHOP, "Bh3", false, null);

        // act
        Move move = MoveFactory.createMove(Colour.White, "Bh3");

        // assert
        assertThat(move, equalTo(expectedMove));
    }

    @Test
    public void shouldCreateBishopMoveWithHintAndCapture() {
        // setup
        Move expectedMove = new BasicMove(Colour.White, PieceType.BISHOP, "Bbxh3", false, "b");

        // act
        Move move = MoveFactory.createMove(Colour.White, "Bbxh3");

        // assert
        assertThat(move, equalTo(expectedMove));
    }

    @Test
    public void shouldCreateQueenMove() {
        // setup
        Move expectedMove = new BasicMove(Colour.White, PieceType.QUEEN, "Qh3", false, null);

        // act
        Move move = MoveFactory.createMove(Colour.White, "Qh3");

        // assert
        assertThat(move, equalTo(expectedMove));
    }

    @Test
    public void shouldCreateQueenMoveWithHintAndCapture() {
        // setup
        Move expectedMove = new BasicMove(Colour.White, PieceType.QUEEN, "Qbxh3", false, "b");

        // act
        Move move = MoveFactory.createMove(Colour.White, "Qbxh3");

        // assert
        assertThat(move, equalTo(expectedMove));
    }

    @Test
    public void shouldCreateKingMove() {
        // setup
        Move expectedMove = new BasicMove(Colour.White, PieceType.KING, "Kh3", false, null);

        // act
        Move move = MoveFactory.createMove(Colour.White, "Kh3");

        // assert
        assertThat(move, equalTo(expectedMove));
    }
}