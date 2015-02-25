package com.ragstorooks.chess.blocks;

public class King extends Piece {
    private static String WHITE_NOTATION = "K";
    private static String BLACK_NOTATION = "k";

    public King(Colour colour, String square) {
        super(PieceType.KING, colour, square);
    }

    @Override
    public boolean canMoveTo(String destinationSquare, boolean isCapture) {
        return false;
    }


}
