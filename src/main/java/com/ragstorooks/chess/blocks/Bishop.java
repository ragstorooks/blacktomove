package com.ragstorooks.chess.blocks;

public class Bishop extends Piece {
    public Bishop(Colour colour, String square) {
        super(PieceType.BISHOP, colour);
    }

    @Override
    public boolean canMoveTo(String originSquare, String destinationSquare, boolean isCapture) {
        return false;
    }
}
