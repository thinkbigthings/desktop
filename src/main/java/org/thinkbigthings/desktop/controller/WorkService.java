package org.thinkbigthings.desktop.controller;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

import java.util.function.Consumer;


public class WorkService extends Service<String> {

    public WorkService(Consumer<String> finishingFunction) {

        // this is called by the UI thread, replaces overriding of Service.succeeded()
        setOnSucceeded((e) -> finishingFunction.accept(e.getSource().getValue().toString()));

        setOnCancelled((e) -> finishingFunction.accept("Cancelled"));
    }

    @Override
    protected void cancelled() {
        super.cancelled();
        System.out.println("Service Cancelled");
    }

    @Override
    protected Task<String> createTask() {
        return new Task<>() {

            @Override
            protected void cancelled() {
                super.cancelled();
                System.out.println("Task Cancelled");
            }

            @Override
            protected String call() throws Exception {

                // this is run in a background thread
                // any update to the UI from this thread would need to use Platform.runLater()

                this.updateProgress(0.0, 1.0);

                long seconds = 10;
                for (long i = 0; i < seconds; i++) {
                    if (isCancelled()) {
                        System.out.println("Cancelling...");
                        break;
                    }

                    // Block the thread for a short time, but be sure
                    // to check the InterruptedException for cancellation.
                    // Interrupt acts on blocking calls on the thread
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException interrupted) {
                        if (isCancelled()) {
                            System.out.println("Interrupted...");
                            break;
                        }
                    }

                    this.updateProgress(i, seconds);
                }

                this.updateProgress(1.0, 1.0);

                return "Task Complete at " + System.currentTimeMillis();
            }
        };
    }
}
