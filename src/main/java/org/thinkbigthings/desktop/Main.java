package org.thinkbigthings.desktop;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

// TODO use DI, split application into Components
// https://stackoverflow.com/questions/28804012/javafx-fxml-how-to-use-spring-di-with-nested-custom-controls/29777101

// TODO use design pattern for app structure https://fxdocs.github.io/docs/html5/#_application_structure

// TODO use property bindings for other components

// TODO try scene builder

// TODO integration test with Robot

public class Main extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(Launcher.class).run();
    }

    @Override
    public void start(Stage stage) {
        applicationContext.publishEvent(new StageReadyEvent(stage));
    }

    @Override
    public void stop() {
        applicationContext.close();
        Platform.exit();
    }

}