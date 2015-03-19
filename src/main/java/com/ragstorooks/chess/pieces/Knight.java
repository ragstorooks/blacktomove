package com.ragstorooks.chess.pieces;

import com.ragstorooks.chess.blocks.Colour;
import com.ragstorooks.chess.blocks.Position;

public class Knight extends AbstractPiece {
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
