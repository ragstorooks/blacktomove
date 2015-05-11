package com.ragstorooks.chess;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class PGNParserIntegrationTest {
    private PGNParser pgnParser = new PGNParser();

    @Test
    public void parseSingleGameInPgn() throws IOException {
        // setup
        Map<String, String> expectedMetadata = createMetadataForAnandNakamura();
        String finalFen = "1R5Q/5p2/2p1p1kb/2PpP2p/3Pq1pP/6P1/5P1K/8";

        // act
        List<Game> games = pgnParser.parsePGN(new File("src/integration/resources/anand-nakamura.pgn"));

        // assert
        assertThat(games.size(), equalTo(1));
        Game game = games.get(0);
        assertThat(game.getMetadata(), equalTo(expectedMetadata));
        assertThat(game.getCurrentBoardPosition(), equalTo(finalFen));
    }

    @Test
    public void parseMultiGameInPgn() throws IOException {
        // setup

        // act
        List<Game> games = pgnParser.parsePGN(new File("src/integration/resources/shamkir.pgn"));

        // verify number of games and 3 games at random (games 10, 20 and 30 in the file)
        assertThat(games.size(), equalTo(34));
        assertThatMetadataAndFinalPositionAreCorrectForAdamsCaruana(games.get(9));
        assertThatMetadataAndFinalPositionAreCorrectForAdamsMVL(games.get(19));
        assertThatMetadataAndFinalPositionAreCorrectForSoCarlsen(games.get(29));
    }

    private void assertThatMetadataAndFinalPositionAreCorrectForSoCarlsen(Game game) {
        Map<String, String> expectedMetadata = new HashMap<>();
        expectedMetadata.put("Event", "Vugar Gashimov Mem 2015");
        expectedMetadata.put("Site", "Shamkir AZE");
        expectedMetadata.put("Date", "2015.04.25");
        expectedMetadata.put("Round", "8");
        expectedMetadata.put("White", "So, W.");
        expectedMetadata.put("Black", "Carlsen, M.");
        expectedMetadata.put("Result", "1/2-1/2");
        expectedMetadata.put("ECO", "A29");
        expectedMetadata.put("WhiteElo", "2788");
        expectedMetadata.put("BlackElo", "2863");
        expectedMetadata.put("PlyCount", "81");
        expectedMetadata.put("EventDate", "2015.04.17");
        expectedMetadata.put("SourceDate", "2015.02.07");

        assertThat(game.getMetadata(), equalTo(expectedMetadata));
        assertThat(game.getCurrentBoardPosition(), equalTo("6k1/2rp1pp1/7p/4P3/6K1/6P1/3R1P1P/8"));
    }

    private void assertThatMetadataAndFinalPositionAreCorrectForAdamsMVL(Game game) {
        Map<String, String> expectedMetadata = new HashMap<>();
        expectedMetadata.put("Event", "Vugar Gashimov Mem 2015");
        expectedMetadata.put("Site", "Shamkir AZE");
        expectedMetadata.put("Date", "2015.04.23");
        expectedMetadata.put("Round", "6");
        expectedMetadata.put("White", "Adams, Mi");
        expectedMetadata.put("Black", "Vachier Lagrave, M.");
        expectedMetadata.put("Result", "1/2-1/2");
        expectedMetadata.put("ECO", "A37");
        expectedMetadata.put("WhiteElo", "2745");
        expectedMetadata.put("BlackElo", "2765");
        expectedMetadata.put("PlyCount", "117");
        expectedMetadata.put("EventDate", "2015.04.17");
        expectedMetadata.put("SourceDate", "2015.02.07");

        assertThat(game.getMetadata(), equalTo(expectedMetadata));
        assertThat(game.getCurrentBoardPosition(), equalTo("8/b3k3/P3p3/7B/3PK3/8/8/8"));
    }

    private void assertThatMetadataAndFinalPositionAreCorrectForAdamsCaruana(Game game) {
        Map<String, String> expectedMetadata = new HashMap<>();
        expectedMetadata.put("Event", "Vugar Gashimov Mem 2015");
        expectedMetadata.put("Site", "Shamkir AZE");
        expectedMetadata.put("Date", "2015.04.18");
        expectedMetadata.put("Round", "2");
        expectedMetadata.put("White", "Adams, Mi");
        expectedMetadata.put("Black", "Caruana, F.");
        expectedMetadata.put("Result", "1/2-1/2");
        expectedMetadata.put("ECO", "C65");
        expectedMetadata.put("WhiteElo", "2745");
        expectedMetadata.put("BlackElo", "2802");
        expectedMetadata.put("PlyCount", "51");
        expectedMetadata.put("EventDate", "2015.04.17");
        expectedMetadata.put("SourceDate", "2015.02.07");

        assertThat(game.getMetadata(), equalTo(expectedMetadata));
        assertThat(game.getCurrentBoardPosition(), equalTo("5rk1/1pp2ppp/4b3/2p5/4P3/1P1PR2P/r1P2RPN/6K1"));
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
