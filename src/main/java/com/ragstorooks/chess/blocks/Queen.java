package com.ragstorooks.chess.blocks;

public class Queen extends Piece {
    public Queen(Colour colour, String square) {
        super(PieceType.QUEEN, colour, square);
    }

    @Override
    public boolean canMoveTo(String destinationSquare, boolean isCapture) {
        return false;
    }
}
