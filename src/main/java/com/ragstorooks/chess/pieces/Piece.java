package com.ragstorooks.chess.pieces;

import com.ragstorooks.chess.blocks.Colour;
import com.ragstorooks.chess.blocks.Position;

public interface Piece {
    Colour getColour();

    PieceType getPieceType();

    boolean canMoveTo(String originSquare, String destinationSquare, boolean isCapture, Position position);
}
