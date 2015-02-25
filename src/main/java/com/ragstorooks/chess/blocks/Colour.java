package com.ragstorooks.chess.blocks;

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
}
