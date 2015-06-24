package com.ragstorooks.blacktomove.database;

import clojure.lang.Keyword;
import clojure.lang.Symbol;
import datomic.Connection;
import datomic.Peer;
import datomic.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class DatomicGameDAO implements GameDAO {
    private static final String FIND_MOVES_WITH_POSITION = "[:find (pull ?m [*]) :in $ ?position :where [?m " +
            ":moves/position ?position]]";
    private static final String FIND_GAMES_WITH_MOVE = "[:find (pull ?g [*]) :in $ [?m ...] :where [?g " +
            ":games/moves ?m]]";

    private Connection connection;

    public DatomicGameDAO(String connectionString) {
        connection = Peer.connect(connectionString);
    }

    public Map saveGame(Game game) throws ExecutionException, InterruptedException {
        List<Map> saveGameTransaction = new ArrayList<>();

        List additionalInfoIds = populateAdditionalInfo(game, saveGameTransaction);
        List positionIds = populatePositions(game, saveGameTransaction);

        saveGameTransaction.add(
                Util.map(":db/id", Peer.tempid(":games"),
                        "games/event", game.getEvent(),
                        "games/site", game.getSite(),
                        "games/date", game.getDate(),
                        "games/round", game.getRound(),
                        "games/white", game.getWhite(),
                        "games/black", game.getBlack(),
                        "games/fullPgn", game.getFullPgn(),
                        "games/result", GameResult.getDbFormat(game.getResult()),
                        "games/additionalInfo", additionalInfoIds,
                        "games/moves", positionIds));
        return connection.transact(saveGameTransaction).get();
    }

    public List<Game> findGamesWithPosition(String position) {
        List<Game> result = new ArrayList<>();

        List<Long> moveIds = findMoveIdsWithPosition(position);
        List gamesWithPosition = Peer.query(FIND_GAMES_WITH_MOVE, connection.db(), moveIds);
        for (Object gameWithPosition : gamesWithPosition) {
            result.add(convertToGame((List) gameWithPosition));
        }

        return result;
    }

    private Game convertToGame(List gameWithPosition) {
        Map<String, Object> returnedGame = new HashMap<>();
        Map<Object, Object> game = (Map) gameWithPosition.get(0);
        game.entrySet().stream().forEach(entry -> returnedGame.put(entry.getKey().toString(), entry.getValue()));
        return mapDbGameToGame(returnedGame);
    }

    private List<Long> findMoveIdsWithPosition(String position) {
        List movesWithPosition = Peer.query(FIND_MOVES_WITH_POSITION, connection.db(), position);
        List<Long> moveIds = new ArrayList<>();
        for (Object moveWithPosition : movesWithPosition) {
            Map<Object, Object> move = (Map) ((List)moveWithPosition).get(0);
            moveIds.add((Long) move.get(createDatabaseKeyFor("db/id")));
        }
        return moveIds;
    }

    Keyword createDatabaseKeyFor(String field) {
        return Keyword.intern(Symbol.create(field));
    }

    private Game mapDbGameToGame(Map<String, Object> returnedGame) {
        GameBuilder gameBuilder = new GameBuilder(returnedGame.get(":games/event").toString(),
                returnedGame.get(":games/site").toString(),
                returnedGame.get(":games/date").toString(),
                returnedGame.get(":games/round").toString(),
                returnedGame.get(":games/white").toString(),
                returnedGame.get(":games/black").toString(),
                convertGameResultEnum((Map) returnedGame.get(":games/result")));

        Map<String, String> additionalHeaders = getAdditionalGameHeadersFromDB(returnedGame);
        additionalHeaders.entrySet().stream().forEach(entry -> gameBuilder.addAdditionalInfo(entry.getKey(), entry.getValue
                ()));

        return gameBuilder.setFullPgn(returnedGame.get(":games/fullPgn").toString()).getGame();
    }

    Map<String, String> getAdditionalGameHeadersFromDB(Map<String, Object> returnedGame) {
        Map<String, String> additionalInfoMap = new HashMap<>();

        List additionalInfoEntities = (List) returnedGame.get(":games/additionalInfo");
        for (Object item : additionalInfoEntities) {
            Map additionalInfo = (Map) item;
            additionalInfoMap.put(
                    additionalInfo.get(createDatabaseKeyFor("extraGameMetadata/key")).toString(),
                    additionalInfo.get(createDatabaseKeyFor("extraGameMetadata/value")).toString());
        }
        return additionalInfoMap;
    }

    private String convertGameResultEnum(Map<Object, Object> resultEnum) {
        Long resultId = (Long) resultEnum.get(createDatabaseKeyFor("db/id"));
        return GameResult.getPgnFormat(connection.db().entity(resultId).get(createDatabaseKeyFor("db/ident"))
                .toString());
    }

    private List populatePositions(Game game, List<Map> saveGameTransaction) {
        List<Map> positions = new ArrayList<>();
        game.getPositions().stream().forEach(position -> positions.add(
                Util.map(":db/id", Peer.tempid(":moves"),
                        "moves/position", position)));
        saveGameTransaction.addAll(positions);

        List positionIds = new ArrayList<>();
        positions.stream().forEach(entry -> positionIds.add(entry.get(":db/id")));
        return positionIds;
    }

    private List populateAdditionalInfo(Game game, List<Map> saveGameTransaction) {
        game.getAdditionalInfo().entrySet().stream().forEach(entry -> saveGameTransaction.add(
                Util.map(":db/id", Peer.tempid(":extraGameMetadata"),
                        "extraGameMetadata/key", entry.getKey(),
                        "extraGameMetadata/value", entry.getValue())));
        List additionalInfoIds = new ArrayList<>();
        saveGameTransaction.stream().forEach(entry -> additionalInfoIds.add(entry.get(":db/id")));
        return additionalInfoIds;
    }
}
