package com.ragstorooks.blacktomove.chess;

import com.ragstorooks.blacktomove.chess.blocks.Colour;
import com.ragstorooks.blacktomove.chess.moves.Move;
import com.ragstorooks.blacktomove.chess.moves.MoveFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
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

    @Mock
    private MoveFactory moveFactory;
    @Mock
    private Game game;

    @InjectMocks
    private PGNParser pgnParser = new PGNParser(moveFactory) {
        @Override
        Game createNewGame() {
            return game;
        }
    };

    @Test
    public void testThatGameMetadataIsParsed() {
        // act
        pgnParser.parsePGN(simplePgn);

        // assert
        verify(game).addMeta("key1", "value1");
        verify(game).addMeta("key2", "value2");
    }

    @Test
    public void testThatSingleGameScoreIsParsed() {
        // setup
        Move move1 = mock(Move.class);
        Move move2 = mock(Move.class);
        when(moveFactory.createMove(Colour.White, "e4")).thenReturn(move1);
        when(moveFactory.createMove(Colour.Black, "e5")).thenReturn(move2);

        // act
        Game result = pgnParser.parsePGN(simplePgn);

        // assert
        verify(game).makeMove(move1);
        verify(game).makeMove(move2);
        assertThat(result, equalTo(game));
    }

    @Test(expected = PGNParseException.class)
    public void testThatMissingQuoteInMetadataValueThrowsException() {
        // setup
        simplePgn = simplePgn.replace("\"value2\"", "\"value2");

        // act
        pgnParser.parsePGN(simplePgn);
    }

    @Test(expected = PGNParseException.class)
    public void testThatMissingSpaceInMetadataThrowsException() {
        // setup
        simplePgn = simplePgn.replace("key2 \"value2\"", "key2\"value2\"");

        // act
        pgnParser.parsePGN(simplePgn);
    }

    @Test(expected = PGNParseException.class)
    public void testThatMissingMovesInPgnThrowsException() {
        // setup
        simplePgn = simplePgn.replaceAll("1. e4.*$", "");

        // act
        pgnParser.parsePGN(simplePgn);
    }

    @Test(expected = PGNParseException.class)
    public void testThatAnExceptionWhileMakingMoveThrowsParseException() {
        // setup
        Move move1 = mock(Move.class);
        when(moveFactory.createMove(Colour.White, "e4")).thenReturn(move1);
        doThrow(new IllegalArgumentException()).when(game).makeMove(move1);

        // act
        pgnParser.parsePGN(simplePgn);
    }
}