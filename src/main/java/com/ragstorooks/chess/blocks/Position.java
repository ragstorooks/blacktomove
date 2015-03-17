package com.ragstorooks.chess.blocks;

@FunctionalInterface
public interface Position {
    Piece get(String square);
}
