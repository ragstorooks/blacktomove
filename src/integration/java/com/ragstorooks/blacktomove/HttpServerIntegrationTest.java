package com.ragstorooks.blacktomove;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.ragstorooks.blacktomove.aop.ExceptionHandlingModule;
import com.ragstorooks.blacktomove.database.DatabaseModule;
import com.ragstorooks.blacktomove.file.NewFileListener;
import com.ragstorooks.blacktomove.service.GameList;
import datomic.Connection;
import datomic.Peer;
import datomic.Util;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class HttpServerIntegrationTest {
    private static final String TMP_DIRECTORY = System.getProperty("java.io.tmpdir") + File.separator;
    private static final String IN_DIRECTORY = TMP_DIRECTORY + "in-processor";
    private static final String OUT_DIRECTORY = TMP_DIRECTORY + "out-processor";
    private static final String ERRORS_DIRECTORY = TMP_DIRECTORY + "errors-processor";

    private static final String SCHEMA_FILE = "src/main/resources/db/schema.edn";
    private static final String CONNECTION_STRING = "datomic:mem://blacktomove-integration";

    private static Injector injector;
    private static HttpServer httpServer;

    private static void load(String fileName) throws FileNotFoundException, InterruptedException, ExecutionException {
        Reader schemaReader = new FileReader(fileName);
        List schemaTransaction = (List) Util.readAll(schemaReader).get(0);

        Connection connection = Peer.connect(CONNECTION_STRING);
        connection.transact(schemaTransaction).get();
    }

    @BeforeClass
    public static void setupDatabaseAndSingletons() throws Exception {
        Peer.createDatabase(CONNECTION_STRING);
        load(SCHEMA_FILE);

        FileUtils.forceMkdir(new File(IN_DIRECTORY));
        FileUtils.forceMkdir(new File(OUT_DIRECTORY));
        FileUtils.forceMkdir(new File(ERRORS_DIRECTORY));

        injector = Guice.createInjector(new DatabaseModule(), new ExceptionHandlingModule(), new AbstractModule() {
            @Override
            protected void configure() {
                bind(Integer.class).annotatedWith(Names.named("Http Port")).toInstance(8080);
                bind(String.class).annotatedWith(Names.named("Connection String")).toInstance(CONNECTION_STRING);
                bind(String.class).annotatedWith(Names.named("Input Dir")).toInstance(IN_DIRECTORY);
                bind(String.class).annotatedWith(Names.named("Output Dir")).toInstance(OUT_DIRECTORY);
                bind(String.class).annotatedWith(Names.named("Errors Dir")).toInstance(ERRORS_DIRECTORY);
            }
        });

        new Thread(injector.getInstance(NewFileListener.class)).start();

        httpServer = injector.getInstance(HttpServer.class);
        new Thread(httpServer).start();
    }

    @AfterClass
    public static void deleteDirectories() throws IOException {
        FileUtils.deleteDirectory(new File(IN_DIRECTORY));
        FileUtils.deleteDirectory(new File(OUT_DIRECTORY));
        FileUtils.deleteDirectory(new File(ERRORS_DIRECTORY));
    }

    @Test
    public void testThat6RuyLopezPositionsAreFoundFromDatabase() throws Exception {
        // setup
        FileUtils.copyFileToDirectory(new File("src/integration/resources/shamkir.pgn"), new File(IN_DIRECTORY));
        String encodedPosition = URLEncoder.encode("r1bqkbnr/pppp1ppp/2n5/1B2p3/4P3/5N2/PPPP1PPP/RNBQK2R", "UTF-8");

        Thread.sleep(30000);

        // verify
        GameList gameList = getGames(encodedPosition);
        assertThat(gameList.games.size(), equalTo(6));
    }

    private GameList getGames(String position) throws IOException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet method = new HttpGet("http://localhost:8080/game/position/" + position);

        HttpResponse httpResponse = httpClient.execute(method);
        BufferedReader in = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
        return new ObjectMapper().reader(GameList.class).readValue(in.readLine());
    }
}
