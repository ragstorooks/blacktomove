package com.ragstorooks.chess.pgn;

import com.ragstorooks.chess.Game;
import org.apache.commons.lang.StringUtils;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PGNParser {
    public Game parsePGN(String pgnText) {
        Game game = new Game();
        String[] pgnLines = pgnText.split("[\\r\\n]+");
        for (String line: pgnLines) {
            if (StringUtils.isBlank(line))
                continue;

            if (line.startsWith("[")) {
                Entry<String, String> metadataEntry = parseMetadataLine(line);
                game.add(metadataEntry.getKey(), metadataEntry.getValue());
            } else
                game.addMoves(line);
        }

        if (game.getMoves() == null)
            throw new PGNParseException("Could not find game moves in PGN: " + pgnText);

        return game;
    }

    private Entry<String, String> parseMetadataLine(String line) {
        Pattern pattern = Pattern.compile("\\[(.*) \"(.*)\"]");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return new SimpleEntry<String, String>(matcher.group(1), matcher.group(2));
        }

        throw new PGNParseException("Unable to parse line: " + line);
    }
}
