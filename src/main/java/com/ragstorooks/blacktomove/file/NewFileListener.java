package com.ragstorooks.blacktomove.file;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.WatchEvent.Kind;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

@Singleton
public class NewFileListener implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(NewFileListener.class);

    private PgnFileProcessor pgnFileProcessor;
    private Path directoryPath;
    private WatchKey watchKey;
    private WatchService watchService;

    @Inject
    NewFileListener(@Named("Input Dir") String directory, PgnFileProcessor pgnFileProcessor) {
        this.pgnFileProcessor = pgnFileProcessor;
        try {
            watchService = FileSystems.getDefault().newWatchService();
        } catch (IOException e) {
            logErrorAndThrow("Unable to register a watcher on directory " + directory);
        }

        if (!new File(directory).isDirectory()) {
            logErrorAndThrow(directory + " needs to be a valid directory");
        }

        directoryPath = Paths.get(directory);
        try {
            watchKey = registerFileListener(directoryPath);
        } catch (IOException e) {
            logErrorAndThrow("Unable to register a listener on directory " + directory);
        }
    }

    private void logErrorAndThrow(String message) {
        logger.error(message);
        throw new IllegalArgumentException(message);
    }

    private WatchKey registerFileListener(Path directoryPath) throws IOException {
        return directoryPath.register(watchService, ENTRY_CREATE);
    }

    @Override
    public void run() {
        for (;;) {
            try {
                logger.info("Waiting for a new PGN file to be created");

                WatchKey take = watchService.take();
                if (watchKey.equals(take)) {
                    for (WatchEvent<?> watchEvent : watchKey.pollEvents()) {
                        Kind<?> kind = watchEvent.kind();
                        if (!ENTRY_CREATE.equals(kind)) {
                            logger.warn("Unknown event {} notification", kind);
                            continue;
                        }

                        WatchEvent<Path> event = (WatchEvent<Path>) watchEvent;
                        Path filePath = directoryPath.resolve(event.context());

                        logger.info("Processing PGN file {}", filePath);
                        pgnFileProcessor.parse(filePath.toFile());
                    }
                } else {
                    logger.error("Directory event received for unknown key {}, ignoring", take);
                }
            } catch (InterruptedException e) {
                logger.error("Interrupted while waiting for directory events", e);
                throw new RuntimeException(e);
            }
        }
    }
}
