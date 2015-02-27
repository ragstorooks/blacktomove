package com.ragstorooks.chess.blocks;

@FunctionalInterface
public interface Square {
    Piece get(String square);
}
