package com.ragstorooks.chess.pgn;

import com.ragstorooks.chess.Game;
import org.apache.commons.io.FileUtils;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class PGNParserTest {
    private PGNParser pgnParser = new PGNParser();

    @Ignore("Functionality not complete yet")
    @Test
    public void parseSingleGameInPgn() throws IOException {
        // setup
        String pgnText = FileUtils.readFileToString(new File("src/test/resources/anand-nakamura.pgn"));

        // act
        Game game = pgnParser.parsePGN(pgnText);

        // assert
        Game expectedGame = createExpectedGameForAnandNakamura();
        assertThat(game, equalTo(expectedGame));
    }

    @Test(expected = PGNParseException.class)
    public void throwExceptionIfMetadataIsImproperlyFormatted() throws IOException {
        // setup
        String pgnText = FileUtils.readFileToString(new File("src/test/resources/anand-nakamura-with-bad-metadata.pgn"));

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

    private Game createExpectedGameForAnandNakamura() {
        Game game = new Game();
        game.addMeta("Event", "4th Zurich CC Classical").addMeta("Site", "Zurich SUI").addMeta("Date", "2015.02.17").addMeta("Round",
                "4").addMeta("White", "Anand, V.").addMeta("Black", "Nakamura, Hi").addMeta("Result", "1-0").addMeta("ECO", "D37")
                .addMeta("WhiteElo", "2797").addMeta("BlackElo", "2776").addMeta("PlyCount", "81").addMeta("EventDate" +
                "", "2015.02.14");
        return game;
    }
}