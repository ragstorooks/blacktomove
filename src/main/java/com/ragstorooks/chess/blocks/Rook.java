package com.ragstorooks.chess.blocks;

public class Rook extends Piece {
    public Rook(Colour colour) {
        super(PieceType.ROOK, colour);
    }

    @Override
    protected boolean canMoveTo(int originRank, char originFile, int destinationRank, int destinationFile, int
            numberOfMovingRanks, int numberOfMovingFiles, boolean isCapture, Position position) {
        return false;
    }
}
