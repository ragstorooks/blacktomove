package com.ragstorooks.blacktomove.chess.moves;

import com.ragstorooks.blacktomove.chess.blocks.Colour;
import com.ragstorooks.blacktomove.chess.pieces.Piece;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Map.Entry;

public abstract class AbstractMove implements Move {
    private Colour mover;
    private final EnPassantableEvent defaultEnPassantableEvent = new EnPassantableEvent(null);
    private EnPassantableEventListener enPassantableEventListener;

    protected AbstractMove(Colour mover) {
        this.mover = mover;
    }

    @Override
    public Colour getMover() {
        return mover;
    }

    public void registerEnPassantableEventListener(EnPassantableEventListener enPassantableEventListener) {
        this.enPassantableEventListener = enPassantableEventListener;
    }

    @Override
    public final void makeMove(Entry<String, Piece> source, PieceMover pieceMover) {
        EnPassantableEvent enPassantableEvent = move(source, pieceMover);
        if (enPassantableEventListener != null)
            enPassantableEventListener.notify(enPassantableEvent);
    }

    protected abstract EnPassantableEvent move(Entry<String, Piece> source, PieceMover pieceMover);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractMove that = (AbstractMove) o;

        if (mover != that.mover) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return mover != null ? mover.hashCode() : 0;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("mover", mover).toString();
    }
}
