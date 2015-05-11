package com.ragstorooks.blacktomove.chess.pieces;

import com.ragstorooks.blacktomove.chess.blocks.Colour;
import com.ragstorooks.blacktomove.chess.blocks.Position;

public class Knight extends AbstractPiece {
    public Knight(Colour colour) {
        super(PieceType.KNIGHT, colour);
    }

    @Override
    protected boolean canMoveTo(PieceMoveDetails pieceMoveDetails, Position position) {
        int numberOfMovingFiles = pieceMoveDetails.getNumberOfMovingFiles();
        int numberOfMovingRanks = pieceMoveDetails.getNumberOfMovingRanks();
        if (numberOfMovingFiles > 0 && numberOfMovingRanks > 0 && (numberOfMovingFiles + numberOfMovingRanks == 3))
            return true;

        return false;
    }
}
