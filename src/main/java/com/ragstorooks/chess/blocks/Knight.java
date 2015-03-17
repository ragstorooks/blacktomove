package com.ragstorooks.chess.blocks;

public class Knight extends Piece {
    public Knight(Colour colour) {
        super(PieceType.KNIGHT, colour);
    }

    @Override
    protected boolean canMoveTo(PieceMoveDetails pieceMoveDetails, Position position) {
        if (pieceMoveDetails.getNumberOfMovingFiles() + pieceMoveDetails.getNumberOfMovingRanks() == 3)
            return true;

        return false;
    }
}
