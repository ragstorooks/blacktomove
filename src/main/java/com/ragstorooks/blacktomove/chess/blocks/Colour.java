package com.ragstorooks.blacktomove.chess.blocks;

public enum Colour {
    White,
    Black;

    public static Colour getColour(String c) {
        switch (c) {
            case "w":
            case "W":
                return Colour.White;
            case "b":
            case "B":
                return Colour.Black;
            default:
                return null;
        }
    }

    public static Colour getOppositeColour(Colour colour) {
        return White.equals(colour)? Black : White;
    }
}
