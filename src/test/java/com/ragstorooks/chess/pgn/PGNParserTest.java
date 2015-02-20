package com.ragstorooks.chess.pgn;

import com.ragstorooks.chess.Game;
import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class PGNParserTest {
    private PGNParser pgnParser = new PGNParser();

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
        game.add("Event", "4th Zurich CC Classical").add("Site", "Zurich SUI").add("Date", "2015.02.17").add("Round",
                "4").add("White", "Anand, V.").add("Black", "Nakamura, Hi").add("Result", "1-0").add("ECO", "D37")
                .add("WhiteElo", "2797").add("BlackElo", "2776").add("PlyCount", "81").add("EventDate", "2015.02.14");
        return game.setMoves("1. d4 d5 2. c4 e6 3. Nc3 Be7 4. Nf3 Nf6 5. Bf4 O-O 6. e3 Nbd7 7. c5 Nh5 8. Bd3 Nxf4 9. " +
                "exf4 b6 10. b4 a5 11. a3 c6 12. O-O Qc7 13. g3 Ba6 14. Re1 Bf6 15. Kg2 Bxd3 16. Qxd3 Rfb8 17. h4 Qa7" +
                " 18. Ne2 g6 19. Rab1 axb4 20. axb4 Qa2 21. Rec1 bxc5 22. bxc5 h5 23. Ne5 Nxe5 24. fxe5 Bg7 25. Rb6 " +
                "Rc8 26. Nc3 Qa7 27. Rcb1 Qd7 28. R1b4 Bh6 29. Na4 Qd8 30. Ra6 Kg7 31. Rb7 Rxa6 32. Qxa6 g5 33. Qe2 " +
                "g4 34. Qa6 Qg8 35. Nb6 Rf8 36. Nd7 Qh7 37. Nxf8 Qe4+ 38. Kh2 Kxf8 39. Rb8+ Kg7 40. Qc8 Kg6 41. Qh8 " +
                "1-0");
    }
}