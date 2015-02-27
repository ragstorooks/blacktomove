package com.ragstorooks.chess.blocks;

public class Knight extends Piece {
    public Knight(Colour colour) {
        super(PieceType.KNIGHT, colour);
    }

    @Override
    public boolean canMoveTo(String originSquare, String destinationSquare, boolean isCapture, Square square) {
        char destinationFile = destinationSquare.charAt(0);
        char originFile = originSquare.charAt(0);
        int destinationRank = Integer.parseInt(destinationSquare.substring(1));
        int originRank = Integer.parseInt(originSquare.substring(1));

        int numberOfMovingRanks = Math.abs((destinationRank - originRank));
        int numberOfMovingFiles = Math.abs(destinationFile - originFile);

        if (numberOfMovingFiles + numberOfMovingRanks == 3 && (square.get(destinationSquare) == null || !square.get(destinationSquare).getColour().equals(getColour())))
            return true;

        return false;
    }
}
