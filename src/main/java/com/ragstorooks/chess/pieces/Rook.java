package com.ragstorooks.chess.pieces;

import com.ragstorooks.chess.blocks.Colour;
import com.ragstorooks.chess.blocks.Position;

public class Rook extends AbstractPiece {
    public Rook(Colour colour) {
        super(PieceType.ROOK, colour);
    }

    @Override
    protected boolean canMoveTo(PieceMoveDetails pieceMoveDetails, Position position) {
        if (pieceMoveDetails.getNumberOfMovingFiles() != 0 && pieceMoveDetails.getNumberOfMovingRanks() != 0)
            return false;

        boolean isHorizontalMove = pieceMoveDetails.getNumberOfMovingFiles() != 0;
        if (isHorizontalMove) {
            boolean isAscendingFile = pieceMoveDetails.getOriginFile() < pieceMoveDetails.getDestinationFile();
            for (int i = 1; i < pieceMoveDetails.getNumberOfMovingFiles(); i++) {
                String interveningSquare = String.format("%c%d",
                        isAscendingFile ? pieceMoveDetails.getOriginFile() + i : pieceMoveDetails.getOriginFile() - i,
                        pieceMoveDetails.getOriginRank());

                if (position.get(interveningSquare) != null) {
                    return false;
                }
            }
        } else {
            boolean isAscendingRank = pieceMoveDetails.getOriginRank() < pieceMoveDetails.getDestinationRank();
            for (int i = 1; i < pieceMoveDetails.getNumberOfMovingRanks(); i++) {
                String interveningSquare = String.format("%c%d",
                        pieceMoveDetails.getOriginFile(),
                        isAscendingRank ? pieceMoveDetails.getOriginRank() + i : pieceMoveDetails.getOriginRank() - i);

                if (position.get(interveningSquare) != null)
                    return false;
            }
        }
        return true;
    }
}
