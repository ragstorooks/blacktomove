package com.ragstorooks.blacktomove.chess;

import com.ragstorooks.blacktomove.chess.blocks.Colour;
import com.ragstorooks.blacktomove.chess.moves.MoveFactory;
import com.ragstorooks.blacktomove.chess.moves.Move;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PGNParserTest {
    private final static String NEWLINE = System.lineSeparator();

    private String simplePgn = "[key1 \"value1\"]" + NEWLINE +
            "[key2 \"value2\"]" + NEWLINE + " " + NEWLINE +
            "1. e4 e5 1/2-1/2";
    private String multiGamePgn = simplePgn + NEWLINE + NEWLINE +
            "[2key1 \"2value1\"]" + NEWLINE +
            "[2key2 \"2value2\"]" + NEWLINE + NEWLINE +
            "1. c4 c5 2. Nc3 1/2-1/2";

    private AtomicBoolean hasBeenInvoked = new AtomicBoolean(false);

    @Mock
    private MoveFactory moveFactory;
    @Mock
    private Game game1;
    @Mock
    private Game game2;

    @InjectMocks
    private PGNParser pgnParser = new PGNParser() {
        @Override
        Game createNewGame() {
            return !hasBeenInvoked.getAndSet(true) ? game1 : game2;
        }
    };

    @Test
    public void testThatGameMetadataIsParsed() {
        // act
        pgnParser.parseSingleGamePGN(simplePgn);

        // assert
        verify(game1).addMeta("key1", "value1");
        verify(game1).addMeta("key2", "value2");
    }

    @Test
    public void testThatSingleGameScoreIsParsed() {
        // setup
        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);
        when(moveFactory.createMove(Colour.White, "e4")).thenReturn(move1);
        when(moveFactory.createMove(Colour.Black, "e5")).thenReturn(move2);

        // act
        pgnParser.parseSingleGamePGN(simplePgn);

        // assert
        verify(game1).makeMove(move1);
        verify(game1).makeMove(move2);
    }

    @Test(expected = PGNParseException.class)
    public void testThatMissingQuoteInMetadataValueThrowsException() {
        // setup
        simplePgn = simplePgn.replace("\"value2\"", "\"value2");

        // act
        pgnParser.parseSingleGamePGN(simplePgn);
    }

    @Test(expected = PGNParseException.class)
    public void testThatMissingSpaceInMetadataThrowsException() {
        // setup
        simplePgn = simplePgn.replace("key2 \"value2\"", "key2\"value2\"");

        // act
        pgnParser.parseSingleGamePGN(simplePgn);
    }

    @Test(expected = PGNParseException.class)
    public void testThatMissingMovesInPgnThrowsException() {
        // setup
        simplePgn = simplePgn.replaceAll("1. e4.*$", "");

        // act
        pgnParser.parseSingleGamePGN(simplePgn);
    }

    @Test(expected = PGNParseException.class)
    public void testThatAnExceptionWhileMakingMoveThrowsParseException() {
        // setup
        Move move1 = mock(Move.class);
        when(moveFactory.createMove(Colour.White, "e4")).thenReturn(move1);
        doThrow(new IllegalArgumentException()).when(game1).makeMove(move1);

        // act
        pgnParser.parseSingleGamePGN(simplePgn);
    }

    @Test(expected = PGNParseException.class)
    public void testThatAnIOExceptionWhileReadingFileThrowsParseException() {
        // setup
        File file = mock(File.class);

        // act
        pgnParser.parsePGN(file);
    }

    @Test
    public void testThatGameMetadataIsParsedForMultipleGames() {
        // act
        pgnParser.parseMultiGamePGN(multiGamePgn);

        // assert
        verify(game1).addMeta("key1", "value1");
        verify(game1).addMeta("key2", "value2");
        verify(game2).addMeta("2key1", "2value1");
        verify(game2).addMeta("2key2", "2value2");
    }

    @Test
    public void testThatMultiGameScoreIsParsed() {
        // setup
        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);
        Move move3 = mock(Move.class);
        Move move4 = mock(Move.class);
        Move move5 = mock(Move.class);
        when(moveFactory.createMove(Colour.White, "e4")).thenReturn(move1);
        when(moveFactory.createMove(Colour.Black, "e5")).thenReturn(move2);
        when(moveFactory.createMove(Colour.White, "c4")).thenReturn(move3);
        when(moveFactory.createMove(Colour.Black, "c5")).thenReturn(move4);
        when(moveFactory.createMove(Colour.White, "Nc3")).thenReturn(move5);

        // act
        pgnParser.parseMultiGamePGN(multiGamePgn);

        // assert
        verify(game1).makeMove(move1);
        verify(game1).makeMove(move2);
        verify(game2).makeMove(move3);
        verify(game2).makeMove(move4);
        verify(game2).makeMove(move5);
    }
}