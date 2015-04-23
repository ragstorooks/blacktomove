package com.ragstorooks.chess.blocks;


import com.ragstorooks.chess.pieces.King;
import com.ragstorooks.chess.pieces.Pawn;
import com.ragstorooks.chess.pieces.Piece;
import com.ragstorooks.chess.pieces.PieceType;
import com.ragstorooks.chess.pieces.Rook;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class BoardTest {
    @Test
    public void testInitialBoardGetsCreated() {
        String expected = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";

        // act
        Board board = new Board();

        // assert
        assertThat(board.toString(), equalTo(expected));
    }

    @Test
    public void testThatNewBoardConstructedWith1E4ContainsAPawnOnE4() {
        Piece expected = new Pawn(Colour.White);

        // act
        Board board = new Board("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR");

        // assert
        assertThat(board.get("e4"), equalTo(expected));
    }

    @Test
    public void testThatNewBoardConstructedWith1E4ContainsAnEmptySquareOnE5() {
        // act
        Board board = new Board("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR");

        // assert
        assertThat(board.get("e5"), equalTo(null));
    }

    @Test
    public void testThatWeCanPlaceAPawnOnE4() {
        String expected = "rnbqkbnr/pppppppp/8/8/4P3/8/PPPPPPPP/RNBQKBNR";
        Board board = new Board();

        // act
        board.put("e4", new Pawn(Colour.White));

        // assert
        assertThat(board.toString(), equalTo(expected));
    }

    @Test
    public void testThatWeCanClearTheE2Square() {
        String expected = "rnbqkbnr/pppppppp/8/8/8/8/PPPP1PPP/RNBQKBNR";
        Board board = new Board();

        // act
        board.put("e2", null);

        // assert
        assertThat(board.toString(), equalTo(expected));
    }

    @Test
    public void testThatBlackRooksAreOnA8AndH8() {
        // setup
        Rook rook = new Rook(Colour.Black);

        // act
        Map<String, Piece> blackRooks = new Board().getPiecesOfType(Colour.Black, PieceType.ROOK);

        // verify
        assertThat(blackRooks.size(), equalTo(2));
        assertThat(blackRooks.get("a8"), equalTo(rook));
        assertThat(blackRooks.get("h8"), equalTo(rook));
    }

    @Test
    public void testThatAllWhitePiecesAreIdentifiedOnASparseBoard() {
        // setup
        King king = new King(Colour.White);
        Pawn pawn = new Pawn(Colour.White);
        Board board = new Board("8/8/8/7k/8/6PP/7K/8");

        // act
        Map<String, Piece> whitePieces = board.getPiecesOfColour(Colour.White);

        // verify
        assertThat(whitePieces.size(), equalTo(3));
        assertThat(whitePieces.get("h2"), equalTo(king));
        assertThat(whitePieces.get("h3"), equalTo(pawn));
        assertThat(whitePieces.get("g3"), equalTo(pawn));
    }

    @Test
    public void testThatCopyOfBoardContainsTheSameMappingOfPieces() {
        // setup
        String position = "8/8/8/7k/8/6PP/7K/8";
        Board board = new Board(position);

        // act
        Board newBoard = board.copy();

        // verify
        assertThat(newBoard.toString(), equalTo(position));
    }
}