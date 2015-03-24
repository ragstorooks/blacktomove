package com.ragstorooks.chess.moves;

import com.ragstorooks.chess.blocks.Colour;

public class QueensideCastle extends Castle {
    public QueensideCastle(Colour mover) {
        super(mover, "a", "c", "d");
    }
}
