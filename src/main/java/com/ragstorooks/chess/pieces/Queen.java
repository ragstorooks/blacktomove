package com.ragstorooks.chess.pieces;

import com.ragstorooks.chess.blocks.Colour;
import com.ragstorooks.chess.blocks.Position;

public class Queen extends AbstractPiece {
    private Bishop bishop;
    private Rook rook;

    public Queen(Colour colour) {
        super(PieceType.QUEEN, colour);

        bishop = new Bishop(colour);
        rook = new Rook(colour);
    }

    @Override
    protected boolean canMoveTo(PieceMoveDetails pieceMoveDetails, Position position) {
        return bishop.canMoveTo(pieceMoveDetails, position) || rook.canMoveTo(pieceMoveDetails, position);
    }
}
