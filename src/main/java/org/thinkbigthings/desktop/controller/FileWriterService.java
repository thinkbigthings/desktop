package org.thinkbigthings.desktop.controller;

import javafx.beans.property.*;
import javafx.concurrent.Task;

import java.io.File;

@org.springframework.stereotype.Service
public class FileWriterService extends javafx.concurrent.Service<Void> {

    private SimpleObjectProperty<File> file = new SimpleObjectProperty<>(null, "");
    private LongProperty numLines = new SimpleLongProperty(this, "");

    public File getFile() {
        return file.get();
    }
    public void setFile(File newFile) {
        file.set(newFile);
    }
    public SimpleObjectProperty<File> fileProperty() {
        return file;
    }

    public final void setNumLines(Long value) { numLines.set(value); }
    public final Long getNumLines() { return numLines.get(); }
    public final LongProperty numLinesProperty() { return numLines; }

    @Override
    protected Task<Void> createTask() {

        // this Service is the builder that coordinates the task logic with the task itself
        // the task and task logic are separated to make unit testing easier
        // (can test a task's core logic without running the JavaFX Platform)

        return new GeneralTask<>(new FileWriterTaskCallable(getNumLines(), getFile()));
    }

}
