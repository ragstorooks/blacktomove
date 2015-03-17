package com.ragstorooks.chess.blocks;

public class Bishop extends Piece {
    public Bishop(Colour colour) {
        super(PieceType.BISHOP, colour);
    }

    @Override
    protected boolean canMoveTo(PieceMoveDetails pieceMoveDetails, Position position) {
        return false;
    }
}
