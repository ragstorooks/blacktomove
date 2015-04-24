package com.ragstorooks.chess;

import com.ragstorooks.chess.blocks.Board;
import com.ragstorooks.chess.blocks.Colour;
import com.ragstorooks.chess.blocks.Position;
import com.ragstorooks.chess.moves.BasicMove;
import com.ragstorooks.chess.moves.KingsideCastle;
import com.ragstorooks.chess.moves.Promotion;
import com.ragstorooks.chess.moves.QueensideCastle;
import com.ragstorooks.chess.pieces.King;
import com.ragstorooks.chess.pieces.Piece;
import com.ragstorooks.chess.pieces.PieceType;
import com.ragstorooks.chess.pieces.Queen;
import com.ragstorooks.chess.pieces.Rook;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GameTest {
    private Map<String, Piece> pieces = new HashMap<>();

    @Mock
    private Piece whitePawn;
    @Mock
    private Piece whiteRook;
    @Mock
    private Piece whiteKing;
    @Mock
    private Piece blackPawn;
    @Mock
    private Piece blackRook;
    @Mock
    private Piece blackKing;
    @Mock
    private Board gameBoard;
    @Mock
    private Board tempBoard;

    private Game game;

    @Before
    public void setupWhiteRook() {
        when(whiteRook.getColour()).thenReturn(Colour.White);
        when(whiteRook.getPieceType()).thenReturn(PieceType.ROOK);
    }

    @Before
    public void setupBlackRook() {
        when(blackRook.getColour()).thenReturn(Colour.Black);
        when(blackRook.getPieceType()).thenReturn(PieceType.ROOK);
    }

    @Before
    public void setupWhiteKing() {
        when(whiteKing.getColour()).thenReturn(Colour.White);
        when(whiteKing.getPieceType()).thenReturn(PieceType.KING);
    }

    @Before
    public void setupBlackKing() {
        when(blackKing.getColour()).thenReturn(Colour.Black);
        when(blackKing.getPieceType()).thenReturn(PieceType.KING);
    }

    @Before
    public void setupWhitePawn() {
        when(whitePawn.getColour()).thenReturn(Colour.White);
        when(whitePawn.getPieceType()).thenReturn(PieceType.PAWN);
    }

    @Before
    public void setupBlackPawn() {
        when(blackPawn.getColour()).thenReturn(Colour.Black);
        when(blackPawn.getPieceType()).thenReturn(PieceType.PAWN);
    }

    @Before
    public void setupPieces() {
        pieces.put("a1", whiteRook);
        pieces.put("e1", whiteKing);
        pieces.put("h1", whiteRook);
        pieces.put("e2", whitePawn);
        pieces.put("a8", blackRook);
        pieces.put("e8", blackKing);
        pieces.put("h8", blackRook);
        pieces.put("e7", blackPawn);
    }

    @Before
    public void setupBoards() {
        when(gameBoard.copy()).thenReturn(tempBoard);
        setupBoard(gameBoard);
        setupBoard(tempBoard);

        game = new Game(gameBoard);
    }

    private void setupBoard(Board board) {
        doAnswer(new Answer() {
            @Override
            public Piece answer(InvocationOnMock invocation) throws Throwable {
                return pieces.get((String) invocation.getArguments()[0]);
            }
        }).when(board).get(isA(String.class));
        doAnswer(new Answer<Map<String, Piece>>() {
            @Override
            public Map<String, Piece> answer(InvocationOnMock invocation) throws Throwable {
                Map<String, Piece> result = new HashMap<>();
                Colour invokingColour = (Colour) invocation.getArguments()[0];

                pieces.entrySet().stream().filter(entry -> invokingColour.equals(entry.getValue().getColour()))
                        .forEach(entry -> result.put(entry.getKey(), entry.getValue()));
                return result;
            }
        }).when(board).getPiecesOfColour(isA(Colour.class));

        doAnswer(new Answer<Map<String, Piece>>() {
            @Override
            public Map<String, Piece> answer(InvocationOnMock invocation) throws Throwable {
                Map<String, Piece> result = new HashMap<>();
                Colour invokingColour = (Colour) invocation.getArguments()[0];
                PieceType pieceType = (PieceType) invocation.getArguments()[1];

                pieces.entrySet().stream().filter(entry -> invokingColour.equals(entry.getValue().getColour()) &&
                        pieceType.equals(entry.getValue().getPieceType())).forEach(entry -> result.put(entry.getKey()
                        , entry.getValue()));
                return result;
            }
        }).when(board).getPiecesOfType(isA(Colour.class), isA(PieceType.class));
    }

    @Test
    public void testThatGameMetadataIsStored() {
        // act
        game.addMeta("key1", "value1").addMeta("key2", "value2");

        // verify
        Map<String, String> metadata = game.getMetadata();
        assertThat(metadata.size(), equalTo(2));
        assertThat(metadata.get("key1"), equalTo("value1"));
        assertThat(metadata.get("key2"), equalTo("value2"));
    }

    @Test
    public void testThatBasicMoveIsMadeIfNoCheck() {
        // setup
        when(whitePawn.canMoveTo(eq("e2"), eq("e4"), eq(false), isA(Position.class))).thenReturn(true);

        // act
        game.makeMove(new BasicMove(Colour.White, PieceType.PAWN, "e4", false, null));

        // assert
        verify(gameBoard).put("e2", null);
        verify(gameBoard).put("e4", whitePawn);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThatBasicMoveIsNotMadeIfItResultsInCheck() {
        // setup
        when(blackRook.canMoveTo(eq("h8"), eq("e1"), eq(true), isA(Position.class))).thenReturn(true);
        when(whitePawn.canMoveTo(eq("e2"), eq("e4"), eq(false), isA(Position.class))).thenReturn(true);

        // act
        game.makeMove(new BasicMove(Colour.White, PieceType.PAWN, "e4", false, null));
    }

    @Test
    public void testThatPromotionWorks() {
        // setup
        when(whitePawn.canMoveTo(eq("e2"), eq("e8"), eq(false), isA(Position.class))).thenReturn(true);

        // act
        game.makeMove(new Promotion(Colour.White, PieceType.QUEEN, "e8", false, null));

        // assert
        verify(gameBoard).put("e2", null);
        verify(gameBoard).put(eq("e8"), argThat(new ArgumentMatcher<Piece>() {
            @Override
            public boolean matches(Object argument) {
                return (argument instanceof Queen) && ((Queen) argument).getColour().equals(Colour.White);
            }
        }));
    }

    @Test
    public void testKingsideCastleIfValid() {
        // act
        game.makeMove(new KingsideCastle(Colour.White));

        // assert
        verify(gameBoard).put("e1", null);
        verify(gameBoard).put("h1", null);
        verify(gameBoard).put(eq("g1"), argThat(new ArgumentMatcher<Piece>() {
            @Override
            public boolean matches(Object argument) {
                return (argument instanceof King) && ((King) argument).getColour().equals(Colour.White);
            }
        }));
        verify(gameBoard).put(eq("f1"), argThat(new ArgumentMatcher<Piece>() {
            @Override
            public boolean matches(Object argument) {
                return (argument instanceof Rook) && ((Rook) argument).getColour().equals(Colour.White);
            }
        }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKingsideCastleFailsIfBlackRookCanMoveToe1() {
        // setup
        when(blackRook.canMoveTo(eq("h8"), eq("e1"), eq(true), isA(Position.class))).thenReturn(true);

        // act
        game.makeMove(new KingsideCastle(Colour.White));
    }

    private void setupTempBoardToReturnKingOnSquare(String square) {
        doAnswer(new Answer<Map<String, Piece>>() {
            @Override
            public Map<String, Piece> answer(InvocationOnMock invocation) throws Throwable {
                Map<String, Piece> result = new HashMap<>();
                Colour invokingColour = (Colour) invocation.getArguments()[0];
                PieceType pieceType = (PieceType) invocation.getArguments()[1];

                if (Colour.White.equals(invokingColour) && PieceType.KING.equals(pieceType)) {
                    result.put(square, whiteKing);
                    return result;
                }

                pieces.entrySet().stream().filter(entry -> invokingColour.equals(entry.getValue().getColour()) &&
                        pieceType.equals(entry.getValue().getPieceType())).forEach(entry -> result.put(entry.getKey()
                        , entry.getValue()));
                return result;
            }
        }).when(tempBoard).getPiecesOfType(isA(Colour.class), isA(PieceType.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKingsideCastleFailsIfBlackRookCanMoveTof1() {
        // setup
        setupTempBoardToReturnKingOnSquare("f1");
        when(blackRook.canMoveTo(eq("h8"), eq("f1"), eq(true), isA(Position.class))).thenReturn(true);

        // act
        game.makeMove(new KingsideCastle(Colour.White));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKingsideCastleFailsIfBlackRookCanMoveTog1() {
        // setup
        setupTempBoardToReturnKingOnSquare("g1");
        when(blackRook.canMoveTo(eq("h8"), eq("g1"), eq(true), isA(Position.class))).thenReturn(true);

        // act
        game.makeMove(new KingsideCastle(Colour.White));
    }

    @Test
    public void testKingsideCastleIsValidEvenIfBlackRookAttacksh1() {
        // setup
        when(blackRook.canMoveTo(eq("h8"), eq("h1"), eq(true), isA(Position.class))).thenReturn(true);

        // act
        game.makeMove(new KingsideCastle(Colour.White));

        // assert
        verify(gameBoard).put("e1", null);
        verify(gameBoard).put("h1", null);
        verify(gameBoard).put(eq("g1"), argThat(new ArgumentMatcher<Piece>() {
            @Override
            public boolean matches(Object argument) {
                return (argument instanceof King) && ((King) argument).getColour().equals(Colour.White);
            }
        }));
        verify(gameBoard).put(eq("f1"), argThat(new ArgumentMatcher<Piece>() {
            @Override
            public boolean matches(Object argument) {
                return (argument instanceof Rook) && ((Rook) argument).getColour().equals(Colour.White);
            }
        }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKingsideCastleFailsIfPieceExistsOnf1() {
        // setup
        pieces.put("f1", whiteRook);

        // act
        game.makeMove(new KingsideCastle(Colour.White));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testKingsideCastleFailsIfPieceExistsOng1() {
        // setup
        pieces.put("g1", whiteRook);

        // act
        game.makeMove(new KingsideCastle(Colour.White));
    }

    @Test
    public void testQueensideCastleIfValid() {
        // act
        game.makeMove(new QueensideCastle(Colour.White));

        // assert
        verify(gameBoard).put("e1", null);
        verify(gameBoard).put("a1", null);
        verify(gameBoard).put(eq("c1"), argThat(new ArgumentMatcher<Piece>() {
            @Override
            public boolean matches(Object argument) {
                return (argument instanceof King) && ((King) argument).getColour().equals(Colour.White);
            }
        }));
        verify(gameBoard).put(eq("d1"), argThat(new ArgumentMatcher<Piece>() {
            @Override
            public boolean matches(Object argument) {
                return (argument instanceof Rook) && ((Rook) argument).getColour().equals(Colour.White);
            }
        }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQueensideCastleFailsIfBlackRookCanMoveToe1() {
        // setup
        when(blackRook.canMoveTo(eq("h8"), eq("e1"), eq(true), isA(Position.class))).thenReturn(true);

        // act
        game.makeMove(new QueensideCastle(Colour.White));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQueensideCastleFailsIfBlackRookCanMoveTod1() {
        // setup
        setupTempBoardToReturnKingOnSquare("d1");
        when(blackRook.canMoveTo(eq("h8"), eq("d1"), eq(true), isA(Position.class))).thenReturn(true);

        // act
        game.makeMove(new QueensideCastle(Colour.White));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQueensideCastleFailsIfBlackRookCanMoveToc1() {
        // setup
        setupTempBoardToReturnKingOnSquare("c1");
        when(blackRook.canMoveTo(eq("h8"), eq("c1"), eq(true), isA(Position.class))).thenReturn(true);

        // act
        game.makeMove(new QueensideCastle(Colour.White));
    }

    public void testQueensideCastleIsValidEvenIfBlackRookAttacksb1() {
        // setup
        when(blackRook.canMoveTo(eq("h8"), eq("b1"), eq(true), isA(Position.class))).thenReturn(true);

        // act
        game.makeMove(new QueensideCastle(Colour.White));

        // assert
        verify(gameBoard).put("e1", null);
        verify(gameBoard).put("a1", null);
        verify(gameBoard).put(eq("c1"), argThat(new ArgumentMatcher<Piece>() {
            @Override
            public boolean matches(Object argument) {
                return (argument instanceof King) && ((King) argument).getColour().equals(Colour.White);
            }
        }));
        verify(gameBoard).put(eq("d1"), argThat(new ArgumentMatcher<Piece>() {
            @Override
            public boolean matches(Object argument) {
                return (argument instanceof Rook) && ((Rook) argument).getColour().equals(Colour.White);
            }
        }));
    }

    @Test
    public void testQueensideCastleIsValidEvenIfBlackRookAttacksa1() {
        // setup
        when(blackRook.canMoveTo(eq("h8"), eq("a1"), eq(true), isA(Position.class))).thenReturn(true);

        // act
        game.makeMove(new QueensideCastle(Colour.White));

        // assert
        verify(gameBoard).put("e1", null);
        verify(gameBoard).put("a1", null);
        verify(gameBoard).put(eq("c1"), argThat(new ArgumentMatcher<Piece>() {
            @Override
            public boolean matches(Object argument) {
                return (argument instanceof King) && ((King) argument).getColour().equals(Colour.White);
            }
        }));
        verify(gameBoard).put(eq("d1"), argThat(new ArgumentMatcher<Piece>() {
            @Override
            public boolean matches(Object argument) {
                return (argument instanceof Rook) && ((Rook) argument).getColour().equals(Colour.White);
            }
        }));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQueensideCastleFailsIfPieceExistsOnd1() {
        // setup
        pieces.put("d1", whiteRook);

        // act
        game.makeMove(new QueensideCastle(Colour.White));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQueensideCastleFailsIfPieceExistsOnc1() {
        // setup
        pieces.put("c1", whiteRook);

        // act
        game.makeMove(new QueensideCastle(Colour.White));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQueensideCastleFailsIfPieceExistsOnb1() {
        // setup
        pieces.put("b1", whiteRook);

        // act
        game.makeMove(new QueensideCastle(Colour.White));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThatKingsideCastleShouldFailIfWhiteKingHasAlreadyMoved() {
        // setup
        try {
            when(whiteKing.canMoveTo(eq("e1"), eq("f1"), eq(false), isA(Position.class))).thenReturn(true);
            game.makeMove(new BasicMove(Colour.White, PieceType.KING, "f1", false, null));
        } catch(Exception e) {
            fail("No exceptions to be caught in the setup portion of the method");
        }

        // act
        game.makeMove(new KingsideCastle(Colour.White));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThatKingsideCastleShouldFailIfWhiteKingsideRookHasAlreadyMoved() {
        // setup
        try {
            when(whiteRook.canMoveTo(eq("h1"), eq("f1"), eq(false), isA(Position.class))).thenReturn(true);
            game.makeMove(new BasicMove(Colour.White, PieceType.ROOK, "f1", false, null));
        } catch(Exception e) {
            fail("No exceptions to be caught in the setup portion of the method");
        }

        // act
        game.makeMove(new KingsideCastle(Colour.White));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThatQueensideCastleShouldFailIfWhiteKingHasAlreadyMoved() {
        // setup
        try {
            when(whiteKing.canMoveTo(eq("e1"), eq("f1"), eq(false), isA(Position.class))).thenReturn(true);
            game.makeMove(new BasicMove(Colour.White, PieceType.KING, "f1", false, null));
        } catch(Exception e) {
            fail("No exceptions to be caught in the setup portion of the method");
        }

        // act
        game.makeMove(new QueensideCastle(Colour.White));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testThatQueensideCastleShouldFailIfWhiteQueensideRookHasAlreadyMoved() {
        // setup
        try {
            when(whiteRook.canMoveTo(eq("a1"), eq("f1"), eq(false), isA(Position.class))).thenReturn(true);
            game.makeMove(new BasicMove(Colour.White, PieceType.ROOK, "f1", false, null));
        } catch(Exception e) {
            fail("No exceptions to be caught in the setup portion of the method");
        }

        // act
        game.makeMove(new QueensideCastle(Colour.White));
    }
}
