package com.ragstorooks.blacktomove.chess.blocks;

import com.ragstorooks.blacktomove.chess.pieces.Piece;

@FunctionalInterface
public interface Position {
    Piece get(String square);
}
