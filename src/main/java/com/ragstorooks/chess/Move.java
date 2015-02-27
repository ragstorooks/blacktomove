package com.ragstorooks.chess;

import com.ragstorooks.chess.blocks.Colour;
import com.ragstorooks.chess.blocks.PieceType;

public class Move {
    private Colour mover;
    private PieceType pieceType;
    private String destination;
    private boolean isCapture;
    private String sourceHint;

    public Move(Colour mover, PieceType pieceType, String destination, boolean isCapture, String sourceHint) {
        this.mover = mover;
        this.pieceType = pieceType;
        this.destination = destination;
        this.isCapture = isCapture;
        this.sourceHint = sourceHint;
    }

    public Colour getMover() {
        return mover;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public String getDestination() {
        return destination;
    }

    public String getSourceHint() {
        return sourceHint;
    }

    public boolean isCapture() {
        return isCapture;
    }
}
