package com.ragstorooks.chess.moves;

import com.ragstorooks.chess.blocks.Colour;
import com.ragstorooks.chess.pieces.PieceType;

public class MoveFactory {
    public Move createMove(Colour mover, String move) {
        return new BasicMove(mover, getPieceTypeForMove(move), move.substring(move.length() - 2),
                move.contains("x"), getSourceHintForMove(move));
    }

    private String getSourceHintForMove(String move) {
        move = move.substring(0, move.length() - 2).replace("x", "").replaceAll("[A-Z]", "");
        return move.length() == 1 ? move : null;
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
            default:
                return PieceType.PAWN;
        }
    }
}
