package com.ragstorooks.chess.pieces;

import com.ragstorooks.chess.blocks.Colour;
import com.ragstorooks.chess.blocks.Position;

public class Pawn extends AbstractPiece {
    public Pawn(Colour colour) {
        super(PieceType.PAWN, colour);
    }

    @Override
    protected boolean canMoveTo(PieceMoveDetails pieceMoveDetails, Position position) {
        if (pieceMoveDetails.isCapture()) {
            return pieceMoveDetails.getNumberOfMovingFiles() == 1 && pieceMoveDetails.getNumberOfMovingRanks() == 1;
        }

        if (pieceMoveDetails.getNumberOfMovingFiles() != 0 || (pieceMoveDetails.getNumberOfMovingRanks() != 1 &&
                pieceMoveDetails.getNumberOfMovingRanks() != 2))
            return false;

        if (pieceMoveDetails.getNumberOfMovingRanks() == 2) {
            if ((Colour.White.equals(getColour()) && pieceMoveDetails.getOriginRank() != 2) || (Colour.Black.equals
                    (getColour()) && pieceMoveDetails.getOriginRank() != 7))
                return false;
            if (position.get(String.format("%c%d", pieceMoveDetails.getOriginFile(), ((pieceMoveDetails.getOriginRank
                    () + pieceMoveDetails.getDestinationRank()) / 2))) != null)
                return false;
        }

        if ((Colour.White.equals(getColour()) && pieceMoveDetails.getDestinationRank() <= 2) ||
                (Colour.Black.equals(getColour()) && pieceMoveDetails.getDestinationRank() >= 7))
            return false;

        return true;
    }

    @Override
    protected int getNumberOfMovingRanks(int originRank, int destinationRank) {
        return Colour.White.equals(getColour()) ? destinationRank - originRank : originRank - destinationRank;
    }
}
