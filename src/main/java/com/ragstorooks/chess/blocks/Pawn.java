package com.ragstorooks.chess.blocks;

public class Pawn extends Piece {
    public Pawn(Colour colour) {
        super(PieceType.PAWN, colour);
    }

    @Override
    public boolean canMoveTo(String originSquare, String destinationSquare, boolean isCapture, Square square) {
        char destinationFile = destinationSquare.charAt(0);
        char originFile = originSquare.charAt(0);
        int destinationRank = Integer.parseInt(destinationSquare.substring(1));
        int originRank = Integer.parseInt(originSquare.substring(1));

        int numberOfMovingRanks = Colour.White.equals(getColour()) ? destinationRank - originRank : originRank -
                destinationRank;
        int numberOfMovingFiles = Math.abs(destinationFile - originFile);
        if (isCapture) {
            Piece pieceAtDestination = square.get(destinationSquare);
            if (pieceAtDestination == null || getColour().equals(pieceAtDestination.getColour()))
                throw new IllegalArgumentException();

            return numberOfMovingFiles == 1 && numberOfMovingRanks == 1;
        }

        if (numberOfMovingFiles != 0 || (numberOfMovingRanks != 1 && numberOfMovingRanks != 2) && square.get
                (destinationSquare) == null)
            return false;

        if (numberOfMovingRanks == 2 && square.get(String.format("%c%d", originFile, ((originRank + destinationRank)
                / 2))) == null)
            return true;

        return true;
    }
}
