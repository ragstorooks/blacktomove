package com.ragstorooks.blacktomove.chess.moves;

import com.ragstorooks.blacktomove.chess.blocks.Colour;
import com.ragstorooks.blacktomove.chess.pieces.PieceType;

public class MoveFactory {
    public Move createMove(Colour mover, String move) {
        move = move.replaceAll("[\\+#]", "");

        if ("O-O".equals(move))
            return new KingsideCastle(mover);

        if ("O-O-O".equals(move))
            return new QueensideCastle(mover);

        char lastCharacter = move.charAt(move.length() - 1);
        if (Character.isDigit(lastCharacter))
            return new BasicMove(mover, getPieceTypeForMove(move), move.substring(move.length() - 2),
                    move.contains("x"), getSourceHintForMove(move));

        move = move.substring(0, move.length() - 1);
        return new Promotion(mover, getPieceType(lastCharacter), move.substring(move.length() - 2), move.contains("x"),
                getSourceHintForMove(move));
    }

    private String getSourceHintForMove(String move) {
        move = move.substring(0, move.length() - 2).replace("x", "").replaceAll("[A-Z]", "");
        return move.length() == 1 ? move : null;
    }

    private PieceType getPieceTypeForMove(String move) {
        if (move.length() == 2)
            return PieceType.PAWN;

        return getPieceType(move.charAt(0));
    }

    private PieceType getPieceType(char pieceType) {
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
            default:
                return PieceType.PAWN;
        }
    }
}
