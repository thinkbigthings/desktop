package org.thinkbigthings.desktop;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

    @Override
    public void onApplicationEvent(StageReadyEvent event) {

        Stage stage = event.getStage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));

        try {
            Parent p = loader.load();
            stage.setScene(new Scene(p));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        stage.show();
    }
}
