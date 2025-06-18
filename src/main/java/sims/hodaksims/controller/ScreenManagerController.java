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
    /**
     * Klasa za menagment prikaza ekrana na aplikaciji
     * <p>
     *     Ova klasa je nastala radi potrebe za jednostavnim mjenjanjem java fx ekrana
     *     Ova klasa nema svoju instancu nego koristimo statičke metode iz nje
     *     kao bi kroz par jednostavnih metoda mjenjali ekrane na aplikaciji
     * <p/>
     */
    private static final Logger log = LoggerFactory.getLogger(ScreenManagerController.class);
    private ScreenManagerController() {
    }

    private static Scene scene;

    /**
     * setScene statička funkcija mjenja statičku varijablu scene unutar klase
     * @param scene
     */
    public static void setScene(Scene scene){
        ScreenManagerController.scene = scene;
    }

    /**
     * switchTo metoda radi izmjenu same scene,
     * kada nju pozovemo u kodu možemo odabrati stranicu koja
     * se nalazi unutar Veiew enumeracije
     * @see View
     * @param view
     */
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
