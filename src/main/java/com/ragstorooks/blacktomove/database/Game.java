package com.ragstorooks.blacktomove.database;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private String event;
    private String site;
    private String date;
    private String round;
    private String white;
    private String black;
    private String result;
    private Map<String, String> additionalInfo = new HashMap<>();
    private List<String> positions = new ArrayList<>();
    private String fullPgn;

    public Game(String event, String site, String date, String round, String white, String black, String result) {
        this.event = event;
        this.site = site;
        this.date = date;
        this.round = round;
        this.white = white;
        this.black = black;
        this.result = result;
    }

    public Game addAdditionalInfo(String key, String value) {
        additionalInfo.put(key, value);
        return this;
    }

    public Game addPosition(String position) {
        positions.add(position);
        return this;
    }

    public Game setFullPgn(String fullPgn) {
        this.fullPgn = fullPgn;
        return this;
    }

    public String getEvent() {
        return event;
    }

    public String getSite() {
        return site;
    }

    public String getDate() {
        return date;
    }

    public String getRound() {
        return round;
    }

    public String getWhite() {
        return white;
    }

    public String getBlack() {
        return black;
    }

    public String getResult() {
        return result;
    }

    public Map<String, String> getAdditionalInfo() {
        return Collections.unmodifiableMap(additionalInfo);
    }

    public Collection<String> getPositions() {
        return Collections.unmodifiableCollection(positions);
    }

    public String getFullPgn() {
        return fullPgn;
    }
}
