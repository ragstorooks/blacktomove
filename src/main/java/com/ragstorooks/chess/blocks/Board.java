package com.ragstorooks.chess.blocks;

import java.util.*;
import java.util.Map.Entry;

public class Board {
    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final String FEN_NOT_VALID = "FEN not valid: ";

    private static final String[] RANKS = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};
    private static final String[] FILES = new String[]{"a", "b", "c", "d", "e", "f", "g", "h"};

    private SortedMap<String, Piece> board = new TreeMap<String, Piece>(new Comparator<String>() {
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
        for (String r : RANKS) {
            for (String f : FILES) {
                board.put(f + r, null);
            }
        }
    }

    public Board(String fenPosition) {
        this();

        String[] rows = fenPosition.split("/");
        if (rows.length != RANKS.length)
            throw new IllegalArgumentException(FEN_NOT_VALID + fenPosition);

        for (int i = 0; i < rows.length; i++) {
            for (int j = 0, fileIndex = 0; j < rows[i].length(); j++, fileIndex++) {
                char c = rows[i].charAt(j);
                Colour colour = Character.isUpperCase(c) ? Colour.White : Colour.Black;
                String square = getAlgebraicNotation(fileIndex, RANKS.length - (i + 1));
                Piece piece = null;

                switch (c) {
                    case 'R':
                    case 'r':
                        piece = new Rook(colour, square);
                        break;
                    case 'N':
                    case 'n':
                        piece = new Knight(colour, square);
                        break;
                    case 'B':
                    case 'b':
                        piece = new Bishop(colour, square);
                        break;
                    case 'Q':
                    case 'q':
                        piece = new Queen(colour, square);
                        break;
                    case 'K':
                    case 'k':
                        piece = new King(colour, square);
                        break;
                    case 'P':
                    case 'p':
                        piece = new Pawn(colour);
                        break;
                    default:
                        if (!Character.isDigit(c))
                            throw new IllegalArgumentException(FEN_NOT_VALID + fenPosition);

                        int numberOfSquaresToSkip = Integer.valueOf("" + c);
                        fileIndex += numberOfSquaresToSkip - 1;
                }
                board.put(square, piece);
            }
        }
    }

    private String getAlgebraicNotation(int file, int rank) {
        return FILES[file] + RANKS[rank];
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
        Map<String, Piece> candidates = new HashMap<String, Piece>();
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
