package com.ragstorooks.chess.blocks;

public class Knight extends Piece {
    public Knight(Colour colour) {
        super(PieceType.KNIGHT, colour);
    }

    @Override
    protected boolean canMoveTo(int originRank, char originFile, int destinationRank, int destinationFile, int
            numberOfMovingRanks, int numberOfMovingFiles, boolean isCapture, Square square) {
        if (numberOfMovingFiles + numberOfMovingRanks == 3)
            return true;

        return false;
    }
}
