package com.ragstorooks.chess.blocks;

import com.ragstorooks.chess.pieces.AbstractPiece;

@FunctionalInterface
public interface Position {
    AbstractPiece get(String square);
}
