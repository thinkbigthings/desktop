package org.thinkbigthings.desktop.controller;

import javafx.beans.value.ChangeListener;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


@Component
public class MainController {

    @FXML
    Button workButton;

    @FXML
    ProgressBar progressBar;

    @FXML
    Button cancelButton;

    private WorkServiceComponent service;

    private EventHandler<WorkerStateEvent> successHandler = event -> {
        workButton.setDisable(false);
        System.out.println(event.getSource().getValue().toString());
    };

    private EventHandler<WorkerStateEvent> cancelHandler = event -> {
        workButton.setDisable(false);
        System.out.println("Cancelled");
    };

    private ChangeListener<? super Number> progressListener = (observable, oldValue, newValue) -> {
        boolean running = newValue.doubleValue() < 1.0;
        progressBar.setProgress(newValue.doubleValue());
        progressBar.setVisible(running);
        cancelButton.setVisible(running);
    };

    public MainController(WorkServiceComponent workService) {
        this.service = workService;
    }

    @PostConstruct
    public void addListeners() {
        service.setOnSucceeded(successHandler);
        service.setOnCancelled(cancelHandler);
        service.progressProperty().addListener(progressListener);
    }

    @PreDestroy
    public void clearListeners() {
        service.setOnSucceeded(null);
        service.setOnCancelled(null);
        service.progressProperty().removeListener(progressListener);
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
