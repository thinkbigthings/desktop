package org.thinkbigthings.desktop.controller;

import javafx.concurrent.Service;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import org.springframework.stereotype.Component;

@Component
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

}
