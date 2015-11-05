package com.ragstorooks.blacktomove.file;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.ragstorooks.blacktomove.service.ChessDatabaseService;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

@Singleton
public class PgnFileProcessor {
    private static final Logger logger = LoggerFactory.getLogger(PgnFileProcessor.class);

    private static final String MULTI_GAME_SPLITTER = "^\\s*$[\\r\\n]+^\\[";
    private static final Pattern MULTI_GAME_SPLITTER_PATTERN = Pattern.compile(MULTI_GAME_SPLITTER, Pattern.MULTILINE);

    private File outDirectory;
    private File errorsDirectory;

    private ChessDatabaseService chessDatabaseService;

    @Inject
    PgnFileProcessor(@Named("Output Dir") String outDirectory, @Named("Errors Dir") String errorsDirectory,
                     ChessDatabaseService chessDatabaseService) {
        this.outDirectory = new File(outDirectory);
        this.errorsDirectory = new File(errorsDirectory);
        this.chessDatabaseService = chessDatabaseService;

        if (!this.outDirectory.isDirectory() || !this.errorsDirectory.isDirectory()) {
            logger.error("out-dir {} and errors-dir {} need to be valid directories", outDirectory, errorsDirectory);
            throw new IllegalArgumentException(outDirectory + " and " + errorsDirectory + " need to be valid directories");
        }
    }

    public void parse(File file) {
        try {
            String fileContents = FileUtils.readFileToString(file, Charset.forName("UTF-8"));

            String[] pgnGames = MULTI_GAME_SPLITTER_PATTERN.split(fileContents);
            logger.info("Processing file {}, which has {} games", file, pgnGames.length);

            int errorCount = 0;
            for (String pgnGame : pgnGames) {
                pgnGame = pgnGame.trim();
                if (!pgnGame.startsWith("["))
                    pgnGame = "[" + pgnGame;

                Response response = null;
                try {
                    response = chessDatabaseService.savePgn(pgnGame);
                } catch (Exception e) {
                    logger.error("Unexpected expection, expected the aspect to handle it!", e);
                }
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
