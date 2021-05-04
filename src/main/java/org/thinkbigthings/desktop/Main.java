package org.thinkbigthings.desktop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


// TODO use design pattern for app structure https://fxdocs.github.io/docs/html5/#_application_structure

// TODO use property bindings for other components

// TODO try scene builder

// TODO Use modules https://edencoding.com/runtime-components-error/

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));

        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}