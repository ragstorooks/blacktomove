package com.ragstorooks.blacktomove;

import com.google.inject.*;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.ragstorooks.blacktomove.aop.ExceptionHandlingModule;
import com.ragstorooks.blacktomove.database.DatabaseModule;
import com.ragstorooks.blacktomove.file.NewFileListener;
import com.ragstorooks.blacktomove.service.ChessDatabaseService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class HttpServer implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(HttpServer.class);

    private int httpPort;
    private Server jettyServer;

    private ChessDatabaseService chessDatabaseService;

    @Inject
    HttpServer(ChessDatabaseService chessDatabaseService, @Named("Http Port") int httpPort) {
        this.chessDatabaseService = chessDatabaseService;
        this.httpPort = httpPort;
    }

    public void run() {
        ResourceConfig resourceConfig = new ResourceConfig().register(chessDatabaseService);
        ServletHolder servletHolder = new ServletHolder(new ServletContainer(resourceConfig));

        ServletContextHandler contextHandler = new ServletContextHandler();
        contextHandler.setContextPath("");
        contextHandler.addServlet(servletHolder, "/*");

        jettyServer = new Server(httpPort);
        jettyServer.setHandler(contextHandler);

        logger.info("Starting the http server on port {}", httpPort);
        try {
            jettyServer.start();
            jettyServer.join();
        } catch (Exception e) {
            logger.error("Error running the http server, exiting", e);
        } finally {
            jettyServer.destroy();
        }
    }

    public static void main(String[] args) {
        int httpPort = Integer.parseInt(System.getProperty("blacktomove_http_port", "80"));
        String dbConnectionString = System.getProperty("blacktomove_database_conn", "datomic:mem://blacktomove-integration");
        String inDirectory = System.getProperty("blacktomove_in_directory", "infiles");
        String outDirectory = System.getProperty("blacktomove_out_directory", "outfiles");
        String errorsDirectory = System.getProperty("blacktomove_errors_directory", "errfiles");

        Injector injector = Guice.createInjector(new DatabaseModule(), new ExceptionHandlingModule(), new AbstractModule() {
            @Override
            protected void configure() {
                bind(Integer.class).annotatedWith(Names.named("Http Port")).toInstance(httpPort);
                bind(String.class).annotatedWith(Names.named("Connection String")).toInstance(dbConnectionString);
                bind(String.class).annotatedWith(Names.named("Input Dir")).toInstance(inDirectory);
                bind(String.class).annotatedWith(Names.named("Output Dir")).toInstance(outDirectory);
                bind(String.class).annotatedWith(Names.named("Errors Dir")).toInstance(errorsDirectory);
            }
        });

        NewFileListener newFileListener = injector.getInstance(NewFileListener.class);
        new Thread(newFileListener).start();

        HttpServer httpServer = injector.getInstance(HttpServer.class);
        new Thread(httpServer).start();
    }
}
