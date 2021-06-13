package org.thinkbigthings.desktop.controller;

import javafx.concurrent.Task;

@org.springframework.stereotype.Service
public class WorkServiceComponent extends javafx.concurrent.Service<String> {

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
            protected String call() {

                // this is run in a background thread
                // any update to the UI from this thread would need to use Platform.runLater()

                this.updateProgress(0.0, 1.0);

                long ms = 5_000;
                for (long i = 0; i < ms; i++) {
                    if (isCancelled()) {
                        System.out.println("Cancelling...");
                        break;
                    }

                    // Block the thread for a short time, but be sure
                    // to check the InterruptedException for cancellation.
                    // Interrupt acts on blocking calls on the thread
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException interrupted) {
                        if (isCancelled()) {
                            System.out.println("Interrupted...");
                            break;
                        }
                    }

                    this.updateProgress(i, ms);
                }

                this.updateProgress(1.0, 1.0);

                return "Task Complete at " + System.currentTimeMillis();
            }
        };
    }
}
