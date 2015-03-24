package com.ragstorooks.chess.moves;

import com.ragstorooks.chess.blocks.Colour;
import com.ragstorooks.chess.pieces.Piece;
import com.ragstorooks.chess.pieces.PieceType;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Map.Entry;

public class BasicMove extends AbstractMove {
    private PieceType pieceType;
    private String destination;
    private boolean isCapture;
    private String sourceHint;

    public BasicMove(Colour mover, PieceType pieceType, String destination, boolean isCapture, String sourceHint) {
        super(mover);
        this.pieceType = pieceType;
        this.destination = destination;
        this.isCapture = isCapture;
        this.sourceHint = sourceHint;
    }

    public PieceType getPieceType() {
        return pieceType;
    }

    public String getDestination() {
        return destination;
    }

    public String getSourceHint() {
        return sourceHint;
    }

    public boolean isCapture() {
        return isCapture;
    }

    @Override
    public void makeMove(Entry<String, Piece> source, PieceMover pieceMover) {
        pieceMover.move(source.getKey(), null);
        pieceMover.move(destination, source.getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        BasicMove basicMove = (BasicMove) o;

        if (isCapture != basicMove.isCapture) return false;
        if (destination != null ? !destination.equals(basicMove.destination) : basicMove.destination != null)
            return false;
        if (pieceType != basicMove.pieceType) return false;
        if (sourceHint != null ? !sourceHint.equals(basicMove.sourceHint) : basicMove.sourceHint != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (pieceType != null ? pieceType.hashCode() : 0);
        result = 31 * result + (destination != null ? destination.hashCode() : 0);
        result = 31 * result + (isCapture ? 1 : 0);
        result = 31 * result + (sourceHint != null ? sourceHint.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("pieceType", pieceType).append
                ("destination", destination).append("isCapture", isCapture).append("sourceHint", sourceHint).toString();
    }
}
