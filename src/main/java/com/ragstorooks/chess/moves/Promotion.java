package com.ragstorooks.chess.moves;

import com.ragstorooks.chess.blocks.Colour;
import com.ragstorooks.chess.pieces.PieceType;
import org.apache.commons.lang.builder.ToStringBuilder;

public class Promotion extends BasicMove {
    private PieceType promotionType;

    public Promotion(Colour mover, PieceType promotionType, String destination, boolean isCapture, String sourceHint) {
        super(mover, PieceType.PAWN, destination, isCapture, sourceHint);

        this.promotionType = promotionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Promotion promotion = (Promotion) o;

        if (promotionType != promotion.promotionType) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (promotionType != null ? promotionType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("promotionType", promotionType)
                .toString();
    }
}