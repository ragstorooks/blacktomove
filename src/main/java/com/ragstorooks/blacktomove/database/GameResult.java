package com.ragstorooks.blacktomove.database;

import java.util.HashMap;
import java.util.Map;

public enum GameResult {
    WhiteWins("1-0", "WhiteWins"),
    BlackWins("0-1", "BlackWins"),
    Draw("1/2-1/2", "Draw");

    private final static String DB_FORMAT_PREFIX = ":games.result/";

    private final static Map<String, GameResult> pgnMap = new HashMap<>(values().length);
    private final static Map<String, GameResult> dbMap = new HashMap<>(values().length);

    private String pgnFormat;
    private String dbFormat;

    static {
        for (GameResult g : values()) {
            pgnMap.put(g.pgnFormat, g);
            dbMap.put(g.dbFormat, g);
        }
    }

    GameResult(String pgnFormat, String dbFormat) {
        this.pgnFormat = pgnFormat;
        this.dbFormat = DB_FORMAT_PREFIX + dbFormat;
    }

    public static String getDbFormat(String pgnFormat) {
        GameResult gameResult = pgnMap.get(pgnFormat);
        if (gameResult == null)
            throw new IllegalArgumentException(pgnFormat);

        return gameResult.dbFormat;
    }

    public static String getPgnFormat(String dbFormat) {
        GameResult gameResult = dbMap.get(dbFormat);
        if (gameResult == null)
            throw new IllegalArgumentException(dbFormat);

        return gameResult.pgnFormat;
    }
}
