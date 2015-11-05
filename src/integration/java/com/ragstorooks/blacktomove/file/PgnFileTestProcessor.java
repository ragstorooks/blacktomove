package com.ragstorooks.blacktomove.file;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.ragstorooks.blacktomove.service.ChessDatabaseService;

import java.io.File;
import java.util.concurrent.CountDownLatch;

@Singleton
public class PgnFileTestProcessor extends PgnFileProcessor {
    private CountDownLatch countDownLatch;

    @Inject
    PgnFileTestProcessor(@Named("Output Dir") String outDirectory, @Named("Errors Dir") String errorsDirectory, ChessDatabaseService chessDatabaseService) {
        super(outDirectory, errorsDirectory, chessDatabaseService);
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    @Override
    public void parse(File file) {
        super.parse(file);

        countDownLatch.countDown();
    }
}
