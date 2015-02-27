package com.ragstorooks.chess.blocks;

public class Queen extends Piece {
    public Queen(Colour colour) {
        super(PieceType.QUEEN, colour);
    }

    @Override
    public boolean canMoveTo(String originSquare, String destinationSquare, boolean isCapture, Square square) {
        return false;
    }
}
