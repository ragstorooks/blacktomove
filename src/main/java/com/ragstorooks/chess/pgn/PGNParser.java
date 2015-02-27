package com.ragstorooks.chess.pgn;

import com.ragstorooks.chess.Game;
import com.ragstorooks.chess.Move;
import com.ragstorooks.chess.blocks.Board;
import com.ragstorooks.chess.blocks.Colour;
import com.ragstorooks.chess.blocks.PieceType;
import org.apache.commons.lang.StringUtils;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PGNParser {
    public Game parsePGN(String pgnText) {
        Game game = new Game();
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
        for (String moveText : movesText) {
            Move move = new Move(mover, getPieceTypeForMove(moveText), moveText.substring(moveText.length() - 2),
                    moveText.contains("x"), getSourceHintForMove(moveText));
            game.makeMove(move);

            mover = mover.equals(Colour.White) ? Colour.Black : Colour.White;
        }

        return game;
    }

    private String getSourceHintForMove(String moveText) {
        moveText = moveText.substring(0, moveText.length() - 2).replace("x", "").replaceAll("[A-Z]", "");
        return moveText.length() == 1 ? moveText : null;
    }

    private PieceType getPieceTypeForMove(String move) {
        if (move.length() == 2)
            return PieceType.PAWN;

        char pieceType = move.charAt(0);
        switch (pieceType) {
            case 'R':
                return PieceType.ROOK;
            case 'N':
                return PieceType.KNIGHT;
            case 'B':
                return PieceType.BISHOP;
            case 'Q':
                return PieceType.QUEEN;
            case 'K':
                return PieceType.KING;
            case 'P':
                return PieceType.PAWN;
            default:
                throw new IllegalArgumentException("Unknown move: " + move);
        }
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
