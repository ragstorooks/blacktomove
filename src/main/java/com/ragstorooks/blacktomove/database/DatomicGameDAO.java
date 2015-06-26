package com.ragstorooks.blacktomove.database;

import clojure.lang.Keyword;
import clojure.lang.Symbol;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import datomic.Connection;
import datomic.Entity;
import datomic.Peer;
import datomic.Util;
import datomic.query.EntityMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;


@Singleton
public class DatomicGameDAO implements GameDAO {
    private static final String FIND_MOVES_WITH_POSITION = "[:find (pull ?m [*]) :in $ ?position :where [?m " +
            ":moves/position ?position]]";
    private static final String FIND_GAMES_WITH_MOVE = "[:find (pull ?g [*]) :in $ [?m ...] :where [?g " +
            ":games/moves ?m]]";
    private static final String FIND_GAMES_WITH_ID = "[:find (pull ?g [*]) :where [?g :games/id ?i]]";

    private Connection connection;

    @Inject
    DatomicGameDAO(@Named("Connection String")String connectionString) {
        connection = Peer.connect(connectionString);
    }

    @Override
    public Long saveGame(Game game) throws ExecutionException, InterruptedException {
        List<Map> saveGameTransaction = new ArrayList<>();

        List additionalInfoIds = populateAdditionalInfo(game, saveGameTransaction);
        List positionIds = populatePositions(game, saveGameTransaction);

        Object gameTempId = Peer.tempid(":games");
        saveGameTransaction.add(
                Util.map(":db/id", gameTempId,
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

        Map<Object, Object> transactionData = connection.transact(saveGameTransaction).get();
        Map<Object, String> tempIds = (Map<Object, String>) transactionData.get(createDatabaseKeyFor("tempids"));
        System.out.println("gameTempId = " + gameTempId);

        Long gameId = (Long) Peer.resolveTempid(connection.db(), tempIds, gameTempId);

        return gameId;
    }

    @Override
    public List<Game> findGamesWithPosition(String position) {
        List<Game> result = new ArrayList<>();

        List<Long> moveIds = findMoveIdsWithPosition(position);
        List gamesWithPosition = Peer.query(FIND_GAMES_WITH_MOVE, connection.db(), moveIds);
        for (Object gameWithPosition : gamesWithPosition) {
            result.add(convertToGame((List) gameWithPosition));
        }

        return result;
    }

    @Override
    public Game findGameById(String id) {
        Map<String, Object> entityMap = new HashMap<>();

        Entity entity = connection.db().entity(Long.parseLong(id));
        entity.keySet().stream().forEach(key -> entityMap.put(key.toString(), entity.get(key)));

        return mapDbGameToGame(entityMap);
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
        String result = getResult(returnedGame.get(":games/result"));
        GameBuilder gameBuilder = new GameBuilder(returnedGame.get(":games/event").toString(),
                returnedGame.get(":games/site").toString(),
                returnedGame.get(":games/date").toString(),
                returnedGame.get(":games/round").toString(),
                returnedGame.get(":games/white").toString(),
                returnedGame.get(":games/black").toString(),
                result);

        Map<String, String> additionalHeaders = getAdditionalGameHeadersFromDB((Collection) returnedGame.get
                (":games/additionalInfo"));
        additionalHeaders.entrySet().stream().forEach(entry -> gameBuilder.addAdditionalInfo(entry.getKey(), entry.getValue
                ()));

        return gameBuilder.setFullPgn(returnedGame.get(":games/fullPgn").toString()).getGame();
    }

    private String getResult(Object resultEnum) {
        return resultEnum instanceof Map? convertGameResultEnum((Map) resultEnum) : GameResult.getPgnFormat
                (resultEnum.toString());
    }

    Map<String, String> getAdditionalGameHeadersFromDB(Collection additionalInfoEntities) {
        Map<String, String> additionalInfoMap = new HashMap<>();

        additionalInfoEntities.stream().forEach(entity -> {
            if (entity instanceof Map) {
                Map entityMap = (Map) entity;
                additionalInfoMap.put(
                        entityMap.get(createDatabaseKeyFor("extraGameMetadata/key")).toString(),
                        entityMap.get(createDatabaseKeyFor("extraGameMetadata/value")).toString());
            } else {
                EntityMap entityMap = (EntityMap) entity;
                additionalInfoMap.put(
                        entityMap.get(createDatabaseKeyFor("extraGameMetadata/key")).toString(),
                        entityMap.get(createDatabaseKeyFor("extraGameMetadata/value")).toString());
            }
        });

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
