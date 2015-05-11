package com.ragstorooks.blacktomove.chess.moves;

import com.ragstorooks.blacktomove.chess.pieces.Piece;

@FunctionalInterface
public interface PieceMover {
    void move(String destination, Piece piece);
}
