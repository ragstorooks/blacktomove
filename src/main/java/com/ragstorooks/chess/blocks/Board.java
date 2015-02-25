package com.ragstorooks.chess.blocks;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final int NUMBER_OF_RANKS_AND_FILES = 8;
    private static final String FILES = "abcdefgh";
    private static final String FEN_NOT_VALID = "FEN not valid: ";

    private Piece[][] piecesOnTheBoard = new Piece[NUMBER_OF_RANKS_AND_FILES][NUMBER_OF_RANKS_AND_FILES];

    public Board(String fenPosition) {
        String[] rows = fenPosition.split("/");
        if (rows.length != NUMBER_OF_RANKS_AND_FILES)
            throw new IllegalArgumentException(FEN_NOT_VALID + fenPosition);

        for (int i = 0; i < NUMBER_OF_RANKS_AND_FILES; i++) {
            int boardRankIndex = NUMBER_OF_RANKS_AND_FILES - i;
            for (int j = 0, fileIndex = 0; j < rows[i].length(); j++, fileIndex++) {
                char c = rows[i].charAt(j);
                Colour colour = Character.isUpperCase(c)? Colour.White : Colour.Black;
                String square = getAlgebraicNotation(j, boardRankIndex);

                switch (c) {
                    case 'R':
                    case 'r':
                        piecesOnTheBoard[boardRankIndex-1][fileIndex] = new Rook(colour, square);
                        break;
                    case 'N':
                    case 'n':
                        piecesOnTheBoard[boardRankIndex-1][fileIndex] = new Knight(colour, square);
                        break;
                    case 'B':
                    case 'b':
                        piecesOnTheBoard[boardRankIndex-1][fileIndex] = new Bishop(colour, square);
                        break;
                    case 'Q':
                    case 'q':
                        piecesOnTheBoard[boardRankIndex-1][fileIndex] = new Queen(colour, square);
                        break;
                    case 'K':
                    case 'k':
                        piecesOnTheBoard[boardRankIndex-1][fileIndex] = new King(colour, square);
                        break;
                    case 'P':
                    case 'p':
                        piecesOnTheBoard[boardRankIndex-1][fileIndex] = new Pawn(colour, square);
                        break;
                    default:
                        if (!Character.isDigit(c))
                            throw new IllegalArgumentException(FEN_NOT_VALID + fenPosition);

                        int numberOfSquaresToSkip = Integer.valueOf("" + c);
                        fileIndex += numberOfSquaresToSkip - 1;
                }
            }
        }
    }

    private String getAlgebraicNotation(int zeroBasedFile, int zeroBasedRank) {
        return String.format("%c%d", FILES.charAt(zeroBasedFile), zeroBasedRank);
    }

    public Board makeMove(Colour movingSide, String move) {
        String destinationSquare = move.substring(move.length() - 2);
        boolean isCapture = move.contains("x");
        PieceType pieceType = getPieceTypeForMove(move);

        List<Piece> candidatePieces = getPiecesOfType(movingSide, pieceType);
        for (Piece candidate : candidatePieces) {
            if (candidate.canMoveTo(destinationSquare, isCapture)) {
                movePieceToSquare(candidate, destinationSquare);
                break;
            }
        }

        return this;
    }

    private void movePieceToSquare(Piece piece, String destinationSquare) {
        String originSquare = piece.getSquare();
        piecesOnTheBoard[rank(originSquare)][file(originSquare)] = null;

        // TODO: Have the piece move itself!
        piece.setSquare(destinationSquare);
        piecesOnTheBoard[rank(destinationSquare)][file(destinationSquare)] = piece;
    }

    private int file(String square) {
        return FILES.indexOf(square.charAt(0));
    }

    private int rank(String square) {
        return Integer.parseInt(square.substring(1)) - 1;
    }

    private List<Piece> getPiecesOfType(Colour movingSide, PieceType pieceType) {
        List<Piece> pieces = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_RANKS_AND_FILES; i++)
            for (int j = 0; j < NUMBER_OF_RANKS_AND_FILES; j++)
                if (piecesOnTheBoard[i][j] != null && piecesOnTheBoard[i][j].getColour().equals(movingSide) &&
                        piecesOnTheBoard[i][j].getPieceType().equals(pieceType))
                    pieces.add(piecesOnTheBoard[i][j]);

        return pieces;
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
        for (int i = NUMBER_OF_RANKS_AND_FILES - 1; i >= 0; i--) {
            for (int j = 0; j < NUMBER_OF_RANKS_AND_FILES; j++) {
                if (piecesOnTheBoard[i][j] == null)
                    boardPosition.append(' ');
                else
                    boardPosition.append(piecesOnTheBoard[i][j]);
            }
            boardPosition.append(NEW_LINE);
        }
        return boardPosition.toString();
    }
}
