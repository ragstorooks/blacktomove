package com.ragstorooks.chess.blocks;

public class Knight extends Piece {
    public Knight(Colour colour, String square) {
        super(PieceType.KNIGHT, colour);
    }

    @Override
    public boolean canMoveTo(String originSquare, String destinationSquare, boolean isCapture) {
        return false;
    }
}
