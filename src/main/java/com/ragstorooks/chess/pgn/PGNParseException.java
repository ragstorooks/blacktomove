package com.ragstorooks.chess.pgn;

public class PGNParseException extends RuntimeException {
    public PGNParseException(String msg) {
        super(msg);
    }

    public PGNParseException(Throwable t) {
        super(t);
    }
}
