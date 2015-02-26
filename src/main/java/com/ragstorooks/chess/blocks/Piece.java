package com.ragstorooks.chess.blocks;

public abstract class Piece {
    private PieceType pieceType;
    private String whiteNotation;
    private String blackNotation;
    private Colour colour;

    protected Piece(PieceType pieceType, Colour colour) {
        this.pieceType = pieceType;
        this.whiteNotation = pieceType.getNotation().toUpperCase();
        this.blackNotation = pieceType.getNotation().toLowerCase();
        this.colour = colour;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public Colour getColour() {
        return colour;
    }

    @Override
    public String toString() {
        return Colour.White.equals(getColour())? whiteNotation : blackNotation;
    }

    public abstract boolean canMoveTo(String originSquare, String destinationSquare, boolean isCapture);
}
