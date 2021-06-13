package org.thinkbigthings.desktop;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

// TODO split application into more UI components
// use DI to show off how DI helps with the complexity
// start unit tests

// TODO use design pattern for app structure https://fxdocs.github.io/docs/html5/#_application_structure
// do we need the <children> tags?

// TODO use event bus for events that cross UI boundaries
// https://thickclient.blog/2019/04/17/decoupled-controllers-events/

// TODO use property bindings for other components

// TODO try scene builder
// unfortunately blocked on https://youtrack.jetbrains.com/issue/IDEA-266524

// TODO make an executable jar that is directly executable
// executable flag in gradle plugin?

// TODO integration test the UI

public class Main extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {

        // make JavaFX objects available elsewhere through Spring DI
        ApplicationContextInitializer<GenericApplicationContext> initializer = ac -> {
            ac.registerBean(Application.class, () -> Main.this);
            ac.registerBean(Parameters.class, this::getParameters);
            ac.registerBean(HostServices.class, this::getHostServices);
        };

        applicationContext = new SpringApplicationBuilder(Launcher.class)
                .sources(Launcher.class)
                .initializers(initializer)
                .run(getParameters().getRaw().toArray(new String[0]));
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