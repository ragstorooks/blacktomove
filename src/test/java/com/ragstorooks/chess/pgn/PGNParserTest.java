package com.ragstorooks.chess.pgn;

import com.ragstorooks.chess.Game;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class PGNParserTest {
    private PGNParser pgnParser = new PGNParser();

    //    @Ignore("Functionality not complete yet")
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

    @Test(expected = PGNParseException.class)
    public void throwExceptionIfMetadataIsImproperlyFormatted() throws IOException {
        // setup
        String pgnText = FileUtils.readFileToString(new File("src/test/resources/anand-nakamura-with-bad-metadata" +
                ".pgn"));

        // act
        pgnParser.parsePGN(pgnText);
    }

    @Test(expected = PGNParseException.class)
    public void throwExceptionIfMovesAreMissing() throws IOException {
        // setup
        String pgnText = FileUtils.readFileToString(new File("src/test/resources/anand-nakamura-without-moves.pgn"));

        // act
        pgnParser.parsePGN(pgnText);
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