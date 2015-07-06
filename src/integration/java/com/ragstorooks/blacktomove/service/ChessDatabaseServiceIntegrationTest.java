package com.ragstorooks.blacktomove.service;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.ragstorooks.blacktomove.database.DatabaseModule;
import datomic.Connection;
import datomic.Peer;
import datomic.Util;
import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.URLEncoder;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class ChessDatabaseServiceIntegrationTest extends JerseyTest {
    private static final String TMP_DIRECTORY = System.getProperty("java.io.tmpdir") + File.separator;
    private static final String IN_DIRECTORY = TMP_DIRECTORY + "in-processor";
    private static final String OUT_DIRECTORY = TMP_DIRECTORY + "out-processor";
    private static final String ERRORS_DIRECTORY = TMP_DIRECTORY + "errors-processor";

    private static final String SCHEMA_FILE = "src/main/resources/db/schema.edn";
    private static final String CONNECTION_STRING = "datomic:mem://blacktomove-integration";

    private PgnDirectoryProcessor pgnDirectoryProcessor;
    private Injector injector;

    @BeforeClass
    public static void setupInAndOutDirectories() throws IOException {
        FileUtils.forceMkdir(new File(IN_DIRECTORY));
        FileUtils.forceMkdir(new File(OUT_DIRECTORY));
        FileUtils.forceMkdir(new File(ERRORS_DIRECTORY));
    }

    @AfterClass
    public static void deleteDirectories() throws IOException {
        FileUtils.deleteDirectory(new File(IN_DIRECTORY));
        FileUtils.deleteDirectory(new File(OUT_DIRECTORY));
        FileUtils.deleteDirectory(new File(ERRORS_DIRECTORY));
    }

    private void createServiceAndDependencies() throws Exception {
        Peer.createDatabase(CONNECTION_STRING);
        load(SCHEMA_FILE);

        injector = Guice.createInjector(new DatabaseModule(), new AbstractModule() {
            @Override
            protected void configure() {
                bind(String.class).annotatedWith(Names.named("Connection String")).toInstance(CONNECTION_STRING);
                bind(String.class).annotatedWith(Names.named("Input Dir")).toInstance(IN_DIRECTORY);
                bind(String.class).annotatedWith(Names.named("Output Dir")).toInstance(OUT_DIRECTORY);
                bind(String.class).annotatedWith(Names.named("Errors Dir")).toInstance(ERRORS_DIRECTORY);
            }
        });

        pgnDirectoryProcessor = injector.getInstance(PgnDirectoryProcessor.class);
    }

    private void load(String fileName) throws FileNotFoundException, InterruptedException, ExecutionException {
        Reader schemaReader = new FileReader(fileName);
        List schemaTransaction = (List) Util.readAll(schemaReader).get(0);

        Connection connection = Peer.connect(CONNECTION_STRING);
        connection.transact(schemaTransaction).get();
    }

    @Override
    protected Application configure() {
        try {
            createServiceAndDependencies();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new Application() {
            @Override
            public Set<Object> getSingletons() {
                Set<Object> singletons = new HashSet<>();
                singletons.add(injector.getInstance(ChessDatabaseService.class));
                singletons.add(injector.getInstance(ObjectMapperResolver.class));
                singletons.add(new JacksonFeature());
                return singletons;
            }
        };
    }

    @Test
    public void testThatASingleGameIsSavedIntoTheDatabaseAndTheRightUrlIsReturned() throws Exception {
        // setup
        String pgn = loadPgnForAnandNakamura();

        // save game & then find position
        Response response = target("game/pgn").request().post(Entity.text(pgn));
        assertThat(response.getStatus(), equalTo(Status.CREATED.getStatusCode()));

        String url = response.getLocation().getPath();
        String result = target(url).request().get(String.class);
        assertThat(result.trim(), equalTo(pgn.trim()));
    }

    @Test
    public void testThatASingleGameIsSavedIntoTheDatabaseAndSearchableByPosition() throws   Exception {
        // setup
        String pgn = loadPgnForAnandNakamura();
        String finalPosition = URLEncoder.encode("1R5Q/5p2/2p1p1kb/2PpP2p/3Pq1pP/6P1/5P1K/8", "UTF-8");

        // save game & then find position
        target("game/pgn").request().post(Entity.text(pgn));
        GameList gameList = target("game/position/" + finalPosition).request().get(GameList.class);

        // verify
        assertThat(gameList.games.size(), equalTo(1));
        assertTrue(gameList.games.get(0).contains("Anand") && gameList.games.get(0).contains("Nakamura"));
    }

    @Test
    public void testThat6RuyLopezPositionsAreFoundFromDatabase() throws IOException {
        // setup
        FileUtils.copyFileToDirectory(new File("src/integration/resources/shamkir.pgn"), new File(IN_DIRECTORY));
        String encodedPosition = URLEncoder.encode("r1bqkbnr/pppp1ppp/2n5/1B2p3/4P3/5N2/PPPP1PPP/RNBQK2R", "UTF-8");

        // act
        pgnDirectoryProcessor.run();

        // verify
        GameList gameList = target("game/position/" + encodedPosition).request().get(GameList.class);
        assertThat(gameList.games.size(), equalTo(6));
    }

    private String loadPgnForAnandNakamura() {
        return "[Event \"4th Zurich CC Classical\"]\n" +
                "[Site \"Zurich SUI\"]\n" +
                "[Date \"2015.02.17\"]\n" +
                "[Round \"4\"]\n" +
                "[White \"Anand, V.\"]\n" +
                "[Black \"Nakamura, Hi\"]\n" +
                "[Result \"1-0\"]\n" +
                "[ECO \"D37\"]\n" +
                "[WhiteElo \"2797\"]\n" +
                "[BlackElo \"2776\"]\n" +
                "[PlyCount \"81\"]\n" +
                "[EventDate \"2015.02.14\"]\n" +
                "\n" +
                "1. d4 d5 2. c4 e6 3. Nc3 Be7 4. Nf3 Nf6 5. Bf4 O-O 6. e3 Nbd7 7. c5 Nh5 8. Bd3 Nxf4 9. exf4 b6 10. b4 a5 11. a3 c6 12.\n" +
                " O-O Qc7 13. g3 Ba6 14. Re1 Bf6 15. Kg2 Bxd3 16. Qxd3 Rfb8 17. h4 Qa7 18. Ne2 g6 19. Rab1 axb4 20. axb4 Qa2 21. Rec1 bxc5\n" +
                " 22. bxc5 h5 23. Ne5 Nxe5 24. fxe5 Bg7 25. Rb6 Rc8 26. Nc3 Qa7 27. Rcb1 Qd7 28. R1b4 Bh6 29. Na4 Qd8 30. Ra6 Kg7 31. Rb7\n" +
                " Rxa6 32. Qxa6 g5 33. Qe2 g4 34. Qa6 Qg8 35. Nb6 Rf8 36. Nd7 Qh7 37. Nxf8 Qe4+ 38. Kh2 Kxf8 39. Rb8+ Kg7 40. Qc8 Kg6 41.\n" +
                " Qh8 1-0\n" +
                "\n";
    }
}
