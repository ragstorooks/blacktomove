package com.ragstorooks.chess.moves;

import com.ragstorooks.chess.blocks.Colour;
import com.ragstorooks.chess.pieces.King;
import com.ragstorooks.chess.pieces.Piece;
import com.ragstorooks.chess.pieces.Rook;
import oracle.jrockit.jfr.StringConstantPool;

import java.util.List;
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

        String backRank = getBackRank();
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
        pieceMover.move(castledRookLocation, new Rook(getMover()));
    }

    public String getKingLocation() {
        return kingLocation;
    }

    public String getRookLocation() {
        return rookLocation;
    }

    public String getCastledKingLocation() {
        return castledKingLocation;
    }

    public String getCastledRookLocation() {
        return castledRookLocation;
    }

    protected String getBackRank() {
        return Colour.White.equals(getMover()) ? WHITE_BACK_RANK : BLACK_BACK_RANK;
    }

    public abstract List<String> getIntermediateSquares();
}
