package com.ragstorooks.pgn;

import org.apache.commons.lang.StringUtils;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PGNParser {
    public PGN parsePGN(String pgnText) {
        PGN pgn = new PGN();
        String[] pgnLines = pgnText.split("[\\r\\n]+");
        for (String line: pgnLines) {
            if (StringUtils.isBlank(line))
                continue;

            if (line.startsWith("[")) {
                Entry<String, String> metadataEntry = parseMetadataLine(line);
                pgn.add(metadataEntry.getKey(), metadataEntry.getValue());
            } else
                pgn.setMoves(line);
        }

        if (pgn.getMoves() == null)
            throw new PGNParseException("Could not find game moves in PGN: " + pgnText);

        return pgn;
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
