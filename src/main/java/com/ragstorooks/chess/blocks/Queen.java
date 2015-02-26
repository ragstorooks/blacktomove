package com.ragstorooks.chess.blocks;

public class Queen extends Piece {
    public Queen(Colour colour, String square) {
        super(PieceType.QUEEN, colour);
    }

    @Override
    public boolean canMoveTo(String originSquare, String destinationSquare, boolean isCapture) {
        return false;
    }
}
