package com.ragstorooks.chess.pieces;

import com.ragstorooks.chess.blocks.Colour;
import com.ragstorooks.chess.blocks.Position;

public abstract class AbstractPiece implements Piece {
    private PieceType pieceType;
    private String notation;
    private Colour colour;

    protected AbstractPiece(PieceType pieceType, Colour colour) {
        this.pieceType = pieceType;
        this.colour = colour;
        this.notation = Colour.White.equals(colour) ? pieceType.getNotation().toUpperCase() : pieceType.getNotation()
                .toLowerCase();
    }

    @Override
    public PieceType getPieceType() {
        return pieceType;
    }

    @Override
    public Colour getColour() {
        return colour;
    }

    @Override
    public boolean canMoveTo(String originSquare, String destinationSquare, boolean isCapture, Position position) {
        if (originSquare.equals(destinationSquare))
            return false;

        char destinationFile = destinationSquare.charAt(0);
        char originFile = originSquare.charAt(0);
        int destinationRank = Integer.parseInt(destinationSquare.substring(1));
        int originRank = Integer.parseInt(originSquare.substring(1));

        int numberOfMovingRanks = getNumberOfMovingRanks(originRank, destinationRank);
        int numberOfMovingFiles = getNumberOfMovingFiles(originFile, destinationFile);

        if (isCaptureAndOppositionPieceNotAtDestination(isCapture, position.get(destinationSquare)) ||
                isNotCaptureAndDestinationNotEmpty(isCapture, position.get(destinationSquare)))
            return false;

        return canMoveTo(new PieceMoveDetails(originRank, originFile, destinationRank, destinationFile, numberOfMovingRanks,
                numberOfMovingFiles, isCapture), position);
    }

    protected boolean isNotCaptureAndDestinationNotEmpty(boolean isCapture, AbstractPiece pieceAtDestination) {
        return !isCapture && pieceAtDestination != null;
    }

    protected boolean isCaptureAndOppositionPieceNotAtDestination(boolean isCapture, AbstractPiece pieceAtDestination) {
        return isCapture && (pieceAtDestination == null || getColour().equals(pieceAtDestination.getColour()));
    }

    protected int getNumberOfMovingFiles(char originFile, char destinationFile) {
        return Math.abs(destinationFile - originFile);
    }

    protected int getNumberOfMovingRanks(int originRank, int destinationRank) {
        return Math.abs(destinationRank - originRank);
    }

    protected abstract boolean canMoveTo(PieceMoveDetails pieceMoveDetails, Position position);

    @Override
    public String toString() {
        return notation;
    }
}
