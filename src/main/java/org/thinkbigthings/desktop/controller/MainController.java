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

    Service<String> service;

    public MainController() {
        service = new WorkService(s -> {
            workButton.setDisable(false);
            System.out.println(s);
        });

        service.progressProperty().addListener((observable, oldValue, newValue) -> {
            progressBar.setProgress(newValue.doubleValue());
            progressBar.setVisible(newValue.doubleValue() < 1.0);
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

    public static class WorkService extends Service<String> {

        public WorkService(Consumer<String> successFunction) {

            // this is called by the UI thread, replaces overriding of Service.succeeded()
            setOnSucceeded((e) -> successFunction.accept(e.getSource().getValue().toString()));
        }

        @Override
        protected Task<String> createTask() {
            return new Task<>() {
                @Override
                protected String call() throws Exception {

                    // this is run in a background thread

                    this.updateProgress(0.0, 1.0);

                    for(long i = 0; i < 200; i++) {
                        Thread.sleep(10);
                        this.updateProgress(i, 200L);
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
