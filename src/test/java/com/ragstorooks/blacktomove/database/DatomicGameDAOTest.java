package com.ragstorooks.blacktomove.database;

import clojure.lang.Keyword;
import clojure.lang.Symbol;
import datomic.Connection;
import datomic.Peer;
import datomic.Util;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class DatomicGameDAOTest {
    private static final String CONNECTION_STRING = "datomic:mem://blacktomove";
    private static final String QUERY_TO_PULL_GAMES_FROM_DB = "[:find (pull ?g [*]) :where [?g :games/event]]";

    private static final String EVENT = "Vugar Gashimov Mem 2015";
    private static final String SITE = "Shamkir AZE";
    private static final String DATE = "2015.04.25";
    private static final String ROUND = "8";
    private static final String WHITE = "So, W.";
    private static final String BLACK = "Carlsen, M.";
    private static final String RESULT = "1/2-1/2";
    private static final String ECO_HEADER = "ECO";
    private static final String ECO_VALUE = "A29";
    private static final String SOURCE_DATE_HEADER = "SourceDate";
    private static final String SOURCE_DATE_VALUE = "2015.02.07";
    private static final String FIRST_MOVE = "firstMove";
    private static final String SECOND_MOVE = "secondMove";
    private static final String THIRD_MOVE = "thirdMove";
    private static final String FOURTH_MOVE = "fourthMove";

    private Game game;
    private Connection connection;
    private GameDAO gameDAO;

    @Before
    public void setupGame() {
        game = new Game(EVENT, SITE, DATE, ROUND, WHITE, BLACK, RESULT);
        game.addAdditionalInfo(ECO_HEADER, ECO_VALUE).addAdditionalInfo(SOURCE_DATE_HEADER, SOURCE_DATE_VALUE);
        game.addPosition(FIRST_MOVE).addPosition(SECOND_MOVE).addPosition(THIRD_MOVE).addPosition(FOURTH_MOVE);
    }

    @Before
    public void createDatabaseAndDAO() throws FileNotFoundException, ExecutionException, InterruptedException {
        Peer.createDatabase(CONNECTION_STRING);
        connection = Peer.connect(CONNECTION_STRING);
        Reader schemaReader = new FileReader("src/main/resources/db/schema.edn");
        List schemaTransaction = (List) Util.readAll(schemaReader).get(0);
        connection.transact(schemaTransaction).get();

        gameDAO = new DatomicGameDAO(CONNECTION_STRING);
    }

    @Test
    public void shouldSaveGame() throws ExecutionException, InterruptedException {
        // act
        gameDAO.saveGame(game);

        // assert
        Map<String, Object> gameEntity = assertOnlyOneGameInDBAndReturnGameMap();
        assertThatAllRequiredGameHeadersAreSaved(gameEntity);

        Map<String, String> additionalInfoResultsMap = getAdditionalGameHeadersFromDB(gameEntity);
        assertThatAdditionalGameHeadersAreSavedInDB(additionalInfoResultsMap);

        List<String> positions = getMovesListFromDB(gameEntity);
        assertThatMovesAreSavedInOrderInDB(positions);
    }

    private void assertThatMovesAreSavedInOrderInDB(List<String> positions) {
        assertThat(positions.size(), equalTo(4));
        assertThat(positions.get(0), equalTo(FIRST_MOVE));
        assertThat(positions.get(1), equalTo(SECOND_MOVE));
        assertThat(positions.get(2), equalTo(THIRD_MOVE));
        assertThat(positions.get(3), equalTo(FOURTH_MOVE));
    }

    private List<String> getMovesListFromDB(Map<String, Object> gameEntity) {
        List<String> movesList = new ArrayList<>();

        List moves = (List) gameEntity.get(":games/moves");
        for (Object item : moves) {
            Map move = (Map) item;
            movesList.add(move.get(createDatabaseKeyFor("moves/position")).toString());
        }
        return movesList;
    }

    private void assertThatAdditionalGameHeadersAreSavedInDB(Map<String, String> additionalInfoResultsMap) {
        assertThat(additionalInfoResultsMap.size(), equalTo(2));
        assertThat(additionalInfoResultsMap.get(ECO_HEADER), equalTo(ECO_VALUE));
        assertThat(additionalInfoResultsMap.get(SOURCE_DATE_HEADER), equalTo(SOURCE_DATE_VALUE));
    }

    private Map<String, String> getAdditionalGameHeadersFromDB(Map<String, Object> gameEntity) {
        Map<String, String> additionalInfoMap = new HashMap<>();

        List additionalInfoEntities = (List) gameEntity.get(":games/additionalInfo");
        for (Object item : additionalInfoEntities) {
            Map additionalInfo = (Map) item;
            additionalInfoMap.put(
                    additionalInfo.get(createDatabaseKeyFor("extraGameMetadata/key")).toString(),
                    additionalInfo.get(createDatabaseKeyFor("extraGameMetadata/value")).toString());
        }
        return additionalInfoMap;
    }

    private void assertThatAllRequiredGameHeadersAreSaved(Map<String, Object> gameEntity) {
        assertThat(gameEntity.get(":games/event"), equalTo(EVENT));
        assertThat(gameEntity.get(":games/site"), equalTo(SITE));
        assertThat(gameEntity.get(":games/date"), equalTo(DATE));
        assertThat(gameEntity.get(":games/round"), equalTo(ROUND));
        assertThat(gameEntity.get(":games/white"), equalTo(WHITE));
        assertThat(gameEntity.get(":games/black"), equalTo(BLACK));

        Long resultId = (Long) ((Map) gameEntity.get(":games/result")).get(createDatabaseKeyFor("db/id"));
        assertThat(connection.db().entity(resultId).get("db/ident").toString(), equalTo(":games.result/Draw"));
    }

    private Keyword createDatabaseKeyFor(String field) {
        return Keyword.intern(Symbol.create(field));
    }

    private Map<String, Object> assertOnlyOneGameInDBAndReturnGameMap() {
        Collection results = Peer.query(QUERY_TO_PULL_GAMES_FROM_DB, connection.db());
        assertThat(results.size(), equalTo(1));
        Map<?, ?> gameEntity = (Map<?, ?>) ((List) results.iterator().next()).get(0);

        Map<String, Object> result = new HashMap<>();
        gameEntity.entrySet().stream().forEach(entry -> result.put(entry.getKey().toString(), entry.getValue()));
        return result;
    }
}