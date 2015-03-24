package com.ragstorooks.chess.moves;

import com.ragstorooks.chess.blocks.Colour;
import com.ragstorooks.chess.pieces.Piece;

import java.util.Map.Entry;

public interface Move {
    Colour getMover();

    void makeMove(Entry<String, Piece> source, PieceMover pieceMover);
}
