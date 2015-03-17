package com.ragstorooks.chess.blocks;

public class King extends Piece {
    private static String WHITE_NOTATION = "K";
    private static String BLACK_NOTATION = "k";

    public King(Colour colour) {
        super(PieceType.KING, colour);
    }

    @Override
    protected boolean canMoveTo(int originRank, char originFile, int destinationRank, int destinationFile, int
            numberOfMovingRanks, int numberOfMovingFiles, boolean isCapture, Position position) {
        return false;
    }


}
