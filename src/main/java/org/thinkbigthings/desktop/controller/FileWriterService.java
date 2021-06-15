package org.thinkbigthings.desktop.controller;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

@org.springframework.stereotype.Service
public class FileWriterService extends javafx.concurrent.Service<Void> {

    private StringProperty filename = new SimpleStringProperty(this, "");
    private LongProperty numLines = new SimpleLongProperty(this, "");

    public final void setNumLines(Long value) { numLines.set(value); }
    public final Long getNumLines() { return numLines.get(); }
    public final LongProperty numLinesProperty() { return numLines; }

    public final void setFilename(String value) { filename.set(value); }
    public final String getFilename() { return filename.get(); }
    public final StringProperty filenameProperty() { return filename; }

    @Override
    protected Task<Void> createTask() {

        final String taskFilename = getFilename();
        final long taskNumLines = getNumLines();

        return new FileWriterTask(taskNumLines, taskFilename);
    }

    public static class FileWriterTask extends Task<Void> {

        long taskNumLines;
        String taskFilename;

        public FileWriterTask(long taskNumLines, String taskFilename) {
            this.taskNumLines = taskNumLines;
            this.taskFilename = taskFilename;
        }

        @Override
        protected Void call() {

            // this is run in a background thread
            // any update to the UI from this thread would need to use Platform.runLater()

            updateProgress(0L, taskNumLines);

            Random random = new Random();

            try(BufferedWriter writer = new BufferedWriter( new FileWriter(taskFilename))) {
                for (long i = 0; i < taskNumLines; i++) {
                    if (isCancelled()) {
                        System.out.println("Cancelling...");
                        break;
                    }
                    writer.write(Long.toString(random.nextLong()));
                    writer.newLine();

                    updateProgress(i, taskNumLines);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            updateProgress(taskNumLines, taskNumLines);

            return null;
        }

    }

}
