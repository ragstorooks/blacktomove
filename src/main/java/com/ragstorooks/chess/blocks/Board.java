package com.ragstorooks.chess.blocks;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.TreeMap;

public class Board {
    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final String FEN_NOT_VALID = "FEN not valid: ";
    private static final int BOARD_SIZE = 8;

    private static final String[] FILES = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};

    private SortedMap<String, Piece> board = new TreeMap<>(new Comparator<String>() {
        @Override
        public int compare(String s1, String s2) {
            if (s1.length() != s2.length() || s1.length() != 2)
                throw new IllegalArgumentException(s1 + " and " + s2 + " need to be correctly formatted algebraic " +
                        "coordinates");

            char s1Rank = s1.charAt(1);
            char s2Rank = s2.charAt(1);
            if (s1Rank == s2Rank)
                return s1.charAt(0) - s2.charAt(0);

            return s2Rank - s1Rank;
        }
    });

    public Board() {
        for (int r = 1; r <= BOARD_SIZE; r++) {
            for (String f : FILES) {
                board.put(f + r, null);
            }
        }
    }

    public Board(String fenPosition) {
        this();

        String[] rows = fenPosition.split("/");
        if (rows.length != BOARD_SIZE)
            throw new IllegalArgumentException(FEN_NOT_VALID + fenPosition);

        for (int i = 0, rank = BOARD_SIZE - i; i < rows.length; i++, rank--) {
            String row = reformatFENRowByPopulatingEmptySquares(rows[i]);
            for (int fileIndex = 0; fileIndex < row.length(); fileIndex++) {
                char c = row.charAt(fileIndex);
                Colour colour = Character.isUpperCase(c) ? Colour.White : Colour.Black;
                String square = FILES[fileIndex] + rank;

                switch (c) {
                    case 'R':
                    case 'r':
                        board.put(square, new Rook(colour));
                        break;
                    case 'N':
                    case 'n':
                        board.put(square, new Knight(colour));
                        break;
                    case 'B':
                    case 'b':
                        board.put(square, new Bishop(colour));
                        break;
                    case 'Q':
                    case 'q':
                        board.put(square, new Queen(colour));
                        break;
                    case 'K':
                    case 'k':
                        board.put(square, new King(colour));
                        break;
                    case 'P':
                    case 'p':
                        board.put(square, new Pawn(colour));
                        break;
                    case ' ':
                        break;
                    default:
                        throw new IllegalArgumentException(FEN_NOT_VALID + fenPosition);
                }
            }
        }
    }

    private String reformatFENRowByPopulatingEmptySquares(String row) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < row.length(); i++) {
            char c = row.charAt(i);
            if (Character.isDigit(c)) {
                int numberOfEmptySquares = Integer.valueOf("" + c);
                for (int j = 0; j < numberOfEmptySquares; j++)
                    result.append(' ');
            } else
                result.append(c);
        }
        return result.toString();
    }

    public Board makeMove(Colour movingSide, String move) {
        String destinationSquare = move.substring(move.length() - 2);
        boolean isCapture = move.contains("x");
        PieceType pieceType = getPieceTypeForMove(move);

        Map<String, Piece> candidatePieces = getPiecesOfType(movingSide, pieceType);
        for (Entry<String, Piece> candidate : candidatePieces.entrySet()) {
            if (candidate.getValue().canMoveTo(candidate.getKey(), destinationSquare, isCapture)) {
                movePieceToSquare(candidate, destinationSquare);
                break;
            }
        }

        return this;
    }

    private void movePieceToSquare(Entry<String, Piece> piece, String destinationSquare) {
        String originSquare = piece.getKey();
        board.put(originSquare, null);
        board.put(destinationSquare, piece.getValue());
    }

    private Map<String, Piece> getPiecesOfType(Colour movingSide, PieceType pieceType) {
        Map<String, Piece> candidates = new HashMap<>();
        board.entrySet().stream().filter(square -> square.getValue() != null && square.getValue().getColour().equals
                (movingSide) && square.getValue().getPieceType().equals(pieceType)).forEach(square -> candidates.put
                (square.getKey(), square.getValue()));

        return candidates;
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
                return null;
        }
    }

    @Override
    public String toString() {
        StringBuilder boardPosition = new StringBuilder();
        board.entrySet().stream().forEach(entry -> boardPosition.append(entry.getValue() == null ? ' ' : entry
                .getValue()).append(entry.getKey().startsWith(FILES[FILES.length - 1]) ? NEW_LINE : ""));
        return boardPosition.toString();
    }
}
