package com.ragstorooks.blacktomove.database;

public class GameBuilder {
    private Game game;

    public GameBuilder(String event, String site, String date, String round, String white, String black, String
            result) {
        game = new Game(event, site, date, round, white, black, result);
    }

    public GameBuilder addAdditionalInfo(String key, String value) {
        game.addAdditionalInfo(key, value);
        return this;
    }

    public GameBuilder addPosition(String position) {
        game.addPosition(position);
        return this;
    }

    public GameBuilder setFullPgn(String fullPgn) {
        game.setFullPgn(fullPgn);
        return this;
    }

    public Game getGame() {
        return game;
    }
}
