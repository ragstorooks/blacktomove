package com.ragstorooks.chess.blocks;

public class Rook extends Piece {
    public Rook(Colour colour) {
        super(PieceType.ROOK, colour);
    }

    @Override
    protected boolean canMoveTo(PieceMoveDetails pieceMoveDetails, Position position) {
        return false;
    }
}
