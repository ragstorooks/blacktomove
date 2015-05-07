package com.ragstorooks.chess.moves;

public class EnPassantableEvent {
    private String enPassantableSquare;

    public EnPassantableEvent(String enPassantableSquare) {
        this.enPassantableSquare = enPassantableSquare;
    }

    public String getEnPassantableSquare() {
        return enPassantableSquare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EnPassantableEvent that = (EnPassantableEvent) o;

        if (enPassantableSquare != null ? !enPassantableSquare.equals(that.enPassantableSquare) : that
                .enPassantableSquare != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return enPassantableSquare != null ? enPassantableSquare.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "EnPassantableEvent{" + "enPassantableSquare='" + enPassantableSquare + '\'' + '}';
    }
}
