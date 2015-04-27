package com.ragstorooks.chess.pgn;

import com.ragstorooks.chess.Game;
import com.ragstorooks.chess.blocks.Colour;
import com.ragstorooks.chess.moves.Move;
import com.ragstorooks.chess.moves.MoveFactory;
import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    private PGNParser pgnParser = new PGNParser() {
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
        pgnParser.parsePGN(simplePgn);

        // assert
        verify(game).makeMove(move1);
        verify(game).makeMove(move2);
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

    @Ignore("needs to move to an integration test setup instead")
    @Test
    public void parseSingleGameInPgn() throws IOException {
        // setup
        Map<String, String> expectedMetadata = createMetadataForAnandNakamura();
        String finalFen = "1R5Q/5p2/2p1p1kb/2PpP2p/3Pq1pP/6P1/5P1K/8";

        String pgnText = FileUtils.readFileToString(new File("src/test/resources/anand-nakamura.pgn"));

        // act
        Game game = pgnParser.parsePGN(pgnText);

        // assert
        assertThat(game.getMetadata(), equalTo(expectedMetadata));
        assertThat(game.getCurrentBoardPosition(), equalTo(finalFen));
    }

    private Map<String, String> createMetadataForAnandNakamura() {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Event", "4th Zurich CC Classical");
        metadata.put("Site", "Zurich SUI");
        metadata.put("Date", "2015.02.17");
        metadata.put("Round", "4");
        metadata.put("White", "Anand, V.");
        metadata.put("Black", "Nakamura, Hi");
        metadata.put("Result", "1-0");
        metadata.put("ECO", "D37");
        metadata.put("WhiteElo", "2797");
        metadata.put("BlackElo", "2776");
        metadata.put("PlyCount", "81");
        metadata.put("EventDate", "2015.02.14");
        return metadata;
    }
}