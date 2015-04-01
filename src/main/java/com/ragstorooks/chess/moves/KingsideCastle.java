package com.ragstorooks.chess.moves;

import com.ragstorooks.chess.blocks.Colour;

import java.util.Arrays;
import java.util.List;

public class KingsideCastle extends Castle {
    public KingsideCastle(Colour mover) {
        super(mover, "h", "g", "f");
    }

    @Override
    public List<String> getIntermediateSquares() {
        return Arrays.asList(getKingLocation(), getRookLocation());
    }
}
