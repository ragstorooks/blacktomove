package com.ragstorooks.chess.blocks;

public class King extends Piece {
    private static String WHITE_NOTATION = "K";
    private static String BLACK_NOTATION = "k";

    public King(Colour colour) {
        super(PieceType.KING, colour);
    }

    @Override
    protected boolean canMoveTo(PieceMoveDetails pieceMoveDetails, Position position) {
        return false;
    }


}
