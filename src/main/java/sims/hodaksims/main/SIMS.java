package sims.hodaksims.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sims.hodaksims.controller.ScreenManagerController;
import sims.hodaksims.model.View;

public class SIMS extends Application {
    /**
     * Ulaz u aplikaciju
     * ovdje se pokreće sve
     */
    private static final Logger log = LoggerFactory.getLogger(SIMS.class);

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(new Region(), 1065, 611);
        ScreenManagerController.setScene(scene);
        ScreenManagerController.switchTo(View.LOGIN);
        stage.setTitle("SIMS");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}