package com.ragstorooks.chess.moves;

import com.ragstorooks.chess.blocks.Colour;

import java.util.Arrays;
import java.util.List;

public class QueensideCastle extends Castle {
    public QueensideCastle(Colour mover) {
        super(mover, "a", "c", "d");
    }

    @Override
    public List<String> getIntermediateSquares() {
        return Arrays.asList(getKingLocation(), getRookLocation(), "b" + getBackRank());
    }
}
