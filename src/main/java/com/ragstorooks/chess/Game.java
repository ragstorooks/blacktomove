package com.ragstorooks.chess;

import com.ragstorooks.chess.blocks.Board;
import com.ragstorooks.chess.moves.BasicMove;
import com.ragstorooks.chess.moves.Move;
import com.ragstorooks.chess.pieces.Piece;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Game {
    private Map<String, String> metadata = new HashMap<>();
    private List<Move> moves = new LinkedList<>();
    private Board board;

    public Game() {
        this(new Board());
    }

    public Game(Board board) {
        this.board = board;
    }

    public Game addMeta(String key, String value) {
        metadata.put(key, value);
        return this;
    }

    public void makeMove(Move move) {
        boolean isValidMove = false;
        if (move instanceof BasicMove)
            isValidMove = makeBasicMove((BasicMove) move);

        if (!isValidMove)
            throw new IllegalArgumentException("Invalid move: " + move);

        moves.add(move);
    }

    private boolean makeBasicMove(BasicMove move) {
        Map<String, Piece> candidatePieces = board.getPiecesOfType(move.getMover(), move.getPieceType());
        for (Entry<String, Piece> candidate : candidatePieces.entrySet()) {
            String originSquare = candidate.getKey();
            Piece piece = candidate.getValue();

            if (move.getSourceHint() != null && !originSquare.contains(move.getSourceHint()))
                continue;

            if (piece.canMoveTo(originSquare, move.getDestination(), move.isCapture(), square -> board.get(square))) {
                board.movePieceToSquare(candidate, move.getDestination());
                return true;
            }
        }

        return false;
    }

    public String getCurrentBoardPosition() {
        return board.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (obj.getClass() != getClass())
            return false;

        Game game = (Game) obj;
        return CollectionUtils.isEqualCollection(moves, game.moves) && isMetadataEqual(game.metadata);
    }

    private boolean isMetadataEqual(Map<String, String> otherMetadata) {
        if (ObjectUtils.equals(metadata, otherMetadata))
            return true;

        if (metadata.size() != otherMetadata.size())
            return false;

        for (Entry<String, String> entry : metadata.entrySet()) {
            if (!otherMetadata.containsKey(entry.getKey()) || !entry.getValue().equals(otherMetadata.get(entry.getKey
                    ())))
                return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(7, 9).append(metadata).append(moves).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("metadata", metadata).append("moves", moves).toString();
    }
}
