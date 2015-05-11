package com.ragstorooks.blacktomove.chess.moves;

import com.ragstorooks.blacktomove.chess.blocks.Colour;
import com.ragstorooks.blacktomove.chess.pieces.Piece;

import java.util.Map.Entry;

public interface Move {
    Colour getMover();

    void makeMove(Entry<String, Piece> source, PieceMover pieceMover);

    void registerEnPassantableEventListener(EnPassantableEventListener enPassantableEventListener);
}
