package com.ragstorooks.chess.moves;

import com.ragstorooks.chess.blocks.Colour;
import com.ragstorooks.chess.pieces.Piece;
import com.ragstorooks.chess.pieces.PieceType;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Arrays;
import java.util.List;
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
    public EnPassantableEvent move(Entry<String, Piece> source, PieceMover pieceMover) {
        String sourceSquare = source.getKey();
        Piece piece = source.getValue();

        pieceMover.move(sourceSquare, null);
        pieceMover.move(destination, piece);

        if (isADoublePawnMove(piece, sourceSquare, destination))
            return new EnPassantableEvent(getCapturableSquare(sourceSquare));

        return null;
    }

    private String getCapturableSquare(String source) {
        char file = source.charAt(0);
        int rank = Integer.parseInt(source.substring(1));

        return "" + file + (Colour.White.equals(getMover())? rank + 1 : rank - 1);
    }

    private boolean isADoublePawnMove(Piece piece, String source, String destination) {
        if (!PieceType.PAWN.equals(getPieceType()))
            return false;

        int destinationRank = Integer.parseInt(destination.substring(1));
        int originRank = Integer.parseInt(source.substring(1));
        int numberOfMovingRanks = Math.abs(destinationRank - originRank);

        return numberOfMovingRanks == 2;
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
