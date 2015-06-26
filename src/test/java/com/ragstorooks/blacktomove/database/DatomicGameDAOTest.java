package com.ragstorooks.blacktomove.database;

import datomic.Connection;
import datomic.Peer;
import datomic.Util;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static java.util.Collections.sort;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class DatomicGameDAOTest {
    private static final String CONNECTION_STRING = "datomic:mem://blacktomove";
    private static final String SCHEMA_FILE = "src/main/resources/db/schema.edn";
    private static final String TEST_DATA_FILE = "src/test/resources/db/test-data.edn";
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
    private static final String FULL_PGN = "this is the entire pgn string";

    private Game game;
    private Connection connection;
    private DatomicGameDAO datomicGameDAO;

    @Before
    public void setupGame() {
        GameBuilder gameBuilder = new GameBuilder(EVENT, SITE, DATE, ROUND, WHITE, BLACK, RESULT);
        gameBuilder.addAdditionalInfo(ECO_HEADER, ECO_VALUE).addAdditionalInfo(SOURCE_DATE_HEADER, SOURCE_DATE_VALUE);
        gameBuilder.addPosition(FIRST_MOVE).addPosition(SECOND_MOVE).addPosition(THIRD_MOVE).addPosition(FOURTH_MOVE);
        gameBuilder.setFullPgn(FULL_PGN);

        game = gameBuilder.getGame();
    }

    @Before
    public void createDatabaseAndDAO() throws FileNotFoundException, ExecutionException, InterruptedException {
        Peer.createDatabase(CONNECTION_STRING);
        connection = Peer.connect(CONNECTION_STRING);
        load(SCHEMA_FILE);

        datomicGameDAO = new DatomicGameDAO(CONNECTION_STRING);
    }

    private void load(String fileName) throws FileNotFoundException, InterruptedException, ExecutionException {
        Reader schemaReader = new FileReader(fileName);
        List schemaTransaction = (List) Util.readAll(schemaReader).get(0);
        connection.transact(schemaTransaction).get();
    }

    @After
    public void destroyDatabase() {
        Peer.deleteDatabase(CONNECTION_STRING);
    }

    @Test
    public void shouldSaveGame() throws ExecutionException, InterruptedException {
        // act
        Long result = datomicGameDAO.saveGame(game);

        // assert
        assertNotNull(result);

        Map<String, Object> gameEntity = assertOnlyOneGameInDBAndReturnGameMap();
        assertThatAllRequiredGameHeadersAreSaved(gameEntity);

        Map<String, String> additionalInfoResultsMap = datomicGameDAO.getAdditionalGameHeadersFromDB((Collection)
                gameEntity.get(":games/additionalInfo"));
        assertThatAdditionalGameHeadersAreSavedInDB(additionalInfoResultsMap);

        List<String> positions = getMovesListFromDB(gameEntity);
        assertThatMovesAreSavedInOrderInDB(positions);

        assertThat(gameEntity.get(":games/fullPgn"), equalTo(FULL_PGN));
        assertThat(gameEntity.get(":db/id"), equalTo(result));
    }

    @Test
    public void shouldFindGamesWithSpecificPosition() throws Exception {
        // setup
        load(TEST_DATA_FILE);

        // act
        List<Game> games = datomicGameDAO.findGamesWithPosition("p123");

        // assert
        assertThat(games.size(), equalTo(2));
        sort(games, (g1, g2) -> g1.getFullPgn().compareTo(g2.getFullPgn()));
        assertThat(games.get(0).getFullPgn(), equalTo("pgn1"));
        assertThat(games.get(1).getFullPgn(), equalTo("pgn3"));
    }

    @Test
    public void shouldFindGameById() throws Exception {
        // setup
        load(TEST_DATA_FILE);
        String id = getGameWithPgn("pgn2").get(":db/id").toString();

        // act
        Game game = datomicGameDAO.findGameById(id);

        // assert
        assertThat(game.getFullPgn(), equalTo("pgn2"));
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
            movesList.add(move.get(datomicGameDAO.createDatabaseKeyFor("moves/position")).toString());
        }
        return movesList;
    }

    private void assertThatAdditionalGameHeadersAreSavedInDB(Map<String, String> additionalInfoResultsMap) {
        assertThat(additionalInfoResultsMap.size(), equalTo(2));
        assertThat(additionalInfoResultsMap.get(ECO_HEADER), equalTo(ECO_VALUE));
        assertThat(additionalInfoResultsMap.get(SOURCE_DATE_HEADER), equalTo(SOURCE_DATE_VALUE));
    }

    private void assertThatAllRequiredGameHeadersAreSaved(Map<String, Object> gameEntity) {
        assertThat(gameEntity.get(":games/event"), equalTo(EVENT));
        assertThat(gameEntity.get(":games/site"), equalTo(SITE));
        assertThat(gameEntity.get(":games/date"), equalTo(DATE));
        assertThat(gameEntity.get(":games/round"), equalTo(ROUND));
        assertThat(gameEntity.get(":games/white"), equalTo(WHITE));
        assertThat(gameEntity.get(":games/black"), equalTo(BLACK));

        Long resultId = (Long) ((Map) gameEntity.get(":games/result")).get(datomicGameDAO.createDatabaseKeyFor("db/id"));
        assertThat(connection.db().entity(resultId).get("db/ident").toString(), equalTo(":games.result/Draw"));
    }

    private Map<String, Object> assertOnlyOneGameInDBAndReturnGameMap() {
        List<Map<String, Object>> allGamesFromDB = getAllGamesFromDBAndReturnListOfGameMaps();
        assertThat(allGamesFromDB.size(), equalTo(1));
        return allGamesFromDB.get(0);
    }

    private Map<String, Object> getGameWithPgn(String pgn) {
        List<Map<String, Object>> allGamesFromDB = getAllGamesFromDBAndReturnListOfGameMaps();
        return allGamesFromDB.stream().filter(map -> map.get(":games/fullPgn").equals(pgn)).findFirst().get();
    }

    private List<Map<String, Object>> getAllGamesFromDBAndReturnListOfGameMaps() {
        List<Map<String, Object>> result = new ArrayList<>();

        Collection results = Peer.query(QUERY_TO_PULL_GAMES_FROM_DB, connection.db());
        Iterator iterator = results.iterator();
        while (iterator.hasNext()) {
            Map<?, ?> gameEntity = (Map<?, ?>) ((List) iterator.next()).get(0);
            Map<String, Object> map = new HashMap<>();
            gameEntity.entrySet().stream().forEach(entry -> map.put(entry.getKey().toString(), entry.getValue()));

            result.add(map);
        }
        return result;
    }
}