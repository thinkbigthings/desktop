package org.thinkbigthings.desktop.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXML;

public class MainController {

    @FXML
    AnchorPane borderPane;

    @FXML
    Button workButton;

    public void clickWorkButton(ActionEvent actionEvent) throws InterruptedException {

        workButton.setDisable(true);
        System.out.println(actionEvent + " Working...");
        Thread.sleep(1000);
        workButton.setDisable(false);
    }
}
