package com.ragstorooks.blacktomove.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

@Singleton
public class PgnDirectoryProcessor implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(PgnDirectoryProcessor.class);

    private static final String MULTI_GAME_SPLITTER = "^\\s*$[\\r\\n]+^\\[";
    private static final Pattern MULTI_GAME_SPLITTER_PATTERN = Pattern.compile(MULTI_GAME_SPLITTER, Pattern.MULTILINE);

    private File inDirectory;
    private File outDirectory;
    private File errorsDirectory;

    private ChessDatabaseService chessDatabaseService;

    @Inject
    PgnDirectoryProcessor(@Named("Input Dir") String inDirectory, @Named("Output Dir") String outDirectory,
                          @Named("Errors Dir") String errorsDirectory, ChessDatabaseService chessDatabaseService) {
        this.inDirectory = new File(inDirectory);
        this.outDirectory = new File(outDirectory);
        this.errorsDirectory = new File(errorsDirectory);
        this.chessDatabaseService = chessDatabaseService;

        if (!this.inDirectory.isDirectory() || !this.outDirectory.isDirectory() || !this.errorsDirectory.isDirectory()) {
            logger.error("in-dir {}, out-dir {} and errors-dir {} need to be valid directories", inDirectory,
                    outDirectory, errorsDirectory);
            throw new IllegalArgumentException(inDirectory + " and " + outDirectory + " need to be valid directories");
        }
    }

    @Override
    public void run() {
        File[] fileList = inDirectory.listFiles();
        for (File file : fileList) {
            try {
                String fileContents = FileUtils.readFileToString(file, Charset.forName("UTF-8"));

                String[] pgnGames = MULTI_GAME_SPLITTER_PATTERN.split(fileContents);
                logger.info("Processing file {}, which has {} games", file, pgnGames.length);

                int errorCount = 0;
                for (String pgnGame : pgnGames) {
                    pgnGame = pgnGame.trim();
                    if (!pgnGame.startsWith("["))
                        pgnGame = "[" + pgnGame;

                    Response response = chessDatabaseService.savePgn(pgnGame);
                    if (!(response.getStatus() == Response.Status.CREATED.getStatusCode()))
                        errorCount++;
                }

                if (errorCount > 0) {
                    logger.warn("Couldn't save {} games in file {}", errorCount, file);
                    FileUtils.moveFileToDirectory(file, errorsDirectory, false);
                } else {
                    logger.info("Processed all {} games in file {}", pgnGames.length, file);
                    FileUtils.moveFileToDirectory(file, outDirectory, false);
                }
            } catch (IOException e) {
                logger.error("Unable to read file " + file + ", skipping...", e);
            }
        }
    }
}
