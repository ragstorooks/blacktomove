package com.ragstorooks.blacktomove.chess.pieces;

public enum PieceType {
    PAWN("P"),
    KNIGHT("N"),
    BISHOP("B"),
    ROOK("R"),
    QUEEN("Q"),
    KING("K");

    private final String notation;

    PieceType(String notation) {
        this.notation = notation;
    }

    public String getNotation() {
        return notation;
    }
}
