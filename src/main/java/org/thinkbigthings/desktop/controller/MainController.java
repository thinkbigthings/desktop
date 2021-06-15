package org.thinkbigthings.desktop.controller;

import javafx.beans.value.ChangeListener;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;


@Component
public class MainController {

    @FXML
    TextField numberRowsField;

    @FXML
    TextField destinationFolderField;

    @FXML
    Button saveButton;

    @FXML
    ProgressBar progressBar;

    @FXML
    Button cancelButton;

    // task vs service
    // https://stackoverflow.com/questions/18880455/task-vs-service-for-database-operations

    // JFX Service creates a ThreadPoolExecutor for you
    // So if you create your own Tasks you need to manage your own thread pool
    // generally should use the execute method inherited from Executor to run a Task

    // Despite the similarity in name, a Spring Service is not like a JavaFX Service
    // A Spring Service in a web app should be a stateless singleton and can be shared among the object graph
    // (hence should be injected via dependency injection)
    // A JavaFX Service, once you want to do anything that is parameterized, is stateful as described here
    // see https://openjfx.io/javadoc/16/javafx.graphics/javafx/concurrent/Service.html
    // and does not lend itself to dependency injection or to being shared among the object graph.
    //
    // It is more of a background task mechanism meant for a specific Controller.



    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private Validator validator;
    private FileWriterService service;

    private EventHandler<WorkerStateEvent> successHandler = event -> {
        saveButton.setDisable(false);
        progressBar.setVisible(false);
        cancelButton.setVisible(false);
        System.out.println("Success");
    };

    private EventHandler<WorkerStateEvent> cancelHandler = event -> {
        saveButton.setDisable(false);
        progressBar.setVisible(false);
        cancelButton.setVisible(false);
        System.out.println("Cancelled");
    };

    private ChangeListener<? super Number> progressListener = (observable, oldValue, newValue) -> {
        progressBar.setProgress(newValue.doubleValue());
    };

    public MainController(FileWriterService workService, Validator validator) {
        this.service = workService;
        this.validator = validator;
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

    public void clickSaveButton(ActionEvent actionEvent) {

        saveButton.setDisable(true);
        progressBar.setVisible(true);
        cancelButton.setVisible(true);

        long numRows = Long.parseLong(numberRowsField.getText());
        String destFolder = destinationFolderField.getText();
        String filename = Paths.get(destFolder, "data.csv").toFile().getAbsolutePath();

        service.setFilename(filename);
        service.setNumLines(numRows);
        service.reset();
        service.start();

        System.out.println("click " + actionEvent + " " + System.currentTimeMillis());
    }

    public void clickOpenMenuItem(ActionEvent actionEvent) {
        System.out.println("Open!");
    }

    public void clickCancelButton(ActionEvent actionEvent) {
        service.cancel();
    }

    public void onDestinationFolderKeyPress(KeyEvent keyEvent) {
        saveButton.setDisable( ! validator.isFilePresent(destinationFolderField.getText()));
    }

    public void onNumberRowsKeyPress(KeyEvent keyEvent) {
        saveButton.setDisable( ! validator.isLong(this.numberRowsField.getText()));
    }
}
