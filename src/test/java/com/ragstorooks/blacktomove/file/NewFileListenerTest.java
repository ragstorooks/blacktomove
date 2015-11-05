package com.ragstorooks.blacktomove.file;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

public class NewFileListenerTest {
    private static final String TMP_DIRECTORY = System.getProperty("java.io.tmpdir") + File.separator;
    private static final String IN_DIRECTORY = TMP_DIRECTORY + "in-processor";

    private PgnFileProcessor pgnFileProcessor = mock(PgnFileProcessor.class);
    private NewFileListener newFileListener;
    private Thread thread;

    @Before
    public void setup() throws IOException {
        FileUtils.forceMkdir(new File(IN_DIRECTORY));
        newFileListener = new NewFileListener(IN_DIRECTORY, pgnFileProcessor);

        thread = new Thread(newFileListener);
        thread.start();
    }

    @After
    public void delete() throws IOException {
        try {
            thread.interrupt();
        } catch(Exception e) {}

        FileUtils.deleteDirectory(new File(IN_DIRECTORY));
    }

    @Test
    public void shouldReceiveNewFileCreationEventAndInvokePgnProcessor() throws Exception {
        // setup
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        doAnswer(invocation -> {
            countDownLatch.countDown();
            return null;
        }).when(pgnFileProcessor).parse(isA(File.class));

        // act
        File file = new File(IN_DIRECTORY + File.separator + "1.pgn");
        FileUtils.writeStringToFile(file, "just some test");

        // verify
        countDownLatch.await(1, TimeUnit.SECONDS);
        verify(pgnFileProcessor).parse(file);
    }
}