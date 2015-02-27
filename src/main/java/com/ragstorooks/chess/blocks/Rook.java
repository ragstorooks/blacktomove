package com.ragstorooks.chess.blocks;

public class Rook extends Piece {
    public Rook(Colour colour) {
        super(PieceType.ROOK, colour);
    }

    @Override
    public boolean canMoveTo(String originSquare, String destinationSquare, boolean isCapture, Square square) {
        return false;
    }
}
