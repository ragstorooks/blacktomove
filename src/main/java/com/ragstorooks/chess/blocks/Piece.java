package com.ragstorooks.chess.blocks;

public abstract class Piece {
    private PieceType pieceType;
    private String whiteNotation;
    private String blackNotation;
    private Colour colour;
    private String square;

    protected Piece(PieceType pieceType, Colour colour, String square) {
        this.pieceType = pieceType;
        this.whiteNotation = pieceType.getNotation().toUpperCase();
        this.blackNotation = pieceType.getNotation().toLowerCase();
        this.colour = colour;
        this.square = square;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public Colour getColour() {
        return colour;
    }

    public String getSquare() {
        return square;
    }

    public void setSquare(String square) {
        this.square = square;
    }

    @Override
    public String toString() {
        return Colour.White.equals(getColour())? whiteNotation : blackNotation;
    }

    public abstract boolean canMoveTo(String destinationSquare, boolean isCapture);
}
