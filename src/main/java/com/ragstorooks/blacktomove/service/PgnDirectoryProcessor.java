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

@Singleton
public class PgnDirectoryProcessor implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(PgnDirectoryProcessor.class);

    private File inDirectory;
    private File outDirectory;

    private ChessDatabaseService chessDatabaseService;

    @Inject
    PgnDirectoryProcessor(@Named("Input Dir") String inDirectory, @Named("Output Dir") String outDirectory,
                          ChessDatabaseService chessDatabaseService) {
        this.inDirectory = new File(inDirectory);
        this.outDirectory = new File(outDirectory);
        this.chessDatabaseService = chessDatabaseService;

        if (!this.inDirectory.isDirectory() || !this.outDirectory.isDirectory()) {
            logger.error("in-dir {} and out-dir {} need to be valid directories", inDirectory, outDirectory);
            throw new IllegalArgumentException(inDirectory + " and " + outDirectory + " need to be valid directories");
        }
    }

    @Override
    public void run() {
        File[] fileList = inDirectory.listFiles();
        for (File file : fileList) {
            logger.info("Processing file {} and saving into the database", file);
            try {
                String fileContents = FileUtils.readFileToString(file, Charset.forName("UTF-8"));
                Response response = chessDatabaseService.saveGamesWithPgn(fileContents);
                if (response.getStatus() == Response.Status.CREATED.getStatusCode()) {
                    logger.info("Processed all games in file {}", file);
                    FileUtils.moveFileToDirectory(file, outDirectory, false);
                } else
                    logger.warn("Couldn't save all games in file {}, response status {}", file, response.getStatus());
            } catch (IOException e) {
                logger.error("Unable to read file " + file + ", skipping...", e);
            }
        }
    }
}
