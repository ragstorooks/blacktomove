package com.ragstorooks.chess.blocks;

public class Rook extends Piece {
    public Rook(Colour colour, String square) {
        super(PieceType.ROOK, colour, square);
    }

    @Override
    public boolean canMoveTo(String destinationSquare, boolean isCapture) {
        return false;
    }
}
