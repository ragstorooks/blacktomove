package com.ragstorooks.blacktomove.chess;

public class PGNParseException extends RuntimeException {
    public PGNParseException(String msg) {
        super(msg);
    }

    public PGNParseException(Throwable t) {
        super(t);
    }
}
