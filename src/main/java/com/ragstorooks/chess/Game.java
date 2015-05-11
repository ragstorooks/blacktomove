package com.ragstorooks.chess;

import com.ragstorooks.chess.blocks.Board;
import com.ragstorooks.chess.blocks.Colour;
import com.ragstorooks.chess.moves.BasicMove;
import com.ragstorooks.chess.moves.Castle;
import com.ragstorooks.chess.moves.EnPassantableEvent;
import com.ragstorooks.chess.moves.EnPassantableEventListener;
import com.ragstorooks.chess.moves.KingsideCastle;
import com.ragstorooks.chess.moves.Move;
import com.ragstorooks.chess.moves.QueensideCastle;
import com.ragstorooks.chess.pieces.Pawn;
import com.ragstorooks.chess.pieces.Piece;
import com.ragstorooks.chess.pieces.PieceType;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Game implements EnPassantableEventListener {
    private static final Logger logger = LoggerFactory.getLogger(Game.class);

    private Map<String, String> metadata = new HashMap<>();
    private List<Move> moves = new LinkedList<>();
    private Board board;

    private Map<Colour, CastleOptions> castleOptions = new HashMap<>();
    private String enPassantableSquare;

    public Game() {
        this(new Board());
    }

    public Game(Board board) {
        this.board = board;

        castleOptions.put(Colour.White, new CastleOptions());
        castleOptions.put(Colour.Black, new CastleOptions());
    }

    public Game addMeta(String key, String value) {
        metadata.put(key, value);
        return this;
    }

    public void makeMove(Move move) {
        logger.debug("Current Board position: {}, Current Move: {}", board.toString(), move);

        boolean isValidMove = false;
        if (move instanceof BasicMove)
            isValidMove = makeBasicMove((BasicMove) move);
        else if (move instanceof Castle)
            isValidMove = castle((Castle) move);

        if (!isValidMove)
            throw new IllegalArgumentException("Invalid move: " + move);

        moves.add(move);
    }

    private boolean makeBasicMove(BasicMove move) {
        Colour mover = move.getMover();
        PieceType pieceType = move.getPieceType();
        Map<String, Piece> candidatePieces = board.getPiecesOfType(mover, pieceType);
        for (Entry<String, Piece> candidate : candidatePieces.entrySet()) {
            String originSquare = candidate.getKey();
            Piece piece = candidate.getValue();

            if (move.getSourceHint() != null && !originSquare.contains(move.getSourceHint()))
                continue;

            String destinationSquare = move.getDestination();
            boolean isEnPassantMove = PieceType.PAWN.equals(pieceType) && move.isCapture() &&
                    destinationSquare.equals(enPassantableSquare);

            if (piece.canMoveTo(originSquare, destinationSquare, move.isCapture(),
                    square -> isEnPassantMove && destinationSquare.equals(square) ?
                            new Pawn(Colour.getOppositeColour(mover)) : board.get(square))
                    && !isIllegalMoveBecauseOfCheck(move, candidate)) {
                move.registerEnPassantableEventListener(this);

                move.makeMove(candidate, (destination, pieceToPlace) -> board.put(destination, pieceToPlace));
                if (isEnPassantMove)
                    board.put(getEnPassantedPawn(move), null);

                updateCastlingAllowed(pieceType, mover, originSquare);

                return true;
            }
        }

        return false;
    }

    private String getEnPassantedPawn(BasicMove move) {
        String destinationSquare = move.getDestination();
        char destinationFile = destinationSquare.charAt(0);
        int destinationRank = Integer.parseInt(destinationSquare.substring(1));

        int enPassantedPawnRank = Colour.White.equals(move.getMover())? destinationRank - 1 : destinationRank + 1;
        return "" + destinationFile + enPassantedPawnRank;
    }

    private void updateCastlingAllowed(PieceType pieceType, Colour mover, String originSquare) {
        if (PieceType.KING.equals(pieceType)) {
            castleOptions.get(mover).kingsideCastleAllowed = false;
            castleOptions.get(mover).queensideCastleAllowed = false;
        } else if (PieceType.ROOK.equals(pieceType)) {
            if ((Colour.White.equals(mover) && "a1".equals(originSquare)) ||
                    Colour.Black.equals(mover) && "a8".equals(originSquare))
                castleOptions.get(mover).queensideCastleAllowed = false;
            else if ((Colour.White.equals(mover) && "h1".equals(originSquare)) ||
                    Colour.Black.equals(mover) && "h8".equals(originSquare))
                castleOptions.get(mover).kingsideCastleAllowed = false;
        }
    }

    private boolean castle(Castle move) {
        Colour mover = move.getMover();
        if ((move instanceof KingsideCastle && !castleOptions.get(mover).kingsideCastleAllowed) ||
                (move instanceof QueensideCastle && !castleOptions.get(mover).queensideCastleAllowed))
            return false;

        for (String square : move.getIntermediateSquares())
            if (board.get(square) != null)
                return false;

        if (isInCheck(board, mover))
            return false;

        Entry<String, Piece> king = getKing(board, mover);
        if (isIllegalMoveBecauseOfCheck(new BasicMove(mover, PieceType.KING, move.getCastledRookLocation(), false,
                null), king) || isIllegalMoveBecauseOfCheck(new BasicMove(mover, PieceType.KING, move
                .getCastledKingLocation(), false, null), king))
            return false;

        move.makeMove(king, (destination, pieceToPlace) -> board.put(destination, pieceToPlace));

        castleOptions.get(mover).kingsideCastleAllowed = false;
        castleOptions.get(mover).queensideCastleAllowed = false;

        return true;
    }

    private boolean isIllegalMoveBecauseOfCheck(BasicMove move, Entry<String, Piece> pieceToMove) {
        Colour mover = move.getMover();
        Board tempBoard = board.copy();
        move.makeMove(pieceToMove, (destination, pieceToPlace) -> tempBoard.put(destination, pieceToPlace));
        if (isInCheck(tempBoard, mover))
            return true;

        return false;
    }

    @Override
    public void notify(EnPassantableEvent enPassantableEvent) {
        enPassantableSquare = enPassantableEvent == null? null : enPassantableEvent.getEnPassantableSquare();
    }

    public String getCurrentBoardPosition() {
        return board.toString();
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    private boolean isInCheck(Board board, Colour colour) {
        String kingLocation = getKing(board, colour).getKey();
        Map<String, Piece> oppositionPieces = board.getPiecesOfColour(Colour.getOppositeColour(colour));
        for (Entry<String, Piece> candidate : oppositionPieces.entrySet()) {
            if (candidate.getValue().canMoveTo(candidate.getKey(), kingLocation, true, square -> board.get(square)))
                return true;
        }

        return false;
    }

    private Entry<String, Piece> getKing(Board board, Colour colour) {
        Map<String, Piece> king = board.getPiecesOfType(colour, PieceType.KING);
        if (king.size() != 1)
            throw new IllegalArgumentException("Board has more than one " + colour + " King!: " + board);

        return king.entrySet().iterator().next();
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

    @Override
    public int hashCode() {
        return new HashCodeBuilder(7, 9).append(metadata).append(moves).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("metadata", metadata).append("moves", moves).toString();
    }

    private static class CastleOptions {
        private boolean kingsideCastleAllowed = true;
        private boolean queensideCastleAllowed = true;
    }
}
