package org.thinkbigthings.desktop;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

    private String title;
    private ConfigurableApplicationContext applicationContext;

    private final URL mainFxml = getClass().getResource("main.fxml");

    public StageInitializer(@Value("${application.ui.title}") String appTitle, ConfigurableApplicationContext context) {
        this.title = appTitle;
        this.applicationContext = context;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {

        try {
            Stage stage = event.getStage();
            FXMLLoader loader = new FXMLLoader(mainFxml);
            loader.setControllerFactory(applicationContext::getBean);
            Parent p = loader.load();
            stage.setTitle(title);
            stage.setScene(new Scene(p, 800, 600));
            stage.show();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
