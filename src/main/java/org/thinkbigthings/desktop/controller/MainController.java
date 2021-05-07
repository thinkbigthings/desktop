package org.thinkbigthings.desktop.controller;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;

import java.util.function.Consumer;

public class MainController {

    @FXML
    Button workButton;

    @FXML
    ProgressBar progressBar;

    @FXML
    Button cancelButton;

    Service<String> service;

    public MainController() {
        service = new WorkService(s -> {
            workButton.setDisable(false);
            System.out.println(s);
        });

        service.progressProperty().addListener((observable, oldValue, newValue) -> {
            boolean running = newValue.doubleValue() < 1.0;
            progressBar.setProgress(newValue.doubleValue());
            progressBar.setVisible(running);
            cancelButton.setVisible(running);
        });
    }

    public void clickWorkButton(ActionEvent actionEvent) {

        workButton.setDisable(true);

        System.out.println("click " + actionEvent + " " + System.currentTimeMillis());

        service.reset();
        service.start();
    }

    public void clickOpenMenuItem(ActionEvent actionEvent) {
        System.out.println("Open!");
    }

    public void clickCancelButton(ActionEvent actionEvent) {
        service.cancel();
    }

    public static class WorkService extends Service<String> {

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

                    this.updateProgress(0.0, 1.0);


                    long seconds = 10;
                    for(long i = 0; i < seconds; i++) {
                        if(isCancelled()) {
                            System.out.println("Cancelling...");
                            break;
                        }

                        // Block the thread for a short time, but be sure
                        // to check the InterruptedException for cancellation
                        try {
                            Thread.sleep(1000);
                        }
                        catch (InterruptedException interrupted) {
                            if (isCancelled()) {
                                System.out.println("Interrupted...");
                                break;
                            }
                        }

                        this.updateProgress(i, seconds);
                    }

                    this.updateProgress(1.0, 1.0);

                    // any update to the UI from this thread would need to use Platform.runLater()
//                    Platform.runLater(() -> {
//                        // getProgress() can only be called from the UI thread
//                        System.out.println(this.getProgress());
//                    });

                    return "Task Complete at " + System.currentTimeMillis();
                }
            };
        }
    }
}
