package com.ragstorooks.chess.blocks;

import com.ragstorooks.chess.pieces.Piece;

@FunctionalInterface
public interface Position {
    Piece get(String square);
}
