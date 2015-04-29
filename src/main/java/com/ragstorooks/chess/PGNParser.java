package com.ragstorooks.chess;

import com.ragstorooks.chess.blocks.Colour;
import com.ragstorooks.chess.moves.MoveFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PGNParser {
    private static final Logger logger = LoggerFactory.getLogger(PGNParser.class);

    private final List<String> results = Arrays.asList("1-0", "0-1", "1/2-1/2");

    private MoveFactory moveFactory;

    public PGNParser() {
        this(new MoveFactory());
    }

    public PGNParser(MoveFactory moveFactory) {
        this.moveFactory = moveFactory;
    }

    public Game parsePGN(File pgnFile) {
        try {
            return parsePGN(FileUtils.readFileToString(pgnFile, Charset.forName("UTF-8")));
        } catch (IOException e) {
            logger.error("Unable to read file to parse pgn", e);
            throw new PGNParseException(e);
        }
    }

    public Game parsePGN(String pgnText) {
        logger.info("Parsing PGN, view debug logs for actual pgn");
        logger.debug("Parsing PGN: {}", pgnText);

        Game game = createNewGame();
        String[] pgnLines = pgnText.split("[\\r\\n]+");

        StringBuilder sb = new StringBuilder();
        for (String line : pgnLines) {
            if (StringUtils.isBlank(line))
                continue;

            if (line.startsWith("[")) {
                Entry<String, String> metadataEntry = parseMetadataLine(line);
                game.addMeta(metadataEntry.getKey(), metadataEntry.getValue());
            } else
                sb.append(line);
        }

        String pgn = sb.toString();
        if (StringUtils.isBlank(pgn))
            throw new PGNParseException("Could not find game moves in PGN: " + pgnText);

        Colour mover = Colour.White;
        String[] movesText = pgn.split("[0-9]+\\.");
        for (String eachMove : movesText) {
            if (StringUtils.isBlank(eachMove))
                continue;

            String[] halfMoves = eachMove.split(" ");
            for (String halfMove : halfMoves) {
                if (StringUtils.isBlank(halfMove))
                    continue;
                if (results.contains(halfMove))
                    break;

                try {
                    game.makeMove(moveFactory.createMove(mover, halfMove));
                } catch (IllegalArgumentException e) {
                    logger.error("Error making move: " + halfMove, e);
                    throw new PGNParseException(e);
                }
                mover = flipMover(mover);
            }
        }

        return game;
    }

    Game createNewGame() {
        return new Game();
    }

    private Colour flipMover(Colour mover) {
        return mover.equals(Colour.White) ? Colour.Black : Colour.White;
    }

    private Entry<String, String> parseMetadataLine(String line) {
        Pattern pattern = Pattern.compile("\\[(.*) \"(.*)\"]");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return new SimpleEntry<>(matcher.group(1), matcher.group(2));
        }

        throw new PGNParseException("Unable to parse line: " + line);
    }
}
