package org.thinkbigthings.desktop.controller;

import javafx.concurrent.Task;

public class GeneralTask<T> extends Task<T> {

    private TaskCallable<T> callable;

    public GeneralTask(TaskCallable<T> c) {
        callable = c;
    }

    @Override
    protected T call() throws Exception {

        callable.setProgressStatusCallback(this::updateProgress);
        callable.setCancelledStatusSupplier(this::isCancelled);

        return callable.call();
    }

}
