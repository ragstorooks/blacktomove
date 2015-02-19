package com.ragstorooks.pgn;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class PGN {
    private Map<String, String> metadata = new HashMap<String, String>();
    private String moves;

    public PGN add(String key, String value) {
        metadata.put(key, value);
        return this;
    }

    public PGN setMoves(String moves) {
        this.moves = moves;
        return this;
    }

    public PGN addMoves(String moves) {
        if (StringUtils.isBlank(this.moves))
            return setMoves(moves);

        return setMoves(this.moves + moves);
    }

    public String getMoves() {
        return moves;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (obj.getClass() != getClass())
            return false;

        PGN pgn = (PGN) obj;
        return StringUtils.equals(moves, pgn.moves) && isMetadataEqual(pgn.metadata);
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
