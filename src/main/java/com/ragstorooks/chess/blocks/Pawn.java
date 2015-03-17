package com.ragstorooks.chess.blocks;

public class Pawn extends Piece {
    public Pawn(Colour colour) {
        super(PieceType.PAWN, colour);
    }

    @Override
    protected boolean canMoveTo(int originRank, char originFile, int destinationRank, int destinationFile, int
            numberOfMovingRanks, int numberOfMovingFiles, boolean isCapture, Position position) {
        if (isCapture) {
            return numberOfMovingFiles == 1 && numberOfMovingRanks == 1;
        }

        if (numberOfMovingFiles != 0 || (numberOfMovingRanks != 1 && numberOfMovingRanks != 2))
            return false;

        if (numberOfMovingRanks == 2) {
            if ((Colour.White.equals(getColour()) && originRank != 2) || (Colour.Black.equals(getColour()) &&
                    originRank != 7))
                return false;
            if (position.get(String.format("%c%d", originFile, ((originRank + destinationRank) / 2))) != null)
                return false;
        }

        return true;
    }

    @Override
    protected int getNumberOfMovingRanks(int originRank, int destinationRank) {
        return Colour.White.equals(getColour()) ? destinationRank - originRank : originRank - destinationRank;
    }
}
