package com.ragstorooks.chess.blocks;

public abstract class Piece {
    private PieceType pieceType;
    private String notation;
    private Colour colour;

    protected Piece(PieceType pieceType, Colour colour) {
        this.pieceType = pieceType;
        this.colour = colour;
        this.notation = Colour.White.equals(colour) ? pieceType.getNotation().toUpperCase() : pieceType.getNotation()
                .toLowerCase();
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public Colour getColour() {
        return colour;
    }

    @Override
    public String toString() {
        return notation;
    }

    public boolean canMoveTo(String originSquare, String destinationSquare, boolean isCapture, Position position) {
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

    protected boolean isNotCaptureAndDestinationNotEmpty(boolean isCapture, Piece pieceAtDestination) {
        return !isCapture && pieceAtDestination != null;
    }

    protected boolean isCaptureAndOppositionPieceNotAtDestination(boolean isCapture, Piece pieceAtDestination) {
        return isCapture && (pieceAtDestination == null || getColour().equals(pieceAtDestination.getColour()));
    }

    protected int getNumberOfMovingFiles(char originFile, char destinationFile) {
        return Math.abs(destinationFile - originFile);
    }

    protected int getNumberOfMovingRanks(int originRank, int destinationRank) {
        return Math.abs(destinationRank - originRank);
    }

    protected abstract boolean canMoveTo(PieceMoveDetails pieceMoveDetails, Position position);
}
