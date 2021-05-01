package org.thinkbigthings.desktop.controller;

import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.fxml.FXML;

import java.util.function.Consumer;

public class MainController {

    @FXML
    AnchorPane borderPane;

    @FXML
    Button workButton;

    public void clickWorkButton(ActionEvent actionEvent) {

        System.out.println("click " + actionEvent + " " + System.currentTimeMillis());

        Platform.runLater(() -> {
            workButton.setDisable(true);
            System.out.println(Thread.currentThread().getName() + " disabled runLater(): " + System.currentTimeMillis());
        });

        Service<String> service = new WorkService(s -> {
            workButton.setDisable(false);
            System.out.println(s);
        });

        service.start();
    }

    public static class WorkService extends Service<String> {

        private Consumer<String> onSuccess;

        public WorkService(Consumer<String> successFunction) {
            this.onSuccess = successFunction;
        }

        @Override
        protected void succeeded() {
            Platform.runLater(() -> onSuccess.accept(this.getValue()));
        }

        @Override
        protected Task<String> createTask() {
            return new Task<>() {
                @Override
                protected String call() throws Exception {
                    Thread.sleep(2000);
                    return "Task Complete at " + System.currentTimeMillis();
                }
            };
        }
    }
}
