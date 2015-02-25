package com.ragstorooks.chess.blocks;

public class Knight extends Piece {
    public Knight(Colour colour, String square) {
        super(PieceType.KNIGHT, colour, square);
    }

    @Override
    public boolean canMoveTo(String destinationSquare, boolean isCapture) {
        return false;
    }
}
