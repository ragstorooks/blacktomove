package com.ragstorooks.blacktomove.chess;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class PGNParserIntegrationTest {
    private PGNParser pgnParser;

    @Before
    public void setup() {
        Injector injector = Guice.createInjector();
        pgnParser = injector.getInstance(PGNParser.class);
    }

    @Test
    public void parseSingleGameInPgn() throws IOException {
        // setup
        Map<String, String> expectedMetadata = createMetadataForAnandNakamura();
        String finalFen = "1R5Q/5p2/2p1p1kb/2PpP2p/3Pq1pP/6P1/5P1K/8";

        // act
        Game game = pgnParser.parsePGN(FileUtils.readFileToString(new File
                ("src/integration/resources/anand-nakamura.pgn"), Charset.forName("UTF-8")));

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
