package com.ragstorooks.blacktomove.chess.moves;

import com.ragstorooks.blacktomove.chess.blocks.Colour;

import java.util.Arrays;
import java.util.List;

public class QueensideCastle extends Castle {
    public QueensideCastle(Colour mover) {
        super(mover, "a", "c", "d");
    }

    @Override
    public List<String> getIntermediateSquares() {
        return Arrays.asList(getCastledKingLocation(), getCastledRookLocation(), "b" + getBackRank());
    }
}
