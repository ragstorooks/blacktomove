package com.ragstorooks.blacktomove.chess.moves;

import com.ragstorooks.blacktomove.chess.blocks.Colour;

import java.util.Arrays;
import java.util.List;

public class KingsideCastle extends Castle {
    public KingsideCastle(Colour mover) {
        super(mover, "h", "g", "f");
    }

    @Override
    public List<String> getIntermediateSquares() {
        return Arrays.asList(getCastledKingLocation(), getCastledRookLocation());
    }
}
