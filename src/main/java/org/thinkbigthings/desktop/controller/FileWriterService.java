package org.thinkbigthings.desktop.controller;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Task;

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

        // this Service is the builder that coordinates the task logic with the task itself
        // the task and task logic are separated to make unit testing easier
        // (can test a task's core logic without running the JavaFX Platform)

        return new GeneralTask<>(new FileWriterTaskCallable(getNumLines(), getFilename()));
    }

}
