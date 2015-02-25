package com.ragstorooks.chess.blocks;

public class Bishop extends Piece {
    public Bishop(Colour colour, String square) {
        super(PieceType.BISHOP, colour, square);
    }

    @Override
    public boolean canMoveTo(String destinationSquare, boolean isCapture) {
        return false;
    }
}
