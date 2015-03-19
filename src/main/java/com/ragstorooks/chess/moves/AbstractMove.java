package com.ragstorooks.chess.moves;

import com.ragstorooks.chess.blocks.Colour;

public abstract class AbstractMove implements Move {
    private Colour mover;

    protected AbstractMove(Colour mover) {
        this.mover = mover;
    }

    @Override
    public Colour getMover() {
        return mover;
    }

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
        return "AbstractMove{" +
                "mover=" + mover +
                '}';
    }
}
