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

    public abstract boolean canMoveTo(String originSquare, String destinationSquare, boolean isCapture, Square square);
}
