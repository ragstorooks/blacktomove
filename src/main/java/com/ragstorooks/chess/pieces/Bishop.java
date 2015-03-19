package com.ragstorooks.chess.pieces;

import com.ragstorooks.chess.blocks.Colour;
import com.ragstorooks.chess.blocks.Position;

public class Bishop extends AbstractPiece {
    public Bishop(Colour colour) {
        super(PieceType.BISHOP, colour);
    }

    @Override
    protected boolean canMoveTo(PieceMoveDetails pieceMoveDetails, Position position) {
        if (pieceMoveDetails.getNumberOfMovingRanks() != pieceMoveDetails.getNumberOfMovingFiles())
            return false;

        boolean isAscendingRank = pieceMoveDetails.getOriginRank() < pieceMoveDetails.getDestinationRank();
        boolean isAscendingFile = pieceMoveDetails.getOriginFile() < pieceMoveDetails.getDestinationFile();

        for (int i=1; i<pieceMoveDetails.getNumberOfMovingFiles(); i++) {
            String interveningSquare = String.format("%c%d",
                    isAscendingFile? pieceMoveDetails.getOriginFile() + i : pieceMoveDetails.getOriginFile() - i,
                    isAscendingRank? pieceMoveDetails.getOriginRank() + i : pieceMoveDetails.getOriginRank() - i);

            if (position.get(interveningSquare) != null)
                return false;
        }

        return true;
    }
}
