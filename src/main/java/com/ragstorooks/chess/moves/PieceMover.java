package com.ragstorooks.chess.moves;

import com.ragstorooks.chess.pieces.Piece;

@FunctionalInterface
public interface PieceMover {
    void move(String destination, Piece piece);
}
