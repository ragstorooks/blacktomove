package com.ragstorooks.blacktomove.service;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PgnDirectoryProcessorTest {
    private static final String TMP_DIRECTORY = System.getProperty("java.io.tmpdir") + File.separator;
    private static final String IN_DIRECTORY = TMP_DIRECTORY + "in-processor";
    private static final String OUT_DIRECTORY = TMP_DIRECTORY + "out-processor";
    private static final String ERRORS_DIRECTORY = TMP_DIRECTORY + "errors-processor";

    private static final String NEWLINE = System.lineSeparator();
    private String pgn1 = "[key1 \"value1\"]" + NEWLINE + "[key2 \"value2\"]" + NEWLINE + " " + NEWLINE +
            "1. e4 e5 1/2-1/2";
    private String pgn2 = "[2key1 \"2value1\"]" + NEWLINE + "[2key2 \"2value2\"]" + NEWLINE + NEWLINE +
            "1. c4 c5 2. Nc3 1-0";
    private String pgn3 = "[3key1 \"3value1\"]" + NEWLINE + "[3key2 \"3value2\"]" + NEWLINE + NEWLINE +
            "1. e4 c5 2. Nc3 0-1";

    private File inFile1;
    private File inFile2;
    private File inFile3;

    @Mock
    private ChessDatabaseService chessDatabaseService;
    private PgnDirectoryProcessor pgnDirectoryProcessor;

    private void setupInAndOutDirectories() throws IOException {
        FileUtils.forceMkdir(new File(IN_DIRECTORY));
        FileUtils.forceMkdir(new File(OUT_DIRECTORY));
        FileUtils.forceMkdir(new File(ERRORS_DIRECTORY));

        inFile1 = new File(IN_DIRECTORY + File.separator + "1.pgn");
        inFile2 = new File(IN_DIRECTORY + File.separator + "2.pgn");
        inFile3 = new File(IN_DIRECTORY + File.separator + "3.pgn");

        FileUtils.write(inFile1, pgn1);
        FileUtils.write(inFile2, pgn2);
        FileUtils.write(inFile3, pgn3);
    }

    @Before
    public void setupChessDatabaseService() {
        when(chessDatabaseService.savePgn(pgn1)).thenReturn(Response.created(null).build());
        when(chessDatabaseService.savePgn(pgn2)).thenReturn(Response.created(null).build());
        when(chessDatabaseService.savePgn(pgn3)).thenReturn(Response.created(null).build());
    }

    @Before
    public void setup() throws IOException {
        setupInAndOutDirectories();
        pgnDirectoryProcessor = new PgnDirectoryProcessor(IN_DIRECTORY, OUT_DIRECTORY, ERRORS_DIRECTORY, chessDatabaseService);
    }

    @After
    public void deleteDirectories() throws IOException {
        FileUtils.deleteDirectory(new File(IN_DIRECTORY));
        FileUtils.deleteDirectory(new File(OUT_DIRECTORY));
        FileUtils.deleteDirectory(new File(ERRORS_DIRECTORY));
    }

    @Test
    public void testThatAllFilesWithinInDirectoryAreSavedViaTheChessDatabaseService() throws IOException {
        // act
        pgnDirectoryProcessor.run();

        // assert
        verify(chessDatabaseService).savePgn(pgn1);
        verify(chessDatabaseService).savePgn(pgn2);
        verify(chessDatabaseService).savePgn(pgn3);

        assertFalse(inFile1.exists());
        assertFalse(inFile2.exists());
        assertFalse(inFile3.exists());
        assertThat(FileUtils.readFileToString(new File(OUT_DIRECTORY + File.separator + "1.pgn")), equalTo(pgn1));
        assertThat(FileUtils.readFileToString(new File(OUT_DIRECTORY + File.separator + "2.pgn")), equalTo(pgn2));
        assertThat(FileUtils.readFileToString(new File(OUT_DIRECTORY + File.separator + "3.pgn")), equalTo(pgn3));
    }

    @Test
    public void testThatAllFilesWithinInDirectoryAreMovedToOutDirectory() throws IOException {
        // act
        pgnDirectoryProcessor.run();

        // assert
        assertFalse(inFile1.exists());
        assertFalse(inFile2.exists());
        assertFalse(inFile3.exists());
        assertThat(FileUtils.readFileToString(new File(OUT_DIRECTORY + File.separator + "1.pgn")), equalTo(pgn1));
        assertThat(FileUtils.readFileToString(new File(OUT_DIRECTORY + File.separator + "2.pgn")), equalTo(pgn2));
        assertThat(FileUtils.readFileToString(new File(OUT_DIRECTORY + File.separator + "3.pgn")), equalTo(pgn3));
    }

    @Test
    public void testThatOnlyFilesWithNoErrorsAreMovedToOutDirectory() throws IOException {
        // setup
        when(chessDatabaseService.savePgn(pgn2)).thenReturn(Response.serverError().build());

        // act
        pgnDirectoryProcessor.run();

        // assert
        assertFalse(inFile1.exists());
        assertFalse(inFile3.exists());
        assertThat(FileUtils.readFileToString(new File(OUT_DIRECTORY + File.separator + "1.pgn")), equalTo(pgn1));
        assertThat(FileUtils.readFileToString(new File(OUT_DIRECTORY + File.separator + "3.pgn")), equalTo(pgn3));

        assertFalse(new File(OUT_DIRECTORY + File.separator + "2.pgn").exists());
    }

    @Test
    public void testThatFilesWithErrorsAreMovedToErrorsDirectory() throws IOException {
        // setup
        when(chessDatabaseService.savePgn(pgn2)).thenReturn(Response.serverError().build());

        // act
        pgnDirectoryProcessor.run();

        // assert
        assertFalse(inFile2.exists());
        assertThat(FileUtils.readFileToString(new File(ERRORS_DIRECTORY + File.separator + "2.pgn")), equalTo(pgn2));

        assertFalse(new File(ERRORS_DIRECTORY + File.separator + "1.pgn").exists());
        assertFalse(new File(ERRORS_DIRECTORY + File.separator + "3.pgn").exists());
    }

    @Test
    public void testThatMultipleGamesAreParsedAndProcessedFromASingleFile() throws IOException {
        // setup
        deleteFiles(inFile1, inFile2, inFile3);

        String multiGamePgn = pgn1 + NEWLINE + NEWLINE + pgn2 + NEWLINE + NEWLINE + pgn3;
        File multiGameFile = new File(IN_DIRECTORY + File.separator + "m.pgn");
        FileUtils.write(multiGameFile, multiGamePgn);

        // act
        pgnDirectoryProcessor.run();

        // assert
        verify(chessDatabaseService).savePgn(pgn1);
        verify(chessDatabaseService).savePgn(pgn2);
        verify(chessDatabaseService).savePgn(pgn3);

        assertFalse(multiGameFile.exists());
        assertThat(FileUtils.readFileToString(new File(OUT_DIRECTORY + File.separator + "m.pgn")), equalTo
                (multiGamePgn));
    }

    private void deleteFiles(File... files) {
        for (File f : files)
            f.delete();
    }
}