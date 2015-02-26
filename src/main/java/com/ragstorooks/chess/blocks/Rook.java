package com.ragstorooks.chess.blocks;

public class Rook extends Piece {
    public Rook(Colour colour, String square) {
        super(PieceType.ROOK, colour);
    }

    @Override
    public boolean canMoveTo(String originSquare, String destinationSquare, boolean isCapture) {
        return false;
    }
}
