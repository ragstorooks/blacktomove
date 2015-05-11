package com.ragstorooks.blacktomove.chess.pieces;

import com.ragstorooks.blacktomove.chess.blocks.Position;
import com.ragstorooks.blacktomove.chess.blocks.Colour;

public interface Piece {
    Colour getColour();

    PieceType getPieceType();

    boolean canMoveTo(String originSquare, String destinationSquare, boolean isCapture, Position position);
}
