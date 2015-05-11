package com.ragstorooks.blacktomove.chess.moves;

import com.ragstorooks.blacktomove.chess.blocks.Colour;
import com.ragstorooks.blacktomove.chess.pieces.PieceType;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class MoveFactoryTest {
    private MoveFactory moveFactory = new MoveFactory();

    @Test
    public void shouldCreatePawnMove() {
        // setup
        Move expectedMove = new BasicMove(Colour.White, PieceType.PAWN, "h3", false, null);

        // act
        Move move = moveFactory.createMove(Colour.White, "h3");

        // assert
        assertThat(move, equalTo(expectedMove));
    }

    @Test
    public void shouldCreatePawnMoveForCapture() {
        // setup
        Move expectedMove = new BasicMove(Colour.White, PieceType.PAWN, "h3", true, "g");

        // act
        Move move = moveFactory.createMove(Colour.White, "gxh3");

        // assert
        assertThat(move, equalTo(expectedMove));
    }

    @Test
    public void shouldCreatePawnMoveForPromotion() {
        // setup
        Move expectedMove = new Promotion(Colour.White, PieceType.QUEEN, "h8", false, null);

        // act
        Move move = moveFactory.createMove(Colour.White, "h8Q");

        // assert
        assertThat(move, equalTo(expectedMove));
    }

    @Test
    public void shouldCreatePawnMoveForPromotionWithCapture() {
        // setup
        Move expectedMove = new Promotion(Colour.Black, PieceType.ROOK, "h1", true, "g");

        // act
        Move move = moveFactory.createMove(Colour.Black, "gxh1R");

        // assert
        assertThat(move, equalTo(expectedMove));
    }

    @Test
    public void shouldCreateRookMove() {
        // setup
        Move expectedMove = new BasicMove(Colour.White, PieceType.ROOK, "h3", false, null);

        // act
        Move move = moveFactory.createMove(Colour.White, "Rh3");

        // assert
        assertThat(move, equalTo(expectedMove));
    }

    @Test
    public void shouldCreateRookMoveWithHintAndCapture() {
        // setup
        Move expectedMove = new BasicMove(Colour.White, PieceType.ROOK, "h3", true, "b");

        // act
        Move move = moveFactory.createMove(Colour.White, "Rbxh3");

        // assert
        assertThat(move, equalTo(expectedMove));
    }

    @Test
    public void shouldCreateKnightMove() {
        // setup
        Move expectedMove = new BasicMove(Colour.White, PieceType.KNIGHT, "h3", false, null);

        // act
        Move move = moveFactory.createMove(Colour.White, "Nh3");

        // assert
        assertThat(move, equalTo(expectedMove));
    }

    @Test
    public void shouldCreateKnightMoveWithHintAndCapture() {
        // setup
        Move expectedMove = new BasicMove(Colour.White, PieceType.KNIGHT, "h3", true, "b");

        // act
        Move move = moveFactory.createMove(Colour.White, "Nbxh3");

        // assert
        assertThat(move, equalTo(expectedMove));
    }

    @Test
    public void shouldCreateBishopMove() {
        // setup
        Move expectedMove = new BasicMove(Colour.White, PieceType.BISHOP, "h3", false, null);

        // act
        Move move = moveFactory.createMove(Colour.White, "Bh3");

        // assert
        assertThat(move, equalTo(expectedMove));
    }

    @Test
    public void shouldCreateBishopMoveWithHintAndCapture() {
        // setup
        Move expectedMove = new BasicMove(Colour.White, PieceType.BISHOP, "h3", true, "b");

        // act
        Move move = moveFactory.createMove(Colour.White, "Bbxh3");

        // assert
        assertThat(move, equalTo(expectedMove));
    }

    @Test
    public void shouldCreateQueenMove() {
        // setup
        Move expectedMove = new BasicMove(Colour.White, PieceType.QUEEN, "h3", false, null);

        // act
        Move move = moveFactory.createMove(Colour.White, "Qh3");

        // assert
        assertThat(move, equalTo(expectedMove));
    }

    @Test
    public void shouldCreateQueenMoveAndIgnoreCheckSymbol() {
        // setup
        Move expectedMove = new BasicMove(Colour.White, PieceType.QUEEN, "h3", false, null);

        // act
        Move move = moveFactory.createMove(Colour.White, "Qh3+");

        // assert
        assertThat(move, equalTo(expectedMove));
    }

    @Test
    public void shouldCreateQueenMoveAndIgnoreCheckMateSymbol() {
        // setup
        Move expectedMove = new BasicMove(Colour.White, PieceType.QUEEN, "h3", false, null);

        // act
        Move move = moveFactory.createMove(Colour.White, "Qh3#");

        // assert
        assertThat(move, equalTo(expectedMove));
    }

    @Test
    public void shouldCreateQueenMoveWithHintAndCapture() {
        // setup
        Move expectedMove = new BasicMove(Colour.White, PieceType.QUEEN, "h3", true, "b");

        // act
        Move move = moveFactory.createMove(Colour.White, "Qbxh3");

        // assert
        assertThat(move, equalTo(expectedMove));
    }

    @Test
    public void shouldCreateKingMove() {
        // setup
        Move expectedMove = new BasicMove(Colour.White, PieceType.KING, "h3", false, null);

        // act
        Move move = moveFactory.createMove(Colour.White, "Kh3");

        // assert
        assertThat(move, equalTo(expectedMove));
    }

    @Test
    public void shouldCreateKingsideCastle() {
        // setup
        Move expectedMove = new KingsideCastle(Colour.White);

        // act
        Move move = moveFactory.createMove(Colour.White, "O-O");

        // assert
        assertThat(move, equalTo(expectedMove));
    }

    @Test
    public void shouldCreateQueensideCastle() {
        // setup
        Move expectedMove = new QueensideCastle(Colour.White);

        // act
        Move move = moveFactory.createMove(Colour.White, "O-O-O");

        // assert
        assertThat(move, equalTo(expectedMove));
    }
}