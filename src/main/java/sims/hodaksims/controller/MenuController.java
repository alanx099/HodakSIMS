package sims.hodaksims.controller;

import javafx.fxml.FXML;
import sims.hodaksims.model.View;

public class MenuController {

    @FXML
    protected void switchToSceneAddSkaldiste() {
        ScreenManagerController.switchTo(View.ADDWAREHOUSE);
    }

}
