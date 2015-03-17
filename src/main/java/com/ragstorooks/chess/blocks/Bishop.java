package com.ragstorooks.chess.blocks;

public class Bishop extends Piece {
    public Bishop(Colour colour) {
        super(PieceType.BISHOP, colour);
    }

    @Override
    protected boolean canMoveTo(int originRank, char originFile, int destinationRank, int destinationFile, int
            numberOfMovingRanks, int numberOfMovingFiles, boolean isCapture, Position position) {
        return false;
    }
}
