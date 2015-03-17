package com.ragstorooks.chess.blocks;

public class Queen extends Piece {
    public Queen(Colour colour) {
        super(PieceType.QUEEN, colour);
    }

    @Override
    protected boolean canMoveTo(PieceMoveDetails pieceMoveDetails, Position position) {
        return false;
    }
}
