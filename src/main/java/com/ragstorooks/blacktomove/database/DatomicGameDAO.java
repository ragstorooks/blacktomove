package com.ragstorooks.blacktomove.database;

import datomic.Connection;
import datomic.Peer;
import datomic.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class DatomicGameDAO implements GameDAO {
    private static Map<String, String> gameResultsMap = new HashMap<>();

    private Connection connection;

    public DatomicGameDAO(String connectionString) {
        populateGameResultsMap();

        connection = Peer.connect(connectionString);
    }

    private void populateGameResultsMap() {
        if (gameResultsMap.isEmpty()) {
            gameResultsMap.put("1-0", "games.result/WhiteWins");
            gameResultsMap.put("0-1", "games.result/BlackWins");
            gameResultsMap.put("1/2-1/2", "games.result/Draw");
        }
    }

    public Map saveGame(Game game) throws ExecutionException, InterruptedException {
        List<Map> saveGameTransaction = new ArrayList<>();

        List additionalInfoIds = populateAdditionalInfoInGameTransaction(game, saveGameTransaction);
        List positionIds = populatePositionsInGameTransaction(game, saveGameTransaction);

        saveGameTransaction.add(
                Util.map(":db/id", Peer.tempid(":games"),
                        "games/event", game.getEvent(),
                        "games/site", game.getSite(),
                        "games/date", game.getDate(),
                        "games/round", game.getRound(),
                        "games/white", game.getWhite(),
                        "games/black", game.getBlack(),
                        "games/result", gameResultsMap.get(game.getResult()),
                        "games/additionalInfo", additionalInfoIds,
                        "games/moves", positionIds));
        return connection.transact(saveGameTransaction).get();
    }

    private List populatePositionsInGameTransaction(Game game, List<Map> saveGameTransaction) {
        List<Map> positions = new ArrayList<>();
        game.getPositions().stream().forEach(position -> positions.add(
                Util.map(":db/id", Peer.tempid(":moves"),
                        "moves/position", position)));
        saveGameTransaction.addAll(positions);

        List positionIds = new ArrayList<>();
        positions.stream().forEach(entry -> positionIds.add(entry.get(":db/id")));
        return positionIds;
    }

    private List populateAdditionalInfoInGameTransaction(Game game, List<Map> saveGameTransaction) {
        game.getAdditionalInfo().entrySet().stream().forEach(entry -> saveGameTransaction.add(
                Util.map(":db/id", Peer.tempid(":extraGameMetadata"),
                        "extraGameMetadata/key", entry.getKey(),
                        "extraGameMetadata/value", entry.getValue())));
        List additionalInfoIds = new ArrayList<>();
        saveGameTransaction.stream().forEach(entry -> additionalInfoIds.add(entry.get(":db/id")));
        return additionalInfoIds;
    }

    public List<Game> findGamesWithPosition(String position) {
        return null;
    }
}
