package sims.hodaksims.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sims.hodaksims.model.View;

import java.io.IOException;
import java.util.Objects;

public class ScreenManagerController {
    private static final Logger log = LoggerFactory.getLogger(ScreenManagerController.class);
    private ScreenManagerController() {
    }

    private static Scene scene;
    public static void setScene(Scene scene){
        ScreenManagerController.scene = scene;
    }


    public static void switchTo(View view){
        if (scene == null) {
            log.info("Nije odabrana scena");
            return;
        }
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(ScreenManagerController.class.getResource(view.getFilename())));
           scene.setRoot(root);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

}
