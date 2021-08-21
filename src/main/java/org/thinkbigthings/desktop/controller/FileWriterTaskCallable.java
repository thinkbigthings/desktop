package org.thinkbigthings.desktop.controller;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class FileWriterTaskCallable implements TaskCallable<Void> {

    private long taskNumLines;
    private String taskFilename;

    private Supplier<Boolean> cancelledStatus = () -> false;
    private BiConsumer<Long, Long> progressStatus = (a, b) -> {};

    public FileWriterTaskCallable(long taskNumLines, String taskFilename) {
        this.taskNumLines = taskNumLines;
        this.taskFilename = taskFilename;
    }

    @Override
    public void setCancelledStatusSupplier(Supplier<Boolean> cancelledStatusSupplier) {
        cancelledStatus = cancelledStatusSupplier;
    }

    @Override
    public void setProgressStatusCallback(BiConsumer<Long, Long> updater) {
        this.progressStatus = updater;
    }

    public long getTaskNumLines() {
        return taskNumLines;
    }

    public String getTaskFilename() {
        return taskFilename;
    }

    @Override
    public Void call() throws IOException {

        progressStatus.accept(0L, taskNumLines);

        Random random = new Random();

        // Task is supposed to throw an exception if a result could not be reached
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(taskFilename))) {
            for (long i = 0; i < taskNumLines; i++) {
                if (cancelledStatus.get()) {
                    System.out.println("Cancelling...");
                    break;
                }
                writer.write(Long.toString(random.nextLong()));
                writer.newLine();

                progressStatus.accept(i, taskNumLines);
            }
        }

        progressStatus.accept(taskNumLines, taskNumLines);

        return null;
    }
}
