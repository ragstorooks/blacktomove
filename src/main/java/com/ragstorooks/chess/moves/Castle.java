package com.ragstorooks.chess.moves;

import com.ragstorooks.chess.blocks.Colour;
import com.ragstorooks.chess.pieces.King;
import com.ragstorooks.chess.pieces.Piece;

import java.util.Map.Entry;

public abstract class Castle extends AbstractMove {
    private final static String WHITE_BACK_RANK = "1";
    private final static String BLACK_BACK_RANK = "8";
    private final static String KING_FILE = "e";

    private String kingLocation;
    private String rookLocation;
    private String castledKingLocation;
    private String castledRookLocation;

    protected Castle(Colour mover, String rookFile, String castledKingFile, String castledRookFile) {
        super(mover);

        String backRank = Colour.White.equals(mover) ? WHITE_BACK_RANK : BLACK_BACK_RANK;
        kingLocation = KING_FILE + backRank;
        rookLocation = rookFile + backRank;
        castledKingLocation = castledKingFile + backRank;
        castledRookLocation = castledRookFile + backRank;
    }

    @Override
    public void makeMove(Entry<String, Piece> source, PieceMover pieceMover) {
        pieceMover.move(kingLocation, null);
        pieceMover.move(rookLocation, null);

        pieceMover.move(castledKingLocation, new King(getMover()));
        pieceMover.move(castledRookLocation, new King(getMover()));
    }
}
