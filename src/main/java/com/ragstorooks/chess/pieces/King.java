package com.ragstorooks.chess.pieces;

import com.ragstorooks.chess.blocks.Colour;
import com.ragstorooks.chess.blocks.Position;

public class King extends AbstractPiece {
    private Queen queen;

    public King(Colour colour) {
        super(PieceType.KING, colour);

        queen = new Queen(colour);
    }

    @Override
    protected boolean canMoveTo(PieceMoveDetails pieceMoveDetails, Position position) {
        if (pieceMoveDetails.getNumberOfMovingFiles() > 1 || pieceMoveDetails.getNumberOfMovingRanks() > 1)
            return false;

        return queen.canMoveTo(pieceMoveDetails, position);
    }
}
