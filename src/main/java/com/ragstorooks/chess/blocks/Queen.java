package com.ragstorooks.chess.blocks;

public class Queen extends Piece {
    public Queen(Colour colour) {
        super(PieceType.QUEEN, colour);
    }

    @Override
    protected boolean canMoveTo(int originRank, char originFile, int destinationRank, int destinationFile, int
            numberOfMovingRanks, int numberOfMovingFiles, boolean isCapture, Position position) {
        return false;
    }
}
