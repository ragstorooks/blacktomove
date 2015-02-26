package com.ragstorooks.chess.blocks;

public class Pawn extends Piece {
    public Pawn(Colour colour) {
        super(PieceType.PAWN, colour);
    }

    @Override
    public boolean canMoveTo(String originSquare, String destinationSquare, boolean isCapture) {
        char destinationFile = destinationSquare.charAt(0);
        char originFile = originSquare.charAt(0);
        int destinationRank = Integer.parseInt(destinationSquare.substring(1));
        int originRank = Integer.parseInt(originSquare.substring(1));

        int numberOfMovingRanks = Colour.White.equals(getColour())? destinationRank - originRank : originRank - destinationRank;
        int numberOfMovingFiles = Math.abs(destinationFile - originFile);
        if (isCapture) // add check to make sure the destination square is occupied by enemy piece
            return numberOfMovingFiles == 1 && numberOfMovingRanks == 1;

        if (numberOfMovingFiles != 0 || (numberOfMovingRanks != 1 && numberOfMovingRanks != 2))
        // add check to make sure the destination square is empty
            return false;

        if (numberOfMovingRanks == 2)
            return true; // add check to make sure the square in between is empty

        return true;
    }
}
