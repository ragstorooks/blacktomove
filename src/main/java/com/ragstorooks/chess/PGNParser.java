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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PGNParser {
    private static final Logger logger = LoggerFactory.getLogger(PGNParser.class);

    private static final String MULTI_GAME_SPLITTER = "^\\s*$[\\r\\n]+^\\[";
    private static final Pattern MULTI_GAME_SPLITTER_PATTERN = Pattern.compile(MULTI_GAME_SPLITTER, Pattern.MULTILINE);
    private static final String MOVE_SPLITTER = "[0-9]+\\.";
    private static final Pattern MOVE_SPLITTER_PATTERN = Pattern.compile(MOVE_SPLITTER, Pattern.MULTILINE);

    private final List<String> results = Arrays.asList("1-0", "0-1", "1/2-1/2");
    private MoveFactory moveFactory;

    public PGNParser() {
        this(new MoveFactory());
    }

    public PGNParser(MoveFactory moveFactory) {
        this.moveFactory = moveFactory;
    }

    public List<Game> parsePGN(File pgnFile) {
        logger.info("Parsing PGN file {}, view debug logs for actual pgn", pgnFile);
        try {
            return parseMultiGamePGN(FileUtils.readFileToString(pgnFile, Charset.forName("UTF-8")));
        } catch (IOException e) {
            logger.error("Unable to read file to parse pgn", e);
            throw new PGNParseException(e);
        }
    }

    public List<Game> parseMultiGamePGN(String pgnText) {
        List<Game> games = new ArrayList<>();
        String[] pgnGames = MULTI_GAME_SPLITTER_PATTERN.split(pgnText);
        for (String pgnGame : pgnGames) {
            if (!pgnGame.startsWith("["))
                pgnGame = "[" + pgnGame;

            games.add(parseSingleGamePGN(pgnGame));
        }

        return games;
    }

    public Game parseSingleGamePGN(String pgnText) {
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
                sb.append(line + System.lineSeparator());
        }

        String pgn = sb.toString();
        if (StringUtils.isBlank(pgn))
            throw new PGNParseException("Could not find game moves in PGN: " + pgnText);

        Colour mover = Colour.White;
        String[] movesText = MOVE_SPLITTER_PATTERN.split(pgn);
        for (String eachMove : movesText) {
            if (StringUtils.isBlank(eachMove))
                continue;

            String[] halfMoves = eachMove.split("\\s");
            for (String halfMove : halfMoves) {
                if (StringUtils.isBlank(halfMove))
                    continue;
                if (results.contains(halfMove))
                    break;

                try {
                    logger.debug("Making move {}...", halfMove);
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
